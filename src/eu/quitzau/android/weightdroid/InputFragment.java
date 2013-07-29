package eu.quitzau.android.weightdroid;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import eu.quitzau.android.weightdroid.dialog.AlertDialog;
import eu.quitzau.android.weightdroid.dialog.BMIDialog;
import eu.quitzau.android.weightdroid.dto.WeightDTO;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;
import eu.quitzau.android.weightdroid.utilities.ExternalFileException;
import eu.quitzau.android.weightdroid.utilities.ExternalFileUtil;

public class InputFragment extends Fragment {

	private EditText weightEditText;
	private EditText sizeEditText;
	private Button submitWeightButton;
	private DatePicker datePicker;
	private TextView weightLogsCount;
	private TextView lastWeightLog;
	private TextView sizeLabel;
	private View v;
	private TextView sizeSpacer;
	private OnSharedPreferenceChangeListener sharedPrefsListener;
	private SharedPreferences prefs;

	private static float weight;
	private static float size;

	private SharedPreferences getPreferences() {
		if (prefs == null) {
			prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		}
		return prefs;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				if (PreferenceConstants.SHOW_SIZE.equalsIgnoreCase(key)) {
					showSize();
				}
			}
		};

		String user = getPreferences().getString(PreferenceConstants.USER, null);
		if (user == null) {
			showAlertDialog(getString(R.string.fillProfile), getString(R.string.fillProfileDetails));
		} else {
			if (getPreferences().getBoolean(PreferenceConstants.AUTO_BACKUP, true)) {

				long lastBackup = getPreferences().getLong(PreferenceConstants.LAST_AUTO_BACKUP, 0);
				long now = System.currentTimeMillis();

				if (lastBackup == 0 || now - lastBackup >= 604800000) { // One
																		// week

					try {
						String exportCSV = ExternalFileUtil.getInstance(getActivity().getApplicationContext()).exportCSV(true);
						Toast.makeText(getActivity().getApplicationContext(), exportCSV, 2000).show();
					} catch (ExternalFileException e) {
						Toast.makeText(getActivity().getApplicationContext(), getString(R.string.exportError), 3000).show();
					}

					SharedPreferences.Editor editor = getPreferences().edit();
					editor.putLong(PreferenceConstants.LAST_AUTO_BACKUP, now);

					// Commit the edits!
					editor.commit();
				}
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferences().registerOnSharedPreferenceChangeListener(sharedPrefsListener);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferences().unregisterOnSharedPreferenceChangeListener(sharedPrefsListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.input_fragment, container, false);
		initControls();
		return v;
	}

	private void initControls() {
		weightEditText = (EditText) v.findViewById(R.id.Main_WeightEditText);
		sizeEditText = (EditText) v.findViewById(R.id.Main_SizeEditText);
		sizeLabel = (TextView) v.findViewById(R.id.Main_SizeLabel);
		sizeSpacer = (TextView) v.findViewById(R.id.Main_SizeSpacer);
		datePicker = (DatePicker) v.findViewById(R.id.Main_DatePicker);
		weightLogsCount = (TextView) v.findViewById(R.id.Main_WeightLogs);
		lastWeightLog = (TextView) v.findViewById(R.id.Main_LastWeightLog);
		showSize();
		submitWeightButton = (Button) v.findViewById(R.id.Main_WeightSubmitButton);
		submitWeightButton.setOnClickListener(weightSubmitListener);
		setWeightStats();

	}

	private void showSize() {
		if (getPreferences().getBoolean(PreferenceConstants.SHOW_SIZE, false)) {
			sizeLabel.setVisibility(View.VISIBLE);
			sizeSpacer.setVisibility(View.VISIBLE);
			sizeEditText.setVisibility(View.VISIBLE);
		} else {
			sizeLabel.setVisibility(View.GONE);
			sizeSpacer.setVisibility(View.GONE);
			sizeEditText.setVisibility(View.GONE);
		}
	}

	private void setWeightStats() {
		List<WeightDTO> allWeightData = WeightDAO.getInstance(getActivity().getApplicationContext()).getAllWeightDataForCurrentProfile();
		weightLogsCount.setText("" + allWeightData.size());
		if (allWeightData.size() > 0) {
			WeightDTO weightDTO = allWeightData.get(allWeightData.size() - 1);
			lastWeightLog.setText(weightDTO.getWeight() + " kg");
		} else {
			lastWeightLog.setText(getString(R.string.noweightlogs));
		}
	}

	private OnClickListener weightSubmitListener = new OnClickListener() {

		public void onClick(View v) {
			Log.d("WeightDroid", "Submitting weight");
			try {
				weight = Float.parseFloat(weightEditText.getText().toString());
			} catch (NumberFormatException e) {
				showAlertDialog(getString(R.string.error), getString(R.string.fillWeight));
				return;
			}
			if (sizeEditText != null) {
				try {
					size = Float.parseFloat(sizeEditText.getText().toString());
				} catch (NumberFormatException e) {
					size = -1;
				}
			} else {
				size = -1;
			}
			WeightDAO weightDAO = WeightDAO.getInstance(getActivity().getApplicationContext());
			String user = getPreferences().getString("user", null);
			if (user != null) {

				GregorianCalendar selectedCalendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

				weightDAO.insertWeightData(new WeightDTO(weight, size, selectedCalendar.getTime()));
				weightEditText.setText("");
				if (sizeEditText != null) {
					sizeEditText.setText("");
				}
				setWeightStats();
				showBMIDialog(weight);
			} else {
				showAlertDialog(getString(R.string.fillProfile), getString(R.string.fillProfileDetails));
			}

		}
	};

	void showBMIDialog(float weight) {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction. We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = BMIDialog.newInstance(weight);
		newFragment.show(ft, "dialog");
	}

	void showAlertDialog(String title, String message) {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("alert");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment newFragment = AlertDialog.newInstance(title, message);
		newFragment.show(ft, "alert");
	}
}