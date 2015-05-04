package gepalcreations.canwemeet;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends Activity {

    private Context mContext;
    private LinearLayout timeLinearLayout;
    private Calendar current;
    private static final String TAG = "TimeZone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting time from clock animation class in minutes
        ClockAnimation mClockAnimation = new ClockAnimation();
        int timeInMinutes = mClockAnimation.getSeconds();

        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.time_linear_layout);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);

        //Getting height and witdh of layout
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Paint mPaint = new Paint();

        mClockAnimation.drawLine(size,width,height,mPaint);


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

    ViewHolder holder;

    public void loadImages(int timeZone) {

        String imageName = null;
        int j = 1;
        boolean startAgain = false;

        //checking and correcting for negative value
        if (timeZone < 0 ){
            timeZone=timeZone*(-1);
        }

        for (int i = timeZone + 1; i < 25 + timeZone; i++) {
            if (i < 25 && !startAgain) {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.time_layout, timeLinearLayout, false);
                holder = new ViewHolder();

                holder.image = (ImageView) v.findViewById(R.id.time_image);

                imageName = "time24h" + i;
                int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resourceId);

                holder.image.setImageBitmap(bitmap);

                v.setTag(holder);

                timeLinearLayout.addView(v);
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.time_layout, timeLinearLayout, false);
                holder = new ViewHolder();

                holder.image = (ImageView) v.findViewById(R.id.time_image);

                imageName = "time24h" + j;
                int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), resourceId);

                holder.image.setImageBitmap(bitmap);

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


    private class ViewHolder {
        ImageView image;
    }

}
