package eric.android.meetinghour.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import net.youmi.android.AdManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import eric.android.meetinghour.CityHourList;
import eric.android.meetinghour.CityNames;
import eric.android.meetinghour.R;
import eric.android.meetinghour.dialog.CitySelectionDialog;
import eric.android.meetinghour.model.City;
import eric.android.meetinghour.model.Hour;
import eric.android.meetinghour.util.MyLocation;
import eric.android.meetinghour.util.MyLocation.LocationResult;

public class MainActivity extends Activity implements OnClickListener,
		OnItemLongClickListener {
	private static final int SHARE_DIALOG = 1;
	private static final int CITY_DIALOG = 0;

	private CityHourList hourList;
	private CityNames cityNames;
	private List<City> cities = new ArrayList<City>();

	MyLocation myLocation = new MyLocation();
	LocationResult locationResult;
//	public City home;
	ProgressDialog homeCityDialog;
	
	static {
		AdManager.init("46e6100c602ba9b2", "554aabc207cb56e8", 30, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		cityNames = (CityNames) findViewById(R.id.city_names);
		hourList = (CityHourList) findViewById(R.id.hour_list);
		hourList.setOnItemLongClickListener(this);
		
		addHomeCity();
	}

	private void addHomeCity() {
		TimeZone tz = TimeZone.getDefault();
		int offset = tz.getRawOffset()/1000/60/60;
		String country = tz.getID();
		addCityToScreen(new City("My Place", offset, country));
		
//		((MeetingHourApp)this.getApplicationContext()).setHomeCity(home);
	}

	private void addCityToScreen(City city) {
		cities.add(city);
		cityNames.addCity(city);
		hourList.addTime(city.getOffset());
	}

	public void removeCityFromScreen(int index) {
		if (cities.size() > index) {
			cities.remove(index);
			cityNames.removeCity(index);
			hourList.removeTime(index);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "+");
		menu.add(0, 0, 1, "-");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("+")) {
			Intent intent = new Intent(this, CityActivity.class);
			startActivityForResult(intent, 0);
		}
		if (item.getTitle().equals("-")) {
			showDialog(CITY_DIALOG);
		}
		return true;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		if (id == CITY_DIALOG) {
			((CitySelectionDialog) dialog).populateData(Arrays.asList(cityNames()));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CITY_DIALOG: {
			return new CitySelectionDialog(this);
		}
		case SHARE_DIALOG: {
			return new AlertDialog.Builder(this).setTitle("Share time via")
					.setItems(R.array.share_targets, this).create();
		}
		}
		return null;
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == 0) {
			String smsBody = "Hello World\nYou Rock!!!";
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", smsBody);
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
		}
		if (which == 2) {
			try {
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("text/html");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Meeting hour");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"schedule");
				startActivity(emailIntent);

			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "You can't share time through Mail",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private String[] cityNames() {
		String[] result = new String[cities.size()];
		String str = "";
		for (int i = 0; i < cities.size(); i++) {
			result[i] = cities.get(i).getName();
			str = str + "," + result[i];
		}
		return result;
	}

	public List<String> allCities() {
		List<String> result = new ArrayList<String>();
		for (City city : cityNames.getAll()) {
			result.add(city.getName());
		}
		return result;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		Log.d("OAR", resultCode + ":" + requestCode);
		// Toast.makeText(this, resultCode+":"+requestCode,
		// Toast.LENGTH_LONG).show();
		if (resultCode == RESULT_OK && requestCode == 0) {
			Bundle extras = data.getExtras();
			int offset = extras.getInt("offset");
			String name = extras.getString("name");
			String country = extras.getString("country");
			City city = new City(name, offset, country);
			addCityToScreen(city);
			// Toast.makeText(this, name, Toast.LENGTH_LONG).show();
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// Toast.makeText(this, "#"+arg2, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, "Meeting Hour");
		i.putExtra(Intent.EXTRA_TEXT, getData(arg2));
		startActivity(Intent.createChooser(i, "Share"));
		return true;
	}

	private String getData(int index) {
		String result = "";
		List<Integer> hours = hourList.getData(index);
		for (int i = 0; i < hours.size(); i++) {
			result = result + cityNames.getCity(i).getName() + "\t"
					+ new Hour(hours.get(i)).toString() + "\n";
		}
		return result;
	}
	
}