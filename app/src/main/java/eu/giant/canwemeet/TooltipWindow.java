package eu.giant.canwemeet;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;


public class TooltipWindow {

    private static final int MSG_DISMISS_TOOLTIP = 100;
    private Context ctx;
    private PopupWindow tipWindow;
    private View contentView;
    private LayoutInflater inflater;
    ImageView up, down;

    public TooltipWindow(Context ctx) {
        this.ctx = ctx;
        tipWindow = new PopupWindow(ctx);

        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.tooltip_layout, null);
        up = (ImageView) contentView.findViewById(R.id.tooltip_nav_up);
        down = (ImageView) contentView.findViewById(R.id.tooltip_nav_down);
    }

    void showToolTip(View anchor, String time, int padding) {

        tipWindow.setHeight(LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(LayoutParams.WRAP_CONTENT);

        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());

        tipWindow.setContentView(contentView);

        int screen_pos[] = new int[2];
// Get location of anchor view on screen
        anchor.getLocationOnScreen(screen_pos);

        Rect anchor_rect;

        if (Integer.parseInt(time) > 20) {
            up.setVisibility(View.GONE);
            down.setVisibility(View.VISIBLE);

            // Get rect for anchor view
            anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                    + anchor.getWidth() * 2 - dpToPx(2), (int) (screen_pos[1] - anchor.getHeight() * padding));
        } else {
            // Get rect for anchor view
            anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
                    + anchor.getWidth() * 2 + dpToPx(2), screen_pos[1] + (anchor.getHeight() / 2));
        }

        // Call view measure to calculate how big your view should be.
        contentView.measure(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();
        // In this case , i dont need much calculation for x and y position of
        // tooltip
        // For cases if anchor is near screen border, you need to take care of
        // direction as well
        // to show left, right, above or below of anchor view
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);

        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, position_x,
                position_y);

        // send message to handler to dismiss tipWindow after X milliseconds
        handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 2000);

    }

    boolean isTooltipShown() {
        if (tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }

    void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        }

        ;
    };

}