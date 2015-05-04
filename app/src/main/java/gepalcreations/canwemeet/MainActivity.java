package gepalcreations.canwemeet;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends Activity {

    private LinearLayout timeLinearLayout;
    private Calendar current;
    private View line;
    Context context = this;


    private static final int hoursPerDt = 20;
    private static final String TAG = "TimeZone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting time from clock animation class in minutes
        Clock mClock = new Clock();
        int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();
        Log.e(TAG, String.valueOf(currentHours));
        Log.e(TAG, String.valueOf(currentMinutes));


        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.time_linear_layout);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //Loading Line
        line = (View) findViewById(R.id.line);
        
        int timeCalculation = getTimeCalculation(currentMinutes, currentHours);


        line.setTranslationY(timeCalculation);
        
        //getting current timeZone from system
        int timeZone = getTimeZone();
        //checking if we are in the same time zone to do other logic.
        int timeZoneCheck = compareSameTimeZone(timeZone);
        if (timeZoneCheck==1){
            timeZone=0;
        }

        Log.e(TAG, String.valueOf(getTimeZone()));
        loadImages(timeZone);
    }

    private int getTimeCalculation(int currentMinutes, int currentHours) {
        //Calculate time for line animation
        float screenDensity = getResources().getDisplayMetrics().density;
        Log.e("ScreenSize", String.valueOf(screenDensity));


        float fTimeCalculation = (currentHours*hoursPerDt*screenDensity+currentMinutes);
        int timeCalculation = (int) fTimeCalculation;

        Log.e("TimeCalculation",String.valueOf(timeCalculation));
        return timeCalculation;
    }

    ViewHolder holder;


    public void loadImages(int timeZone) {

        String imageName = null;
        int j = 1;
        boolean startAgain = false;

        //checking and correcting for negative value
        if (timeZone < 0) {
            timeZone = timeZone * (-1);
        }

        for (int i = timeZone + 1; i < 25 + timeZone; i++) {

            if (i < 25 && !startAgain) {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.time_layout, timeLinearLayout, false);
                holder = new ViewHolder();

                holder.image = (ImageView) v.findViewById(R.id.time_image);

                imageName = "time24h" + i;


                //int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resourceId);

                //holder.image.setImageBitmap(bitmap);

                try {
                    holder.image.setImageDrawable(getAssetImage(this, imageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                v.setTag(holder);

                timeLinearLayout.addView(v);
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.time_layout, timeLinearLayout, false);
                holder = new ViewHolder();

                holder.image = (ImageView) v.findViewById(R.id.time_image);

                imageName = "time24h" + j;


                //int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resourceId);

                //holder.image.setImageBitmap(bitmap);

                try {
                    holder.image.setImageDrawable(getAssetImage(this, imageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                v.setTag(holder);

                timeLinearLayout.addView(v);

                j++;
            }

            if (imageName.equals("time24h24")) {
                j = 1;
                startAgain = true;
            }

        }


    }


    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".jpeg")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    //Getting Local time zone from system
    private int getTimeZone() {
        current = Calendar.getInstance();

        TimeZone tzCurrent = current.getTimeZone();
        return tzCurrent.getRawOffset()/ (60 * 60 * 1000);

    }
    //Method to check if we are in the same time zone as entered by user. If yes then passing the 0 index to the array so
    //it will display the same image.
    private int compareSameTimeZone(int timeZone) {

        Calendar current = Calendar.getInstance();

        TimeZone tzCurrent = current.getTimeZone();
        int time  = tzCurrent.getRawOffset()/ (60 * 60 * 1000);

        if (timeZone != time){

            return timeZone; //not in the same time zone
        }
        int timeZoneIsSame = 1;
        return timeZoneIsSame; //in same time zone
    }

    public static int dpToPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private class ViewHolder {
        ImageView image;
    }

}
