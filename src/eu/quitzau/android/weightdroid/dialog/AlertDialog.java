package eu.quitzau.android.weightdroid.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import eu.quitzau.android.weightdroid.R;

public class AlertDialog extends DialogFragment {

	public static AlertDialog newInstance(String title, String message) {
		AlertDialog dialog = new AlertDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.alert, container, false);
		getDialog().setTitle(getArguments().getString("title"));
		TextView messageTV = (TextView) v.findViewById(R.id.Alert_Message);
		messageTV.setText(getArguments().getString("message"));
		Button closeButton = (Button) v.findViewById(R.id.Alert_Closebutton);
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return v;
	}

}
