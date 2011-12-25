package eric.android.meetinghour;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import eric.android.meetinghour.model.City;

public class CityNameText extends LinearLayout {
	String cityName;
	String timezoneName;
	String countryName;
	int offset;
	Handler handler;
	TextView cityClock;
	Thread timerThread;
	MeetingHourApp app;
	City mCity;

	public CityNameText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CityNameText(Context context, City city){
		super(context);
		app = (MeetingHourApp)context.getApplicationContext();
		mCity = city;
		this.offset = city.getOffset();
		setOrientation(LinearLayout.VERTICAL);
		TextView cityname = (TextView)((Activity)getContext()).getLayoutInflater().inflate(R.layout.city_name_text_template, null);
		cityname.setText(city.getName());
		addView(cityname);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT, 1.0f);
		cityname.setLayoutParams(lp);
		
		cityClock = (TextView)((Activity)getContext()).getLayoutInflater().inflate(R.layout.city_time_text_template, null);
		addView(cityClock);
		
		cityname.setLayoutParams(lp);
		
		TextView countryName = (TextView)((Activity)getContext()).getLayoutInflater().inflate(R.layout.country_name_text_template, null);
		countryName.setText(city.getCountry());
		addView(countryName);
		
		cityname.setLayoutParams(lp);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Date date = new Date();
				if(app.getHomeCity() == null){
					app.setHomeCity(mCity);
				}else{
					date.setHours(date.getHours()- app.getHomeCity().getOffset() + mCity.getOffset());
				}
				String strDateFormat = "HH:mm a";
				SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
				cityClock.setText(sdf.format(date));
			}
			
		};
		startTimer();
	}
	
	private void startTimer() {
		timerThread = new Thread(){
			@Override
			public void run() {
				while(true){
					handler.sendEmptyMessage(0);
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		timerThread.start();
	}

	public void setCityName(String str){
		this.cityName = str;
	}
	
	public void setTimezoneName(String str){
		this.timezoneName = str;
	}
	
	public void setCountryName(String str){
		this.countryName = str;
	}
	

}
