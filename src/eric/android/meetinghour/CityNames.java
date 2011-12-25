package eric.android.meetinghour;

import java.util.ArrayList;
import java.util.List;

import eric.android.meetinghour.model.City;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CityNames extends LinearLayout {
	List<City> cityNames = new ArrayList<City>();
	View parent;
	Context context;
	int currentColumnWidth = 0;

	public CityNames(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	public void setParent(View parent){
		this.parent = parent;
	}
	
	public View getCityView(int index){
		return this.getChildAt(index);
	}

	public void addCity(City city) {
		cityNames.add(city);
		CityNameText cityname = new CityNameText(getContext(), city);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT, 1.0f);
		cityname.setLayoutParams(lp);
		addView(cityname);
		invalidate();
	}
	
	public void removeCity(int index){
		cityNames.remove(index);
		removeViewAt(index);
		invalidate();
	}
	
	public City getCity(int index){
		return cityNames.get(index);
	}
	
	public List<City> getAll(){
		return cityNames;
	}

	public int getCurrentColumnWidth() {
		return this.currentColumnWidth;
	}


}
