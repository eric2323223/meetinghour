package eric.android.meetinghour.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import eric.android.meetinghour.R;
import eric.android.meetinghour.model.City;

public class CityActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	public static final String YAHOO_MAP_API_KEY = "dj0yJmk9ME43djRhb2FkYW9HJmQ9WVdrOWJscHFTbE5JTldrbWNHbzlNekF4TmpJMk16WXkmcz1jb25zdW1lcnNlY3JldCZ4PWI3";
	public static final String CITY_GEOINFO_WS_URL = "http://where.yahooapis.com/geocode?appid=" + YAHOO_MAP_API_KEY;

	public static final String GEOINFO_TIMEZONE_WS_URL = "http://www.earthtools.org/timezone";

	SharedPreferences pref;
	ProgressDialog progressDialog;
	Handler mhandler;
	City rcity;
	Button button;
	ListView cityList;
	AutoCompleteTextView cityTextView;
//	TextView cityTextView;
	SimpleAdapter sa;
	private List<Map<String, Object>> resultList = new ArrayList();
	ArrayAdapter<String> cityNameAdatper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		pref = this.getPreferences(MODE_PRIVATE);
		cityTextView = (AutoCompleteTextView) findViewById(R.id.city_search);
		cityNameAdatper = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cityNames());
		cityTextView.setAdapter(cityNameAdatper);
//		cityTextView = (TextView) findViewById(R.id.city_search);
		mhandler = new Handler();
		button = (Button) findViewById(R.id.search_button);
		button.setOnClickListener(this);
		cityList = (ListView) findViewById(R.id.cities_found);
		sa = new SimpleAdapter(this, resultList, R.layout.search_result_item,
				new String[] { "name", "offset", "country" }, new int[] {
						R.id.item_name, R.id.item_offset, R.id.item_country });
		cityList.setAdapter(sa);
		cityList.setOnItemClickListener(this);
		loadRecentCities();
	}
	
	private void loadRecentCities(){
		Iterator<String> it = pref.getAll().keySet().iterator();
		while(it.hasNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			String cityName = it.next();
			City city = new City(pref.getString(cityName, ""));
			map.put("name", city.getName());
			map.put("offset", city.getOffset());
			map.put("country", city.getCountry());
			resultList.add(map);
		}
		sa.notifyDataSetChanged();
		
	}

	private String[] cityNames() {
		return (String[])pref.getAll().keySet().toArray(new String[]{});
	}

	public void onClick(View v) {
		if(isConnected()){
			if (cityTextView.getText().toString().trim().length() == 0) {
				Toast.makeText(this, "Please fill in a valid city name",
						Toast.LENGTH_SHORT).show();
			} else {
				progressDialog = ProgressDialog.show(this, "", "Searching city...");
				new QueryThread().start();
			}
		}else{
			Toast.makeText(this, "Sorry, you are not connected :(", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		return super.onCreateDialog(id, args);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Map map = resultList.get(arg2);
		// selection = cities.get(arg2);

		Intent intent = new Intent();
		intent.putExtra("name", map.get("name").toString());
		intent.putExtra("offset", new Integer(map.get("offset").toString())
				.intValue());
		intent.putExtra("country", map.get("country").toString());
		pref = this.getPreferences(arg2);
		setResult(Activity.RESULT_OK, intent);
		this.finish();
	}
	
	private void saveCity(City city) {
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(city.getName(), city.toString());
		prefEditor.commit();
	}

	private boolean isConnected() {
	    try {
	        ConnectivityManager nInfo = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	        nInfo.getActiveNetworkInfo().isConnectedOrConnecting();
	        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getActiveNetworkInfo();
	        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	            return true;
	        } else {
	            return false;
	        }

	    } catch (Exception e) {
	        return false;
	    }
	}

	
	private Runnable updateUI = new Runnable() {
		public void run() {
			progressDialog.dismiss();
			saveCity(rcity);
			progressDialog.dismiss();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("name", rcity.getName());
			map2.put("offset", rcity.getOffset());
			map2.put("country", rcity.getCountry());
			resultList.add(map2);
			sa.notifyDataSetChanged();
		}
	};
	
	private Runnable showError = new Runnable(){
		public void run(){
			progressDialog.dismiss();
			Toast.makeText(CityActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
		}
	};

	class QueryThread extends Thread{
		public void run() {
			try {
				URL cityUrl = new URL(CITY_GEOINFO_WS_URL + "&q="
						+ cityTextView.getText());
				Log.v("url", CITY_GEOINFO_WS_URL + "&q="+ cityTextView.getText());
				SAXParserFactory spf1 = SAXParserFactory.newInstance();
				SAXParser sp1 = spf1.newSAXParser();
				XMLReader xr1 = sp1.getXMLReader();
				GeoInfoHandler dataHandler = new GeoInfoHandler();
				xr1.setContentHandler(dataHandler);
				xr1.parse(new InputSource(cityUrl.openStream()));
				City data = dataHandler.getData();
				if (data == null) {
					mhandler.post(showError);
				}else{
					URL timeZoneUrl = new URL(GEOINFO_TIMEZONE_WS_URL + "//"
							+ data.getLatitude() + "//" + data.getLongitude());
					SAXParserFactory spf2 = SAXParserFactory.newInstance();
					SAXParser sp2 = spf2.newSAXParser();
					XMLReader xr2 = sp2.getXMLReader();
					TimeZoneHandler timeZoneHandler = new TimeZoneHandler();
					xr2.setContentHandler(timeZoneHandler);
					xr2.parse(new InputSource(timeZoneUrl.openStream()));
					City city = timeZoneHandler.getData();
					city.setName(data.getName());
					city.setCountry(data.getCountry());
					rcity = city;
					mhandler.post(updateUI);
				}
				
			} catch (Exception e) {
				String st = e.getMessage() + "\n";
				for (StackTraceElement item : e.getStackTrace()) {
					st = st + item + "\n";
				}
				Log.v("Exception", st);
				mhandler.post(showError);
				progressDialog.dismiss();
			}
		}
	}
}

