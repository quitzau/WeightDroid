package eu.quitzau.android.weightdroid.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.os.Environment;
import eu.quitzau.android.weightdroid.dto.WeightDTO;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;

public class ExternalFileUtil {

	private static final String WEIGHTDROID_CSV = "weightdroid.csv";
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static ExternalFileUtil instance;
	

	public boolean externalStorageAvailable = false;
	public boolean externalStorageWriteable = false;
	
	protected Context ctx;
	
	private ExternalFileUtil()
	{
		
	}
	
	public static ExternalFileUtil getInstance(Context ctx)
	{
		if (instance == null) {
			instance = new ExternalFileUtil();
		}

		instance.ctx = ctx;

		instance.checkReadWriteState();
		
		return instance;
	}
	
	public void importCSV() throws ExternalFileException {
		WeightDAO weightDAO = WeightDAO.getInstance(ctx); 
		
		try {
			File externalStorageDirectory = Environment.getExternalStorageDirectory();
			File weightDroidCSV = new File(externalStorageDirectory.getAbsoluteFile() + "/weightdroid", WEIGHTDROID_CSV);
			BufferedReader bufRdr = new BufferedReader(new FileReader(weightDroidCSV));
			String line = null;

			// read each line of text file
			while ((line = bufRdr.readLine()) != null) {
				List<String> weightData = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String nextToken = st.nextToken();
					weightData.add(nextToken);
				}
				
				WeightDTO dto = new WeightDTO();
				dto.setDate(simpleDateFormat.parse(weightData.get(0)));
				dto.setWeight(Float.parseFloat(weightData.get(1)));
				dto.setSize(Float.parseFloat(weightData.get(2)));
				weightDAO.insertWeightData(dto);
			}

			// close the file
			bufRdr.close();
		} catch (FileNotFoundException e) {
			throw new ExternalFileException("Could not find " + WEIGHTDROID_CSV + " file", e);
		} catch (IOException e) {
			throw new ExternalFileException("Could not read " + WEIGHTDROID_CSV + " file", e);
		} catch (ParseException e) {
			throw new ExternalFileException("Could not convert strings in " + WEIGHTDROID_CSV + " file", e);
		}

	}
	
	public String exportCSV(boolean autobackup) throws ExternalFileException {
		
		
		WeightDAO weightDAO = WeightDAO.getInstance(ctx); 
		List<WeightDTO> allWeightDataForCurrentProfile = weightDAO.getAllWeightDataForCurrentProfile();
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		String path = externalStorageDirectory.getAbsolutePath() + "/weightdroid";
		File weightDroidFolder = new File(path);
		if(!weightDroidFolder.exists())
		{
			weightDroidFolder.mkdir();
		}
				
		String csvFilename = WEIGHTDROID_CSV;
		if(autobackup)
		{
			csvFilename =  "backup_" + simpleDateFormat.format(new Date()) + "_" + csvFilename ;
		}
		File weightDroidCSV = new File(weightDroidFolder, csvFilename);
		try {
			BufferedWriter bufWrt = new BufferedWriter(new FileWriter(weightDroidCSV));
			for (WeightDTO weightDTO : allWeightDataForCurrentProfile) {
				bufWrt.append(simpleDateFormat.format(weightDTO.getDate()) + ";" + weightDTO.getWeight() + ";" + weightDTO.getSize());
				bufWrt.newLine();
			}
			
			bufWrt.flush();
			bufWrt.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new ExternalFileException("Could not export file to " + path + "/" + csvFilename, e);
		}
		
		return path + "/" + csvFilename;
		
	}

	public void checkReadWriteState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			externalStorageAvailable = externalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		} else {
			externalStorageAvailable = externalStorageWriteable = false;
		}
	}
	
}
