package gepalcreations.canwemeet;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;

import android.graphics.Point;

import android.os.Bundle;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.SearchView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private View line;
    Context context = this;
    static int activityHeight = 0;
    int timeZone = 5; //for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Getting time from clock and timezone animation class in minutes
        Clock mClock = new Clock();
        int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();
        int currentTimeZone = mClock.getTimeZone();

        //Log.e("hours", String.valueOf(currentHours));
        int height = getScreenHeight() - getDensityPixelToRemove(context);

        //Log.e("Screen Height",String.valueOf(getScreenHeight()));
        //Log.e("Screen Density",String.valueOf(getDensityPixelToRemove(context)));
        //Log.e("Final Height",String.valueOf(height));

        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.current_time_linear);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //Loading Line
        line = findViewById(R.id.line);


        float timeCalculation = getTimeCalculation(currentHours, currentMinutes, height);
        line.setTranslationY(timeCalculation);

        //checking if we are in the same time zone to do other logic.
        int timeZoneCheck = compareSameTimeZone(timeZone, currentTimeZone);
        if (timeZoneCheck == 1) {
            timeZone = 0;
        }

        setPaddingToTextViews();

        loadImagesFromXML(timeZone);

        Log.i("Height2", String.valueOf(getScreenHeight()));
        Log.i("Height3", String.valueOf(getActionBarHeight()));
        Log.i("Height4", String.valueOf(getStatusBarHeight()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
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
                    if (indexOfFiles == 1) {
                        //int sleepStart = hour.getHeight();
                        //Log.e("Height of Start Sleep",String.valueOf(sleepStart));
                    }
                }

                //den douleuei swsta thelei ftiaksimo
                    /*if (indexOfFiles == 3 ){
                        holder.icon.setImageResource(R.drawable.ic_sleep);
                        holder.icon.setBackgroundColor(getResources().getColor(R.color.md_purple_500));
                    }}*/

                else if (indexOfFiles >= 8 && indexOfFiles <= 10)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_teal_500));
                else if (indexOfFiles >= 11 && indexOfFiles <= 17)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_amber_500));
                else holder.hour.setBackgroundColor(getResources().getColor(R.color.md_brown_500));

                holder.hour.setPaddingRelative(10, 0, 0, 0);


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
        int sum = 0, pixelsDiff;
        double padding;
        for (int i : textViewIds) {
            TextView textView = (TextView) findViewById(i);

            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getScreenWidth(), View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
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
            t.setPadding(0, (int) Math.floor(padding), dpToPx(10), (int) Math.ceil(padding));
        }

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
        float pixelPerHour = height / 25;

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

}
