package gepalcreations.canwemeet;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Set;


public class SearchActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_bar);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			getDoMySearch(query);
		}
	}

	private void getDoMySearch(String query){
		Set<String> zoneIds = DateTimeZone.getAvailableIDs();
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("ZZ");

		for(String zoneId:zoneIds) {
			System.out.println("("+dateTimeFormatter.withZone(DateTimeZone.forID(zoneId)).print(0) +") "+zoneId+", "+DateTimeZone.forID(zoneId).getName(DateTimeUtils.currentTimeMillis()));
		}
	}
}
