package gepalcreations.canwemeet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Calendar;


public class ClockAnimation {

    public int getSeconds() {
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.MINUTE);
        return seconds;
    }

    public void drawLine(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        int startx = 0;
        int starty = 0;
        int endx = 0;
        int endy = 0;
        canvas.drawLine(startx, starty, endx, endy, paint);
    }




    // Line

}
