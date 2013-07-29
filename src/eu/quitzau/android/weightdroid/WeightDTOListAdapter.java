package eu.quitzau.android.weightdroid;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import eu.quitzau.android.weightdroid.dto.WeightDTO;

public class WeightDTOListAdapter extends ArrayAdapter<WeightDTO> {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM-yyyy");

	private List<WeightDTO> weights;

	public WeightDTOListAdapter(Context context, int textViewResourceId, List<WeightDTO> weights) {
		super(context, textViewResourceId, weights);
		this.weights = weights;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listweights, null);
		}

		WeightDTO weight = weights.get(position);
		if (weight != null) {
			TextView weightTextView = (TextView) v.findViewById(R.id.list_weight);
			TextView dateTextView = (TextView) v.findViewById(R.id.list_date);
			TextView diffTextView = (TextView) v.findViewById(R.id.list_diff);

			if (weightTextView != null) {
				weightTextView.setText("" + weight.getWeight());
			}

			if (dateTextView != null) {
				dateTextView.setText(simpleDateFormat.format(weight.getDate()));
			}

			String diffText = "";
			if (position > 0) {
				WeightDTO previousWeight = weights.get(position - 1);
				float diff = weight.getWeight() - previousWeight.getWeight();
				if (diff < 0) {
					diffTextView.setTextColor(Color.GREEN);
				} else if (diff > 0) {
					diffTextView.setTextColor(Color.RED);
					diffText = "+";
				} else {
					diffTextView.setTextColor(Color.GRAY);
				}

				diffText += diff;
			}

			if (diffTextView != null) {
				diffTextView.setText(diffText);
			}
		}
		return v;
	}
}
