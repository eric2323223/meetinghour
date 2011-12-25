package eric.android.meetinghour;

import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import eric.android.meetinghour.adapter.CityHourListAdapter;
import eric.android.meetinghour.model.HourData;

public class CityHourList extends ListView {
	private CityHourListAdapter ma;
	private HourData data;

	public CityHourList(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		data = new HourData();
		ma = new CityHourListAdapter(context, data);
		this.setAdapter(ma);
	}
	
	public void addTime(int time){
		data.addTime(time);
		ma.notifyDataSetChanged();
	}
	
	public void removeTime(int index){
		data.removeTime(index);
		ma.notifyDataSetChanged();
	}

	public List<Integer> getData(int index) {
		return data.get(index);
	}

}
