package gepalcreations.canwemeet;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private View line;
    Context context = this;
    static int activityHeight = 0;
    private Measures measures;


    private enum dayPart {
        DAY, NOON, AFTERNOON, NIGHT
    }

    private enum textViewColors {
        PURPLE, TEAL, AMBER, BROWN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityTable mCityTable = new CityTable();
        String[] cities = mCityTable.tableReturn();


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.ic_search);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);

        actionBar.setCustomView(v);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_dropdown_item_1line, cities);

        ArrayList<String> searchArrayList= new ArrayList<String>(Arrays.asList(cities));

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, searchArrayList);

        final AutoCompleteTextView textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);

                timeCalculator(selection);
                Clock mClock = new Clock();
                int currentHours = mClock.getHours();
                int timeZoneDifference = timeCalculator(selection);

                textView.setText("");

                timeLinearLayout.removeAllViews();

                loadImagesFromXML(timeZoneDifference, currentHours, selection);


            }
        });

        //Getting time from clock and timezone animation class in minutes
        Clock mClock = new Clock();
        //int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();
        int currentTimeZone = mClock.getTimeZone();

        //Kapou to gamisa kai einai sinexei null to selection. Gamiseto gia ayrio
        /*String selection = "";
        if (selection == null)
            selection = "Europe/Berlin";*/
        //gejava.util.Date date = new java.util.Date(System.currentTimeMillis());

        DateTimeZone dtz = DateTimeZone.getDefault();
        String selection = dtz.getID();
        Log.i("Value of selection", String.valueOf(selection));

        int timeZoneDifference = timeCalculator(selection);

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

        //File screenShotsDir = new File(Environment.getExternalStorageDirectory(), "CanWeMeet");

        //if (!screenShotsDir.exists()) {
        //    screenShotsDir.mkdirs();
        //}


        measures = new Measures();

        setPaddingToTextViews(currentHours);

        loadImagesFromXML(timeZoneDifference, currentHours, selection);

        // float timeCalculation = getTimeCalculation(currentHours, currentMinutes, height);
        //float timeCalculation = getTimeCalculation(currentHours, currentMinutes, measures.getLeftLinearHeight());
        //line.setTranslationY(timeCalculation);


    }

    private int timeCalculator(String selection) {
        // get current moment in default time zone
        DateTime dt = new DateTime();
        // translate to local time

        DateTime dtLondon = dt.withZone(DateTimeZone.forID(selection));

        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        DateTimeZone dtz = DateTimeZone.getDefault();// Gets the default time zone.
        DateTime dateTime = new DateTime(date.getTime(), dtz);
        int timeZoneDifference = dtLondon.getHourOfDay() - dateTime.getHourOfDay();

        Log.i("Difference", String.valueOf(timeZoneDifference));
        Log.i("Date time from yoda", String.valueOf(dateTime));
        Log.i("Date time zone for id", String.valueOf(dtLondon));

        return timeZoneDifference;
    }


    ViewHolder holder;


    public void loadImagesFromXML(int timeZone, int ct, String selection) {
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

        LinearLayout.LayoutParams cityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

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

        timeLinearLayout.addView(wholeLinear);

        TextView currentTime = new TextView(this);
        selection = selection.substring(selection.lastIndexOf('/') + 1);
        selection = selection.replaceAll("_", " ");
        currentTime.setText(selection);
        currentTime.setBackgroundColor(getResources().getColor(R.color.md_black_1000));
        currentTime.setGravity(Gravity.CENTER);
        currentTime.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        currentTime.setTextColor(getResources().getColor(R.color.md_white_1000));
        currentTime.setTextSize(20);
        currentTime.setPadding(0, measures.getPadding(), 0, measures.getPadding() + +dpToPx(1));
        currentTime.requestFocus();

        wholeLinear.addView(currentTime, cityParams);

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
                if (indexes[i] == (ct + timeZone)) {
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

            if (t.getText().toString().equals(String.valueOf(currentHours)))
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
