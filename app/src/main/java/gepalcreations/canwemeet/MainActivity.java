package gepalcreations.canwemeet;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    int[] timeZonePics =
            {R.mipmap.time24h1, R.mipmap.time24h2, R.mipmap.time24h3, R.mipmap.time24h4, R.mipmap.time24h5, R.mipmap.time24h5,
             R.mipmap.time24h6, R.mipmap.time24h7, R.mipmap.time24h8,R.mipmap.time24h9,R.mipmap.time24h10, R.mipmap.time24h11,
             R.mipmap.time24h12, R.mipmap.time24h13, R.mipmap.time24h14, R.mipmap.time24h15, R.mipmap.time24h16, R.mipmap.time24h17,
             R.mipmap.time24h18, R.mipmap.time24h19, R.mipmap.time24h20, R.mipmap.time24h21, R.mipmap.time24h22, R.mipmap.time24h23,
             R.mipmap.time24h24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
