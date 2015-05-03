package gepalcreations.canwemeet;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends Activity {
    /*
        int[] timeZonePics =
                {R.mipmap.time24h1, R.mipmap.time24h2, R.mipmap.time24h3, R.mipmap.time24h4, R.mipmap.time24h5, R.mipmap.time24h5,
                        R.mipmap.time24h6, R.mipmap.time24h7, R.mipmap.time24h8, R.mipmap.time24h9, R.mipmap.time24h10, R.mipmap.time24h11,
                        R.mipmap.time24h12, R.mipmap.time24h13, R.mipmap.time24h14, R.mipmap.time24h15, R.mipmap.time24h16, R.mipmap.time24h17,
                        R.mipmap.time24h18, R.mipmap.time24h19, R.mipmap.time24h20, R.mipmap.time24h21, R.mipmap.time24h22, R.mipmap.time24h23,
                        R.mipmap.time24h24};*/
    private LinearLayout timeLinearLayout;
    private Calendar current;
private static final String TAG = "TimeZone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLinearLayout = (LinearLayout) findViewById(R.id.time_linear_layout);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);

        loadImages();
    }

    ViewHolder holder;

    public void loadImages() {


        current = Calendar.getInstance();

        TimeZone tzCurrent = current.getTimeZone();
        int timeZone = tzCurrent.getRawOffset();
        String imageName = null;
        int j = 1;
        boolean startAgain = false;

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

       Log.v(TAG, String.valueOf(timeZone));

    }


    private class ViewHolder {
        ImageView image;
    }

}
