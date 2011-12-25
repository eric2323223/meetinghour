package eric.android.meetinghour.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import eric.android.meetinghour.R;
import eric.android.meetinghour.model.Hour;
import eric.android.meetinghour.model.HourData;

public class CityHourListAdapter extends BaseAdapter {
	private Context context;
	private HourData data;

	public CityHourListAdapter(Context mylistActivity, HourData generateData) {
		context = mylistActivity;
		data = generateData;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ArrayList<Integer> texts = (ArrayList<Integer>) data.get(position);
		for(Integer text:texts){
			
			TextView hourText;
			Hour h = new Hour(text.intValue());
//			hourText = new TextView(context);
			
			if(h.isDayLight()){
				hourText = (TextView)((Activity)context).getLayoutInflater().inflate(R.layout.hour_text_daylight, null);
			}else if(h.isNight()){
				hourText = (TextView)((Activity)context).getLayoutInflater().inflate(R.layout.hour_text_night, null);
			}else{
				hourText = (TextView)((Activity)context).getLayoutInflater().inflate(R.layout.hour_text_dawn, null);
			}
			hourText.setText(h.toString());
//			hourText.setText(hour);//error!!!!
			ll.addView(hourText);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
			hourText.setLayoutParams(lp);
			ll.invalidate();
		}
		return (View)ll;
	}

}
