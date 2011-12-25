package eric.android.meetinghour.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eric.android.meetinghour.R;
import eric.android.meetinghour.activity.MainActivity;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CitySelectionDialog extends Dialog implements OnItemClickListener, OnClickListener{
	ListView cityList;
	Button button;
	MainActivity context;
	List<Integer> selections = new ArrayList<Integer>();
	
	public CitySelectionDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog);
		this.context = (MainActivity)context;
		setTitle("Delete City");
		button = (Button)findViewById(R.id.dialog_button);
		button.setOnClickListener(this);
		cityList = (ListView)findViewById(R.id.exist_cities);
		populateData((this.context).allCities());
	}
	
	public void populateData(List<String> data){
		ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, data);
		cityList.setAdapter(aa);
		cityList.setOnItemClickListener(this);
		aa.notifyDataSetChanged();
	}

	public void onClick(View v) {
		Collections.sort(selections);
		for(int i=0;i<selections.size();i++)
		{
			Log.d("deleteCity", selections.get(i).toString());
			context.removeCityFromScreen(selections.get(i)-i);
		}
		dismiss();
		selections = new ArrayList<Integer>();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		boolean isChecked = ((CheckedTextView)arg1).isChecked();
		if(!isChecked){
			selections.add(new Integer(arg2));
		}else{
			selections.remove(new Integer(arg2));
		}
	}

}
