package gepalcreations.canwemeet;


import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.ViewGroup;
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

    private enum dayPart {
        DAY, NOON, AFTERNOON, NIGHT
    }

    private enum textViewColors {
        PURPLE, TEAL, AMBER, BROWN
    }


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

        setPaddingToTextViews(currentHours);

        Set<String> zoneIds = DateTimeZone.getAvailableIDs();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("ZZ");

        loadImagesFromXML(timeZoneDifference, currentHours);

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
//                line.setVisibility(View.GONE);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                RelativeLayout root = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
                root.setDrawingCacheEnabled(true);

                Bitmap bmp = getBitmapFromView(this.getWindow().getDecorView().findViewById(R.id.main_relative).getRootView());

                URI uri = storePrintFile(bmp);

                Toast.makeText(getApplicationContext(), "Image Saved at " + uri.getPath(), Toast.LENGTH_SHORT).show();
                //               line.setVisibility(View.VISIBLE);
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

    public void loadImagesFromXML(int timeZone, int ct) {
        // checking and correcting for negative value
        if (timeZone < 0) {
            timeZone = timeZone * (-1);
        }

        LinearLayout.LayoutParams firstMorningRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams firstNoonRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams firstAfternoonRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams firstNightRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);

        LinearLayout.LayoutParams secondMorningRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams secondNoonRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams secondAfternoonRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
        LinearLayout.LayoutParams secondNightRelativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);


        RelativeLayout.LayoutParams firstMorningLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams firstNoonLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams firstAfternoonLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams firstNightLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams secondMorningLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams secondNoonLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams secondAfternoonLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams secondNightLinearParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams sleepParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams coffeeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams workParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams homeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        RelativeLayout firstMorningRelative = new RelativeLayout(this);
        RelativeLayout firstNoonRelative = new RelativeLayout(this);
        RelativeLayout firstAfternoonRelative = new RelativeLayout(this);
        RelativeLayout firstNightRelative = new RelativeLayout(this);

        RelativeLayout secondMorningRelative = new RelativeLayout(this);
        RelativeLayout secondNoonRelative = new RelativeLayout(this);
        RelativeLayout secondAfternoonRelative = new RelativeLayout(this);
        RelativeLayout secondNightRelative = new RelativeLayout(this);

        LinearLayout firstMorningLinear = new LinearLayout(this);
        LinearLayout firstNoonLinear = new LinearLayout(this);
        LinearLayout firstAfternoonLinear = new LinearLayout(this);
        LinearLayout firstNightLinear = new LinearLayout(this);

        LinearLayout secondMorningLinear = new LinearLayout(this);
        LinearLayout secondNoonLinear = new LinearLayout(this);
        LinearLayout secondAfternoonLinear = new LinearLayout(this);
        LinearLayout secondNightLinear = new LinearLayout(this);

        firstMorningLinear.setOrientation(LinearLayout.VERTICAL);
        secondMorningLinear.setOrientation(LinearLayout.VERTICAL);
        firstNoonLinear.setOrientation(LinearLayout.VERTICAL);
        secondNoonLinear.setOrientation(LinearLayout.VERTICAL);
        firstAfternoonLinear.setOrientation(LinearLayout.VERTICAL);
        secondAfternoonLinear.setOrientation(LinearLayout.VERTICAL);
        firstNightLinear.setOrientation(LinearLayout.VERTICAL);
        secondNightLinear.setOrientation(LinearLayout.VERTICAL);

        ImageView sleep = new ImageView(this);
        ImageView coffee = new ImageView(this);
        ImageView work = new ImageView(this);
        ImageView home = new ImageView(this);

        Drawable sleepImage = getResources().getDrawable(R.drawable.ic_sleep, getTheme());
        Drawable coffeeImage = getResources().getDrawable(R.drawable.ic_coffee, getTheme());
        Drawable workImage = getResources().getDrawable(R.drawable.ic_work, getTheme());
        Drawable homeImage = getResources().getDrawable(R.drawable.ic_home, getTheme());

        sleepParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        sleepParams.addRule(RelativeLayout.CENTER_VERTICAL);

        coffeeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        coffeeParams.addRule(RelativeLayout.CENTER_VERTICAL);

        workParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        workParams.addRule(RelativeLayout.CENTER_VERTICAL);

        homeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        homeParams.addRule(RelativeLayout.CENTER_VERTICAL);


        sleep.setImageDrawable(sleepImage);
        coffee.setImageDrawable(coffeeImage);
        work.setImageDrawable(workImage);
        home.setImageDrawable(homeImage);

        dayPart dp;
        textViewColors tc;

        List<textViewColors> colors = new ArrayList<>();
        int[] indexes = new int[25];
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getScreenWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        LinearLayout wholeLinear = new LinearLayout(this);
        wholeLinear.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams wholeLinearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 4.0f);
        wholeLinear.setLayoutParams(wholeLinearParams);
        // Isws prepei na mpei sto telos
        timeLinearLayout.addView(wholeLinear);

        TextView currentTime = (TextView) findViewById(R.id.current_city);
        currentTime.setPadding(0, measures.getPadding() + dpToPx(1), 0, measures.getPadding());

        for (int i = 0; i < 25; i++) {
            indexes[i] = ((i + 1) + timeZone) % 25;
            Log.i("Time", String.valueOf(indexes[i]));
        }

        for (int i = 0; i < 25; i++) {
            if (indexes[i] != 0) {

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.times_layout, timeLinearLayout, false);

                holder = new ViewHolder();

                holder.hour = (TextView) v.findViewById(R.id.hours);

                holder.hour.setText(String.valueOf(indexes[i]));
                if(indexes[i] == (ct + timeZone)){
                    holder.hour.setTypeface(null, Typeface.BOLD_ITALIC);
                }
                holder.hour.measure(widthMeasureSpec, heightMeasureSpec);

                if (indexes[i] >= 1 && indexes[i] <= 7) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_purple_500));
                    dp = dayPart.NIGHT;
                    tc = textViewColors.PURPLE;
                } else if (indexes[i] >= 8 && indexes[i] <= 10) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_teal_500));
                    dp = dayPart.DAY;
                    tc = textViewColors.TEAL;
                } else if (indexes[i] >= 11 && indexes[i] <= 17) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_amber_500));
                    dp = dayPart.NOON;
                    tc = textViewColors.AMBER;
                } else {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_brown_500));
                    dp = dayPart.AFTERNOON;
                    tc = textViewColors.BROWN;
                }

                holder.hour.setPadding(dpToPx(10), measures.getPadding(), 0, measures.getPadding());

                colors.add(tc);

                switch (dp) {
                    case DAY:
                        firstMorningLinear.addView(v);
                        break;
                    case NOON:
                        firstNoonLinear.addView(v);
                        break;
                    case AFTERNOON:
                        firstAfternoonLinear.addView(v);
                        break;
                    case NIGHT:
                        firstNightLinear.addView(v);
                        break;
                }
            }
        }


        for (int i = 0; i < firstMorningLinear.getChildCount(); i++) {

            if (i > 0) {
                View curChild = ((ViewGroup) firstMorningLinear).getChildAt(i);
                View previousChild = ((ViewGroup) firstMorningLinear).getChildAt(i - 1);

                TextView curChildTextView = (TextView) ((ViewGroup) curChild).getChildAt(0);
                TextView previousChildTextView = (TextView) ((ViewGroup) previousChild).getChildAt(0);

                if (Integer.parseInt(curChildTextView.getText().toString()) < Integer.parseInt(previousChildTextView.getText().toString())) {
                    firstMorningLinear.removeView(curChild);
                    secondMorningLinear.addView(curChild);
                    i = 0;
                }
            }
        }


        for (int i = 0; i < firstNoonLinear.getChildCount(); i++) {

            if (i > 0) {
                View curChild = ((ViewGroup) firstNoonLinear).getChildAt(i);
                View previousChild = ((ViewGroup) firstNoonLinear).getChildAt(i - 1);

                TextView curChildTextView = (TextView) ((ViewGroup) curChild).getChildAt(0);
                TextView previousChildTextView = (TextView) ((ViewGroup) previousChild).getChildAt(0);

                if (Integer.parseInt(curChildTextView.getText().toString()) < Integer.parseInt(previousChildTextView.getText().toString())) {
                    firstNoonLinear.removeView(curChild);
                    secondNoonLinear.addView(curChild);
                    i = 0;
                }
            }
        }

        for (int i = 0; i < firstAfternoonLinear.getChildCount(); i++) {

            if (i > 0) {
                View curChild = ((ViewGroup) firstAfternoonLinear).getChildAt(i);
                View previousChild = ((ViewGroup) firstAfternoonLinear).getChildAt(i - 1);

                TextView curChildTextView = (TextView) ((ViewGroup) curChild).getChildAt(0);
                TextView previousChildTextView = (TextView) ((ViewGroup) previousChild).getChildAt(0);

                if (Integer.parseInt(curChildTextView.getText().toString()) < Integer.parseInt(previousChildTextView.getText().toString())) {
                    firstAfternoonLinear.removeView(curChild);
                    secondAfternoonLinear.addView(curChild);
                    i = 0;
                }
            }
        }

        for (int i = 0; i < firstNightLinear.getChildCount(); i++) {

            if (i > 0) {
                View curChild = ((ViewGroup) firstNightLinear).getChildAt(i);
                View previousChild = ((ViewGroup) firstNightLinear).getChildAt(i - 1);

                TextView curChildTextView = (TextView) ((ViewGroup) curChild).getChildAt(0);
                TextView previousChildTextView = (TextView) ((ViewGroup) previousChild).getChildAt(0);

                if (Integer.parseInt(curChildTextView.getText().toString()) < Integer.parseInt(previousChildTextView.getText().toString())) {
                    firstNightLinear.removeView(curChild);
                    secondNightLinear.addView(curChild);
                    i = 0;
                }
            }
        }

        for (int j = 0; j < colors.size(); j++) {
            if (j != colors.size() - 1) {
                if (colors.get(j) == colors.get(j + 1)) {
                    colors.remove(j);
                    j = -1;
                }
            }
        }

        boolean firstNightStored = false, firstMorningStored = false, firstNoonStored = false, firstAfterNoonStored = false;

        for (textViewColors c : colors) {
            Log.i("color", String.valueOf(c));

            if (c == textViewColors.PURPLE && !firstNightStored) {
                firstNightRelative.addView(firstNightLinear, firstNightLinearParams);
                wholeLinear.addView(firstNightRelative, firstNightRelativeParams);
                firstNightStored = true;
            } else if (c == textViewColors.PURPLE && firstNightStored && secondNightLinear.getChildCount() > 0) {
                secondNightRelative.addView(secondNightLinear, secondNightLinearParams);
                wholeLinear.addView(secondNightRelative, secondNightRelativeParams);
            }

            if (c == textViewColors.TEAL && !firstMorningStored) {
                firstMorningRelative.addView(firstMorningLinear, firstMorningLinearParams);
                wholeLinear.addView(firstMorningRelative, firstMorningRelativeParams);
                firstMorningStored = true;
            } else if (c == textViewColors.TEAL && firstMorningStored && secondMorningLinear.getChildCount() > 0) {
                secondMorningRelative.addView(secondMorningLinear, secondMorningLinearParams);
                wholeLinear.addView(secondMorningRelative, secondMorningRelativeParams);
            }

            if (c == textViewColors.AMBER && !firstNoonStored) {
                firstNoonRelative.addView(firstNoonLinear, firstNoonLinearParams);
                wholeLinear.addView(firstNoonRelative, firstNoonRelativeParams);
                firstNoonStored = true;
            } else if (c == textViewColors.AMBER && firstNoonStored && secondNoonLinear.getChildCount() > 0) {
                secondNoonRelative.addView(secondNoonLinear, secondNoonLinearParams);
                wholeLinear.addView(secondNoonRelative, secondNoonRelativeParams);
            }

            if (c == textViewColors.BROWN && !firstAfterNoonStored) {
                firstAfternoonRelative.addView(firstAfternoonLinear, firstAfternoonLinearParams);
                wholeLinear.addView(firstAfternoonRelative, firstAfternoonRelativeParams);
                firstAfterNoonStored = true;
            } else if (c == textViewColors.BROWN && firstAfterNoonStored && secondAfternoonLinear.getChildCount() > 0) {
                secondAfternoonRelative.addView(secondAfternoonLinear, secondAfternoonLinearParams);
                wholeLinear.addView(secondAfternoonRelative, secondAfternoonRelativeParams);
            }

        }

        if (firstNightLinear.getChildCount() > secondNightLinear.getChildCount())
            firstNightRelative.addView(sleep, sleepParams);
        else
            secondNightRelative.addView(sleep, sleepParams);

        if (firstMorningLinear.getChildCount() > secondMorningLinear.getChildCount())
            firstMorningRelative.addView(coffee, coffeeParams);
        else
            secondMorningRelative.addView(coffee, coffeeParams);

        if (firstNoonLinear.getChildCount() > secondNoonLinear.getChildCount())
            firstNoonRelative.addView(work, workParams);
        else
            secondNoonRelative.addView(work, workParams);

        if (firstAfternoonLinear.getChildCount() > secondAfternoonLinear.getChildCount())
            firstAfternoonRelative.addView(home, homeParams);
        else
            secondAfternoonRelative.addView(home, homeParams);

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

    public void setPaddingToTextViews(int currentHours) {
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

            if(t.getText().toString().equals(String.valueOf(currentHours)))
                t.setTypeface(null, Typeface.BOLD_ITALIC);

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
