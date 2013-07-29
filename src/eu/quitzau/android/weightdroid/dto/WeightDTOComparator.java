package eu.quitzau.android.weightdroid.dto;

import java.util.Comparator;

public class WeightDTOComparator implements Comparator<WeightDTO> {

	@Override
	public int compare(WeightDTO arg0, WeightDTO arg1) {
		int dateCmp = arg0.getDate().compareTo(arg1.getDate());
		return dateCmp;
	}

}
