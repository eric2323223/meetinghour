package eric.android.meetinghour.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class City {
	int offset;
	String name;
	String country;
	float latitude;
	float longitude;
	
	public City(String name, int offset, String country, float latitude, float longitude){
		try{
			Log.v("city", name+":"+offset+":"+country+":"+latitude+":"+longitude);
			this.name = name;
			this.offset = offset;
			this.country = country;
			this.latitude = latitude;
			this.longitude = longitude;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public City(String name, int offset, String country){
		try{
			Log.v("city", name+":"+offset+":"+country);
			this.name = name;
			this.offset = offset;
			this.country = country;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public City(String json){
		try{
			JSONObject object = new JSONObject(json);
			this.name = object.getString("name");
			this.offset = object.getInt("offset");
			this.country = object.getString("country");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String toString(){
		JSONObject json = new JSONObject();
		try {
			json.put("name", this.name);
			json.put("offset", this.offset);
			json.put("country", this.country);
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public City() {	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int timeDifference(City city) {
		return this.offset - city.getOffset();
	}
	
	public boolean equals(Object city){
		if(city instanceof City){
			return this.name.equals(((City)city).getName());
		}else{
			return false;
		}
	}
}
