package eu.quitzau.android.weightdroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import eu.quitzau.android.weightdroid.dto.WeightDTO;
import eu.quitzau.android.weightdroid.persistence.WeightDAO;

public class WeightWidget extends AppWidgetProvider {

	private Context currentContext;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// called when widgets are deleted
		// see that you get an array of widgetIds which are deleted
		// so handle the delete of multiple widgets in an iteration
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		// runs when all of the instances of the widget are deleted from
		// the home screen
		// here you can do some setup
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		// runs when all of the first instance of the widget are placed
		// on the home screen
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// all the intents get handled by this method
		// mainly used to handle self created intents, which are not
		// handled by any other method

		// the super call delegates the action to the other methods

		// for example the APPWIDGET_UPDATE intent arrives here first
		// and the super call executes the onUpdate in this case
		// so it is even possible to handle the functionality of the
		// other methods here
		// or if you don't call super you can overwrite the standard
		// flow of intent handling
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		currentContext = context;
		// Create an Intent to launch ExampleActivity
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		// Get the layout for the App Widget and attach an on-click listener to
		// the button
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setOnClickPendingIntent(R.id.WidgetParent, pendingIntent);
		// views.setOnClickPendingIntent(R.id.Widget_SaveButton, pendingIntent);
		WeightDAO weightDAO = WeightDAO.getInstance(currentContext);
		List<WeightDTO> allWeightDataForCurrentProfile = weightDAO.getAllWeightDataForCurrentProfile();

		if(allWeightDataForCurrentProfile.size() > 0){
		WeightDTO lastWeightDTO = allWeightDataForCurrentProfile.get(allWeightDataForCurrentProfile.size() - 1);
		views.setTextViewText(R.id.WidgetLastWeight, "" + allWeightDataForCurrentProfile.size());
		
		// views.setTextViewText(R.id.WidgetNumRec,
		// context.getString(R.string.weightLogs)+ " " +
		// allWeightDataForCurrentProfile.size());
		Date date = lastWeightDTO.getDate();
		String dateStr = "";
		if (dateStr != null) {
			dateStr = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
		}
		views.setTextViewText(R.id.WidgetLastDate, dateStr + " - " + lastWeightDTO.getWeight() + " kg");

		} else {
			views.setTextViewText(R.id.WidgetLastWeight, currentContext.getString(R.string.noweightlogs));
			views.setTextViewText(R.id.WidgetLastDate, currentContext.getString(R.string.noweightlogs));

		}
		// Tell the AppWidgetManager to perform an update on the current App
		// Widget
		
		
		appWidgetManager.updateAppWidget(new ComponentName(context, WeightWidget.class), views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
