package eu.quitzau.android.weightdroid.utilities;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import eu.quitzau.android.weightdroid.PreferenceConstants;
import eu.quitzau.android.weightdroid.R;
import eu.quitzau.android.weightdroid.dto.WeightDTO;

public class GraphWebHandler {

	Context context;
	private WebView mAppView;
	private List<WeightDTO> weights;
	private String title;

	public GraphWebHandler(Context context, WebView appView) {
		this.mAppView = appView;
		this.context = context;
	}
	
	public void setWeights(List<WeightDTO> weights)
	{
		this.weights = weights;
	}

	
	
	public String getTitle() {
		return "<h3>" + title + "</h3>";
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void loadGraph() {
		Log.d("WeightDroid", "loadGraph called");

		mAppView.loadUrl("javascript:GotGraph(" + "[" + getRawDataJSON() + "]" + ")");
	}

	private Object getRawDataJSON() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		float weightGoal = Float.parseFloat(sharedPreferences.getString(PreferenceConstants.USER_WEIGHT_GOAL, "0"));

		StringBuffer weightJson = new StringBuffer();
		StringBuffer goalJson = new StringBuffer();
		weightJson.append("{ lines: { show: true }, points: { show: true }, label: \""+ context.getString(R.string.weightLabel) + "\", data: [");
		goalJson.append("{ label: \"" + context.getString(R.string.weightGoal) + "\", data: [");
		int count = 0;
		for (WeightDTO weightDTO : weights) {
			weightJson.append("[" + count + "," + weightDTO.getWeight() + "]");
			weightJson.append(",");
			goalJson.append("[" + count + "," + weightGoal + "]");
			goalJson.append(",");
			count++;
		}
		weightJson.append("]}");
		goalJson.append("]}");
		String json = weightJson.toString() + "," + goalJson.toString();
		Log.d("WeightDroid", json);
		return json;
	}
}
