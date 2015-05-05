package gepalcreations.canwemeet;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private Calendar current;
    private View line;
    Context context = this;


    private static final int hoursPerDp = 20;
    private static final String TAG = "TimeZone";
	int timeZone = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting time from clock and timezone animation class in minutes
        Clock mClock = new Clock();
        int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();
		int currentTimeZone = mClock.getTimeZone();
		Log.e("hours",String.valueOf(currentHours));


        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.time_linear_layout);
		timeLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //Loading Line
        line = (View) findViewById(R.id.line);

        int timeCalculation = getTimeCalculation(currentMinutes, currentHours);
        line.setTranslationY(timeCalculation);

        //checking if we are in the same time zone to do other logic.
        int timeZoneCheck = compareSameTimeZone(timeZone,currentTimeZone);
        if (timeZoneCheck == 1) {
            timeZone = 0;
        }

        loadImages(timeZone);
    }

    private int getTimeCalculation(int currentMinutes, int currentHours) {
        //Calculate time for line animation
		int timeCalculation = currentHours * hoursPerDp + currentMinutes;
		int pixels = dpToPixels(context,timeCalculation);

        Log.e("TimeCalculation", String.valueOf(timeCalculation));
        return pixels;
    }

    ViewHolder holder;


    public void loadImages(int timeZone) {

        String imageName;

        //checking and correcting for negative value
        if (timeZone < 0) {
            timeZone = timeZone * (-1);
        }

        for (int i = 0; i < 25; i++) {
            int indexOfFiles = (i + timeZone) % 25;
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.time_layout, timeLinearLayout, false);
            holder = new ViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.time_image);

            if (indexOfFiles != 0) {
                imageName = "time24h" + indexOfFiles;


                try {
                    holder.image.setImageDrawable(getAssetImage(this, imageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                v.setTag(holder);
                timeLinearLayout.addView(v);
            }

        }

    }


    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".jpeg")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
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

    public static int dpToPixels(Context context, float dp) {

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);

    }

    private class ViewHolder {
        ImageView image;
    }

}
