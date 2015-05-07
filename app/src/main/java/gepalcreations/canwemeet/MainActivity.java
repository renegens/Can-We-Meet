package gepalcreations.canwemeet;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private View line;
    private ImageView testImage, testImage2, testImage3, testImage4;
    Context context = this;

    int timeZone = 2; //for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);

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
        //testImage = (ImageView) findViewById(R.id.testimage);
        // testImage2 = (ImageView) findViewById(R.id.testimage2);
        //testImage3 = (ImageView) findViewById(R.id.testimage3);
        // testImage4 = (ImageView) findViewById(R.id.testimage4);

//        testImage.setTranslationY(250);
        //     testImage.setTranslationX(250);
        //     testImage2.setTranslationY(450);
        //     testImage2.setTranslationX(250);
        //     testImage3.setTranslationY(780);
        //     testImage3.setTranslationX(250);
        //     testImage4.setTranslationY(1270);
        //      testImage4.setTranslationX(250);


        float timeCalculation = getTimeCalculation(currentHours, currentMinutes, height);
        line.setTranslationY(timeCalculation);

        //checking if we are in the same time zone to do other logic.
        int timeZoneCheck = compareSameTimeZone(timeZone, currentTimeZone);
        if (timeZoneCheck == 1) {
            timeZone = 0;
        }

        loadImagesFromXML(timeZone);
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
                LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                if (indexOfFiles >= 1 && indexOfFiles <= 7)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_purple_500));
                else if (indexOfFiles >= 8 && indexOfFiles <= 9)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_teal_500));
                else if (indexOfFiles >= 10 && indexOfFiles <= 17)
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_amber_500));
                else holder.hour.setBackgroundColor(getResources().getColor(R.color.md_brown_500));

                holder.hour.setPaddingRelative(10, 0, 0, 0);

                v.setTag(holder);
                timeLinearLayout.addView(v, hourParams);
            }
            Log.i("index", String.valueOf(indexOfFiles));
        }
    }

    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".jpeg")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private static int getDensityPixelToRemove(Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        //action bar 48dp and status bar 20dp
        if (density >= 4.0) {
            return 272;//48+20*3
        }
        if (density >= 3.0) {
            return 204;
        }
        if (density >= 2.0) {
            return 136;
        }
        if (density >= 1.5) {
            return 102;
        }
        return 68;
    }

    public int getScreenHeight() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Point size = new Point();
        display.getSize(size);
        return size.y;
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
    }

}
