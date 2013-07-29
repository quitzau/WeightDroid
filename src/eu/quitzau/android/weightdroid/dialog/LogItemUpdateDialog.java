package eu.quitzau.android.weightdroid.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import eu.quitzau.android.weightdroid.LogFragment;
import eu.quitzau.android.weightdroid.PreferenceConstants;
import eu.quitzau.android.weightdroid.R;
import eu.quitzau.android.weightdroid.dto.WeightDTO;
import eu.quitzau.android.weightdroid.dto.WeightDTOComparator;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;

public class LogItemUpdateDialog extends DialogFragment {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM-yyyy");

	public static LogItemUpdateDialog newInstance(WeightDTO weight, int position) {
		LogItemUpdateDialog logUpdateDialog = new LogItemUpdateDialog();
		Bundle args = new Bundle();
		args.putSerializable("weight", weight);
		args.putInt("position", position);
		logUpdateDialog.setArguments(args);
		return logUpdateDialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.log_update, container, false);
		initializeLayout(v);
		return v;
	}

	public void initializeLayout(View v) {
		final WeightDAO weightDAO = WeightDAO.getInstance(getActivity().getApplicationContext());
		final LogFragment log = (LogFragment) getFragmentManager().findFragmentByTag("Log");

		final WeightDTO currentSelected = (WeightDTO) getArguments().getSerializable("weight");
		getDialog().setTitle(getString(R.string.logDetails) + " : " + simpleDateFormat.format(currentSelected.getDate()));
		final EditText weight = (EditText) v.findViewById(R.id.LogUpdate_weight);
		weight.setText("" + currentSelected.getWeight());

		final EditText size = (EditText) v.findViewById(R.id.LogUpdate_size);

		String sizeText = null;
		if (currentSelected.getSize() == -1) {
			sizeText = "";
		} else {
			sizeText = "" + currentSelected.getSize();
		}
		size.setText(sizeText);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		float height = Float.parseFloat(sharedPreferences.getString(PreferenceConstants.USER_HEIGHT, "0"));
		TextView bmi = (TextView) v.findViewById(R.id.LogUpdate_BMIText);
		String calBmi = "" + currentSelected.getWeight() / (Math.pow((height * +0.01), 2));
		bmi.setText("" + calBmi.substring(0, calBmi.indexOf(".") + 3));

		final DatePicker datePicker = (DatePicker) v.findViewById(R.id.LogUpdate_DatePicker);
		Calendar cal = new GregorianCalendar();
		cal.setTime(currentSelected.getDate());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
		Button updateButton = (Button) v.findViewById(R.id.LogUpdate_Button_ok);
		updateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar updatedCal = new GregorianCalendar();
				updatedCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
				currentSelected.setDate(updatedCal.getTime());
				currentSelected.setWeight(Float.parseFloat(weight.getText().toString()));
				if (!"".equals(size.getText().toString().trim())) {
					currentSelected.setSize(Float.parseFloat(size.getText().toString()));
				}
				weightDAO.updateWeightDataForCurrentProfile(currentSelected);
				log.adapter.remove(currentSelected);
				log.adapter.add(currentSelected);
				log.adapter.sort(new WeightDTOComparator());
				dismiss();
			}
		});

		ImageButton deleteButton = (ImageButton) v.findViewById(R.id.LogUpdate_Deletebutton);
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				weightDAO.deleteWeightDataForCurrentProfile(currentSelected);
				log.adapter.remove(currentSelected);
				dismiss();
			}
		});

		Button closeButton = (Button) v.findViewById(R.id.LogUpdate_Button_cancel);
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}
}
