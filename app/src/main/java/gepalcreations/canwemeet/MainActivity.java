package gepalcreations.canwemeet;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private View line;
    Context context = this;
    static int activityHeight = 0;
    String searchedCity = "New York";
    private Measures measures;

    // Pacific (US) -> US/Pacific
    // Pacific New (US) -> US/Pacific-New
    // Pacific (Canada) -> Canada/Pacific
    // "Shiprock, Navajo"
    // Mountain (US) -> US/Mountain
    // Mountain (Canada) -> Canada/Mountain
    // Knox (Indiana) -> America/Indiana/Knox
    // Knox IN -> America/Knox_IN
    // Starke (Indiana) -> US/Indiana-Starke
    // ^--"America/Knox_IN, US/Indiana-Starke"
    // Tell City (Indiana) -> America/Indiana/Tell_City
    // New Salem (North Dakota) -> America/North_Dakota/New_Salem
    // East Saskatchewan -> Canada/East-Saskatchewan
    // ^-- Canada/East-Saskatchewan, Canada/Saskatchewan

    private static final String[] cities = new String[]{
            "Apia", "Midway", "Niue", "Pago Pago", "Samoa", "Adak", "Atka", "Aleutian",
            "Fakaofo", "Honolulu", "Hawaii", "Johnston", "Rarotonga", "Tahiti", "Marquesas",
            "Anchorage", "Alaska", "Juneau", "Nome", "Yakutat", "Gambier", "Dawson", "Los Angeles",
            "Pacific (US)", "Pacific New (US)", "Santa Isabel", "Tijuana", "Ensenada", "BajaNorte", "Vancouver",
            "Pacific (Canada)", "Whitehorse", "Yukon", "Pitcairn", "Boise", "Cambridge Bay", "Chihuahua",
            "Dawson Creek", "Denver", "Shiprock", "Navajo", "Mountain (US)", "Edmonton", "Mountain (Canada)",
            "Hermosillo", "Inuvik", "Mazatlan", "BajaSur", "Ojinaga", "Phoenix", "Arizona", "Yellowknife",
            "Bahia Banderas", "Belize", "Cancun", "Chicago", "Central", "Costa Rica", "El Salvador", "Guatemala",
            "Knox (Indiana)", "Knox IN", "Starke (Indiana)", "Tell City (Indiana)", "Managua", "Matamoros", "Menominee",
            "Merida", "Mexico City", "Monterrey", "New Salem (North Dakota)", "Rainy River", "Rankin Inlet", "Regina",
            "East Saskatchewan", "Saskatchewan"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting time from clock and timezone animation class in minutes
        Clock mClock = new Clock();
        int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();
        int currentTimeZone = mClock.getTimeZone();

		// get current moment in default time zone

		DateTime dt = new DateTime();
		// translate to London local time
		String searchTime = "Europe/Berlin";
		DateTime dtLondon = dt.withZone(DateTimeZone.forID(searchTime));

		java.util.Date date = new java.util.Date(System.currentTimeMillis());
		DateTimeZone dtz = DateTimeZone.getDefault();// Gets the default time zone.
		DateTime dateTime = new DateTime(date.getTime(), dtz);
		int timeZoneDifference = dtLondon.getHourOfDay() - dateTime.getHourOfDay();

		Log.i("Difference", String.valueOf(timeZoneDifference));
		Log.i("Date time from yoda", String.valueOf(dateTime));
		Log.i("Date time zone for id", String.valueOf(dtLondon));

        //Log.e("hours", String.valueOf(currentHours));
        //int height = getScreenHeight() - getDensityPixelToRemove(context);

        //Log.e("Screen Height",String.valueOf(getScreenHeight()));
        //Log.e("Screen Density",String.valueOf(getDensityPixelToRemove(context)));
        //Log.e("Final Height",String.valueOf(height));

        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.current_time_linear);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //Loading Line
        //line = findViewById(R.id.line);

        File screenShotsDir = new File(Environment.getExternalStorageDirectory(), "CanWeMeet");

        if (!screenShotsDir.exists()) {
            screenShotsDir.mkdirs();
        }


        //checking if we are in the same time zone to do other logic.
        int timeZoneCheck = compareSameTimeZone(timeZoneDifference, currentTimeZone);
        if (timeZoneCheck == 1) {
            timeZoneDifference = 0;
        }

        measures = new Measures();

        setPaddingToTextViews();
		Set<String> zoneIds = DateTimeZone.getAvailableIDs();
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("ZZ");

        loadImagesFromXML(timeZoneDifference);

        // float timeCalculation = getTimeCalculation(currentHours, currentMinutes, height);
        //float timeCalculation = getTimeCalculation(currentHours, currentMinutes, measures.getLeftLinearHeight());
        //line.setTranslationY(timeCalculation);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
		ComponentName cn = new ComponentName(this, TimeZoneAdapter.class);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
		//searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

		return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
        switch (item.getItemId()) {
			case R.id.menu_search:
				onSearchRequested();
				return true;
            case R.id.capture:
                line.setVisibility(View.GONE);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
                root.setDrawingCacheEnabled(true);

                Bitmap bmp = getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.main_relative).getRootView());

                URI uri = storePrintFile(bmp);

                Toast.makeText(getApplicationContext(), "Image Saved at " + uri.getPath(), Toast.LENGTH_SHORT).show();
                line.setVisibility(View.VISIBLE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Bitmap getBitmapFromView(View v) {
        v.setLayoutParams(new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public URI storePrintFile(Bitmap bitmapToStore) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        String path = Environment.getExternalStorageDirectory() + File.separator + "CanWeMeet" + File.separator;
        File file = new File(path);
        String current = searchedCity + ".jpg";//uniqueId.replace(" ", "-").replace(":", "-") + ".jpeg";
        File mypath = new File(file, current);
        try {
            FileOutputStream out = new FileOutputStream(mypath);
            bitmapToStore.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mypath.toURI();
    }

    ViewHolder holder;

    public void loadImagesFromXML(int timeZone) {
        //checking and correcting for negative value
        if (timeZone < 0) {
            timeZone = timeZone * (-1);
        }

        for (int i = 0; i < 25; i++) {
            int indexOfFiles = ((i + 1) + timeZone) % 25;
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.times_layout, timeLinearLayout, false);

            holder = new ViewHolder();
            if (indexOfFiles != 0) {

                holder.hour = (TextView) v.findViewById(R.id.hours);

                holder.hour.setText(String.valueOf(indexOfFiles));
                //holder.icon = (ImageView) v.findViewById(R.id.time_icon);


                LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                if (indexOfFiles >= 1 && indexOfFiles <= 7) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_purple_500));
                } else if (indexOfFiles >= 8 && indexOfFiles <= 10)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_teal_500));
                else if (indexOfFiles >= 11 && indexOfFiles <= 17)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_amber_500));
                else holder.hour.setBackgroundColor(getResources().getColor(R.color.md_brown_500));

                holder.hour.setPaddingRelative(dpToPx(10), 0, 0, 0);


                v.setTag(holder);
                timeLinearLayout.addView(v, hourParams);
            }
            Log.i("index", String.valueOf(indexOfFiles));
        }
    }

    private static int getDensityPixelToRemove(Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        //action bar 48dp and status bar 20dp
        if (density >= 4.0) {
            return 384;//48+20+60*3
        }
        if (density >= 3.0) {
            return 244;
        }
        if (density >= 2.0) {
            return 176;
        }
        if (density >= 1.5) {
            return 142;
        }
        return 68;
    }

    public int getScreenWidth() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getScreenHeight() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public int getActionBarHeight() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setPaddingToTextViews() {
        int[] textViewIds = {R.id.hereTextView, R.id.hour_1, R.id.hour_2, R.id.hour_3, R.id.hour_4, R.id.hour_5, R.id.hour_6, R.id.hour_7, R.id.hour_8, R.id.hour_9, R.id.hour_10, R.id.hour_11, R.id.hour_12, R.id.hour_13, R.id.hour_14, R.id.hour_15, R.id.hour_16, R.id.hour_17, R.id.hour_18, R.id.hour_19, R.id.hour_20, R.id.hour_21, R.id.hour_22, R.id.hour_23, R.id.hour_24,};
        List<TextView> textViews = new ArrayList<>();
        int activityHeight = getScreenHeight() - getActionBarHeight() - getStatusBarHeight();
        measures.setActivityHeight(activityHeight);
        int sum = 0, pixelsDiff;
        double padding;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getScreenWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        for (int i : textViewIds) {
            TextView textView = (TextView) findViewById(i);

            textView.measure(widthMeasureSpec, heightMeasureSpec);
            sum += textView.getMeasuredHeight();
            textViews.add(textView);
        }
        Log.i("sum", String.valueOf(sum));
        pixelsDiff = activityHeight - sum;
        padding = ((double) pixelsDiff / 24) / 2;
        Log.i("padding", String.valueOf(padding));
        Log.i("padding_floor", String.valueOf(Math.floor(padding)));
        Log.i("padding_ceil", String.valueOf(Math.ceil(padding)));
        for (TextView t : textViews) {
            // Math.floor -> px 4.5 to kanei 4
            // Math.ceil -> px 4.5 to kanei 5
            t.setPadding(0, (int) Math.ceil(padding), dpToPx(10), (int) Math.ceil(padding));
        }
        measures.setPadding((int) Math.ceil(padding));

        LinearLayout leftLinear = (LinearLayout) findViewById(R.id.left_layout);

        leftLinear.measure(leftLinear.getWidth(), leftLinear.getHeight());

        measures.setLeftLinearHeight(leftLinear.getMeasuredHeight());

        Log.i("Linear Height", String.valueOf(leftLinear.getMeasuredHeight()));
        Log.i("Difference", String.valueOf(Math.abs(activityHeight - leftLinear.getMeasuredHeight())));

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    //Calculate time for line animation
    private float getTimeCalculation(int currentHours, int currentMinutes, float height) {
        // float pixelPerHour = height / 25;
        float pixelPerHour = (height / 24) + (measures.getPadding());

        return currentHours * pixelPerHour + currentMinutes;
    }

    //Method to check if we are in the same time zone as entered by user. If yes then passing the 0 index to the array so
    //it will display the same image.
    private int compareSameTimeZone(int timeZone, int currentTimeZone) {

        int timeZoneIsSame = 0; //initialize to not in same zone
        if (timeZone == currentTimeZone) {
            timeZoneIsSame = 1; //in same time zone
        }
        return timeZoneIsSame;
    }

    private class ViewHolder {
        TextView hour;
        ImageView icon;
    }

    private static class Measures {
        int activityHeight, leftLinearHeight, padding;

        public int getActivityHeight() {
            return activityHeight;
        }

        public int getLeftLinearHeight() {
            return leftLinearHeight;
        }

        public int getPadding() {
            return padding;
        }

        public void setActivityHeight(int activityHeight) {
            this.activityHeight = activityHeight;
        }

        public void setLeftLinearHeight(int leftLinearHeight) {
            this.leftLinearHeight = leftLinearHeight;
        }

        public void setPadding(int padding) {
            this.padding = padding;
        }
    }
}
