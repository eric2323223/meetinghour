package eric.android.meetinghour;

import android.app.Application;
import eric.android.meetinghour.model.City;

public class MeetingHourApp extends Application {
	private City homeCity;

	public City getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(City homeCity) {
		this.homeCity = homeCity;
	}
	
}
