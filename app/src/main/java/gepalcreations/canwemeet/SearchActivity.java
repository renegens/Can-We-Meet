package gepalcreations.canwemeet;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class SearchActivity extends Activity {

	DatabaseTable db = new DatabaseTable(this);
	private TextView mTextView;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);
		mTextView = (TextView) findViewById(R.id.text);
		mListView = (ListView) findViewById(R.id.list);
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Cursor c = db.getWordMatches(query, null);
			//process Cursor and display results
			showResults(query);
			Log.i ("Query Value", String.valueOf(query));
		}

	}

	/**
	 * Searches the dictionary and displays results for the given query.
	 * @param query The search query
	 */
	private void showResults(String query) {

		Cursor cursor = getContentResolver().query(DictionaryProvider.CONTENT_URI, null, null,
				new String[]{query}, null);

		if (cursor == null || cursor.getCount() > 0) {
			// There are no results
			mTextView.setText(getString(R.string.no_results, new Object[] {query}));
		} else {
			// Display the number of results
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(R.plurals.search_results,
					count, new Object[] {count, query});
			mTextView.setText(countString);

			// Specify the columns we want to display in the result
			String[] from = new String[] { DatabaseTable.COL_CITY,
					 };

			// Specify the corresponding layout elements where we want the columns to go
			int[] to = new int[] { R.id.word,
					};

			// Create a simple cursor adapter for the definitions and apply them to the ListView
			SimpleCursorAdapter words = new SimpleCursorAdapter(this,
					R.layout.result, cursor, from, to, 1);
			mListView.setAdapter(words);

			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// Build the Intent used to open WordActivity with a specific word Uri
					Intent wordIntent = new Intent(getApplicationContext(), MainActivity.class);
					Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI,
							String.valueOf(id));
					wordIntent.setData(data);
					startActivity(wordIntent);
				}
			});
		}
	}



}
