package eu.quitzau.android.weightdroid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;
import eu.quitzau.android.weightdroid.utilities.GraphWebHandler;

public class GraphFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private GraphWebHandler myhandler;
	private WebView webview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.graph_webview, container, false);
		webview = (WebView) v.findViewById(R.id.GraphBrowser);
		final WeightDAO weightDAO = WeightDAO.getInstance(getActivity().getApplicationContext());

		RadioButton allButton = (RadioButton) v.findViewById(R.id.GraphAllButton);
		allButton.setChecked(true);
		allButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (buttonView.isChecked()) {
					myhandler.setWeights(weightDAO.getAllWeightDataForCurrentProfile());
					webview.reload();
				}
			}
		});

		RadioButton lastMonthButton = (RadioButton) v.findViewById(R.id.GraphLastMonthButton);
		lastMonthButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (buttonView.isChecked()) {
					Calendar lastMonthCalendar = new GregorianCalendar();
					lastMonthCalendar.setTime(new Date());
					lastMonthCalendar.add(Calendar.MONTH, -1);
					myhandler.setWeights(weightDAO.getWeightDataForCurrentProfile(lastMonthCalendar.getTime()));
					webview.reload();
				}
			}
		});

		myhandler = new GraphWebHandler(getActivity().getApplicationContext(), webview);
		myhandler.setWeights(weightDAO.getAllWeightDataForCurrentProfile());
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		webview.requestFocusFromTouch();
		webview.setWebViewClient(new WebViewClient());
		webview.setWebChromeClient(new WebChromeClient());
		webview.addJavascriptInterface(myhandler, "Android");
		webview.loadUrl("file:///android_asset/html/dynamic.html");
		return v;
	}

	private String getGraphTitle(int resId) {
		return getString(R.string.graphTitle) + " - " + getString(resId);
	}

}
