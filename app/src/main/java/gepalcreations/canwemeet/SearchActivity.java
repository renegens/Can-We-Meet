package gepalcreations.canwemeet;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class SearchActivity extends Activity {

	RecyclerView mRecyclerView;
	private TimeZoneAdapter mTimeZoneAdapter;
	private LayoutInflater inflater;
	private LinearLayout rowLinearLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			getDoMySearch(query);
			Log.i ("Query Value", String.valueOf(query));
		}

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.activity_search_list, container, false);
		mRecyclerView =(RecyclerView) (layout.findViewById(R.id.recycle_list));
		mTimeZoneAdapter = new TimeZoneAdapter(getApplicationContext(),getData());
		mRecyclerView.setAdapter(mTimeZoneAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


		return layout;
	}

	public static List<JodaZones> getData(){

		List<JodaZones> jodaZones = new ArrayList<>();
		String [] city = {"Berlin, Athens, New York, London, Tokyo"};
		for (int i = 0; i<city.length; i++){
			JodaZones current = new JodaZones();
			current.city = city [i];
			jodaZones.add(current);
		}

		return jodaZones;
	}

	private void getDoMySearch(String query) {
		Set <String> zoneIds = DateTimeZone.getAvailableIDs();
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("ZZ");


		List <String> zoneIDtoList = new ArrayList<String>(zoneIds);
		Collections.sort(zoneIDtoList);

		for(String zoneId:zoneIDtoList) {
			Log.i ("Zones", zoneId);
		}
		//	System.out.println("("+dateTimeFormatter.withZone(DateTimeZone.forID(zoneId)).print(0) +") "+zoneId+", "+DateTimeZone.forID(zoneId).getName(DateTimeUtils.currentTimeMillis()));
		//}
	}

}
