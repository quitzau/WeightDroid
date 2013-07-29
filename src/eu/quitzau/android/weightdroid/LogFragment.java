package eu.quitzau.android.weightdroid;

import java.util.List;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import eu.quitzau.android.weightdroid.dialog.LogItemUpdateDialog;
import eu.quitzau.android.weightdroid.dto.WeightDTO;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;

public class LogFragment extends ListFragment {

	public ArrayAdapter<WeightDTO> adapter;
	List<WeightDTO> weightDatas;

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		WeightDTO weightDTO = (WeightDTO) l.getItemAtPosition(position);
		showLogDetailsDialog(weightDTO, position);
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		weightDatas = WeightDAO.getInstance(getActivity().getApplicationContext()).getAllWeightDataForCurrentProfile();
		adapter = new WeightDTOListAdapter(getView().getContext(), R.layout.listweights, weightDatas);
		setListAdapter(adapter);

	}

	void showLogDetailsDialog(WeightDTO weight, int position) {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("details");

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = LogItemUpdateDialog.newInstance(weight, position);
		newFragment.show(ft, "details");

	}

}
