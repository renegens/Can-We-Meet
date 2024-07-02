package gepalcreations.canwemeet;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private LinearLayout timeLinearLayout;
    private Context context = this;
    private Measures measures;
    private InputMethodManager imm;
    private boolean autoCompleteTextViewisOpened = false;
    private TooltipWindow tipWindow;
    private CurrentTimeView tv;

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

//		Unit Testing for null in table

/*		CityTable mCityTable = new CityTable();
		String[] cities = mCityTable.tableReturn();


		for (int i=0; i<cities.length; i++){

				String test  = DateTimeZone.forID(cities[i]).toString();
				System.out.println(i+ "== " + test);

		}*/
		// Continue with code






        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        //Get the appcompat toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }


        //Getting time from clock and timezone animation class in minutes
        Clock mClock = new Clock();
        //int currentMinutes = mClock.getMinutes();
        int currentHours = mClock.getHours();

        TimeZone dtz = TimeZone.getDefault();
        //String selection = dtz.getID();

        // Get the app's shared preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //Get Default Value
        String selection = sharedPref.getString("userdata", null);

        if (selection == null)
            selection = dtz.getID();
        //Log.i("stored selection", String.valueOf(selection));


        int timeZoneDifference = timeCalculator(selection);

        //Loading Layout
        timeLinearLayout = (LinearLayout) findViewById(R.id.current_time_linear);
        timeLinearLayout.setOrientation(LinearLayout.VERTICAL);




        measures = new Measures();

        setPaddingToTextViews(currentHours);

        loadImagesFromXML(timeZoneDifference, currentHours, selection);


        //View line = (View) findViewById(R.id.line);
        //line.setTranslationY(getTimeCalculation(currentHours, mClock.getMinutes(), measures.getLeftLinearHeight()));

    }

    public void tooltip(View anchor) {
        //Enable for next version
        //tipWindow = new TooltipWindow(this);
        //if (!tipWindow.isTooltipShown())
            //   tipWindow.showToolTip(tv.getV(), tv.getTime(), measures.getPadding());
    }
    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      /* Inflate the menu; this adds items to the action bar if
      it is present */
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_search:
                //Initialize Table
                CityTable mCityTable = new CityTable();
                String[] cities = mCityTable.tableReturn();
                LayoutInflater inflator = (LayoutInflater) this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflator.inflate(R.layout.actionbar, null);

                if (getSupportActionBar() != null)
                    getSupportActionBar();

                getSupportActionBar().setDisplayShowCustomEnabled(true);
                getSupportActionBar().setCustomView(v);

                //autoCompleteTextViewisOpened

                final ArrayList<String> searchArrayList = new ArrayList<>(Arrays.asList(cities));

                AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, searchArrayList);

                final AutoCompleteTextView textView = (AutoCompleteTextView) v
                        .findViewById(R.id.editText1);
                textView.setAdapter(adapter);

                if (!autoCompleteTextViewisOpened) {
                    textView.setVisibility(View.VISIBLE);
                    autoCompleteTextViewisOpened = true;
                } else {
                    textView.setVisibility(View.GONE);
                    autoCompleteTextViewisOpened = false;
                }

                textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String selection = (String) parent.getItemAtPosition(position);
                        //Log.i("at position selection ", selection);

                        //Save the usercity which is clicked
                        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("userdata", selection);
                        editor.apply();
                        //Log.i("userdata", selection);


                        timeCalculator(selection);
                        Clock mClock = new Clock();
                        int currentHours = mClock.getHours();
                        int timeZoneDifference = timeCalculator(selection);

                        textView.setText("");


                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                        timeLinearLayout.removeAllViews();


                        loadImagesFromXML(timeZoneDifference, currentHours, selection);


                    }
                });

                textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {


                            if (!v.getText().toString().equals("")) {

                                String selection = v.getText().toString();

                                StringBuffer res = new StringBuffer();

                                String[] strArr = selection.split(" ");
                                for (String str : strArr) {
                                    char[] stringArray = str.trim().toCharArray();
                                    stringArray[0] = Character.toUpperCase(stringArray[0]);
                                    str = new String(stringArray);

                                    res.append(str).append(" ");
                                }

                                if (res.toString().contains("Of")) {
                                    String s1 = res.toString().substring(res.toString().indexOf("Of") + 1);
                                    String s2 = res.toString().substring(0, res.toString().indexOf("Of"));
                                    res = new StringBuffer(s2 + "o" + s1.trim());
                                } else if (res.toString().contains("Es")) {
                                    String s1 = res.toString().substring(res.toString().indexOf("Es") + 1);
                                    String s2 = res.toString().substring(0, res.toString().indexOf("Es"));
                                    res = new StringBuffer(s2 + "e" + s1.trim());
                                } else if (res.toString().contains("Au")) {
                                    String s1 = res.toString().substring(res.toString().indexOf("Au") + 1);
                                    String s2 = res.toString().substring(0, res.toString().indexOf("Au"));
                                    res = new StringBuffer(s2 + "a" + s1.trim());

                                    Log.i("s1", s1);
                                    Log.i("s2", s2);
                                }

                                String currentSelection = null;
                                int counter = 0;
                                selection = res.toString();
                                selection = selection.trim().replaceAll(" ", "_");

                                for (String city : searchArrayList) {
                                    if (city.contains(selection)) {
                                        currentSelection = city;
                                        counter++;
                                    }
                                }

                                if (counter == 0) {
                                    selection = selection.trim().replaceAll("_", "-");
                                    for (String city : searchArrayList) {
                                        if (city.contains(selection)) {
                                            currentSelection = city;
                                            counter++;
                                        }
                                    }
                                }

                                selection = currentSelection;


                                if (counter > 0) {
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    timeCalculator(selection);
                                    Clock mClock = new Clock();
                                    int currentHours = mClock.getHours();

                                    int timeZoneDifference = timeCalculator(selection);

                                    v.setText("");

                                    timeLinearLayout.removeAllViews();

                                    loadImagesFromXML(timeZoneDifference, currentHours, selection);
                                } else {
                                    v.setText("");
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int timeCalculator(String selection) {
        ZonedDateTime defaultZonedDateTime = ZonedDateTime.now();
        ZonedDateTime targetZonedDateTime = ZonedDateTime.now(ZoneId.of(selection));
        return targetZonedDateTime.getHour() - defaultZonedDateTime.getHour();
    }

    ViewHolder holder;


    public void loadImagesFromXML(int timeZone, int ct, String selection) {
		int flag = 1; //True when timezone is positive
        // checking and correcting for negative value
        if (timeZone < 0) {
            timeZone = 24 - ((timeZone * (-1)));
			flag = -1;


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

        Drawable sleepImage;
        Drawable coffeeImage;
        Drawable workImage;
        Drawable homeImage;

        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            sleepImage = getResources().getDrawable(R.mipmap.ic_sleep, getTheme());
            coffeeImage = getResources().getDrawable(R.mipmap.ic_coffee, getTheme());
            workImage = getResources().getDrawable(R.mipmap.ic_work, getTheme());
            homeImage = getResources().getDrawable(R.mipmap.ic_home, getTheme());
        } else {
            sleepImage = getResources().getDrawable(R.mipmap.ic_sleep);
            coffeeImage = getResources().getDrawable(R.mipmap.ic_coffee);
            workImage = getResources().getDrawable(R.mipmap.ic_work);
            homeImage = getResources().getDrawable(R.mipmap.ic_home);
        }


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
        selection = selection.replaceAll("-", " ");
        currentTime.setText(selection);
        currentTime.setBackgroundColor(getResources().getColor(R.color.md_teal_500));
        currentTime.setGravity(Gravity.CENTER);
        currentTime.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        currentTime.setTextColor(getResources().getColor(R.color.md_white_1000));
        currentTime.setTextSize(20);
        currentTime.setPadding(0, measures.getPadding(), 0, measures.getPadding() + +dpToPx(1));
        currentTime.requestFocus();

        wholeLinear.addView(currentTime, cityParams);

        for (int i = 0; i < 25; i++) {
            indexes[i] = ((i + 1) + timeZone) % 25;
            //Log.i("Time", String.valueOf(indexes[i]));
        }

        for (int i = 0; i < 25; i++) {
            if (indexes[i] != 0) {

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.times_layout, timeLinearLayout, false);

                holder = new ViewHolder();

                holder.hour = (TextView) v.findViewById(R.id.hours);

                //Set text color for current hour of right side
                holder.hour.setText(String.valueOf(indexes[i]));

                //Check the time to correct for 0
                if (ct==0) ct=ct+24;

                //Log.i("ct", String.valueOf(ct));
                //Log.i("timezone", String.valueOf(timeZone));
				//When time is negative this will work
				if (flag==1 && timeZone != 0 && indexes[i] == (ct+timeZone)%25 ) {
					holder.hour.setTypeface(null, Typeface.BOLD);
					holder.hour.setTextColor(Color.YELLOW);
					//Log.i("i for not 0", String.valueOf(i));

					//Log.i("ct for not 0", String.valueOf(ct));
					//Log.i("math for not 0", String.valueOf((ct+timeZone)%25+1));

				}
				//When time is forward this works
                if (flag==-1 && timeZone != 0 && indexes[i] == (ct+timeZone)%25 +1 ) {
                        holder.hour.setTypeface(null, Typeface.BOLD);
                        holder.hour.setTextColor(Color.YELLOW);
                    //Log.i("i for not 0", String.valueOf(i));

                    //Log.i("ct for not 0", String.valueOf(ct));
                    //Log.i("math for not 0", String.valueOf((ct+timeZone)%25+1));

                }
                //This is when timezone is same
                if (timeZone == 0 && indexes[i] == ct) {

                        holder.hour.setTypeface(null, Typeface.BOLD);
                        holder.hour.setTextColor(Color.YELLOW);
                    //Log.i("i", String.valueOf(i));
                    //Log.i("timezone", String.valueOf(timeZone));

                    //Log.i("math", String.valueOf((ct + timeZone) % 25));
                    }

                holder.hour.measure(widthMeasureSpec, heightMeasureSpec);

                if (indexes[i] >= 1 && indexes[i] <= 7) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_red_300));
                    dp = dayPart.NIGHT;
                    tc = textViewColors.PURPLE;
                } else if (indexes[i] >= 8 && indexes[i] <= 10) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_brown_200));
                    dp = dayPart.DAY;
                    tc = textViewColors.TEAL;
                } else if (indexes[i] >= 11 && indexes[i] <= 17) {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_teal_200));
                    dp = dayPart.NOON;
                    tc = textViewColors.AMBER;
                } else {
                    holder.hour.setBackgroundColor(getResources().getColor(R.color.md_blue_grey_300));
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
                View curChild = (firstMorningLinear).getChildAt(i);
                View previousChild = (firstMorningLinear).getChildAt(i - 1);

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
                View curChild = (firstNoonLinear).getChildAt(i);
                View previousChild = (firstNoonLinear).getChildAt(i - 1);

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
                View curChild = (firstAfternoonLinear).getChildAt(i);
                View previousChild = (firstAfternoonLinear).getChildAt(i - 1);

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
                View curChild = (firstNightLinear).getChildAt(i);
                View previousChild = (firstNightLinear).getChildAt(i - 1);

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

        if (currentHours == 0)
            currentHours = 24;

        int[] textViewIds = {R.id.hereTextView, R.id.hour_1, R.id.hour_2, R.id.hour_3, R.id.hour_4, R.id.hour_5, R.id.hour_6, R.id.hour_7, R.id.hour_8, R.id.hour_9, R.id.hour_10, R.id.hour_11, R.id.hour_12, R.id.hour_13, R.id.hour_14, R.id.hour_15, R.id.hour_16, R.id.hour_17, R.id.hour_18, R.id.hour_19, R.id.hour_20, R.id.hour_21, R.id.hour_22, R.id.hour_23, R.id.hour_24};

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
        tv = new CurrentTimeView();
        for (TextView t : textViews) {
            // Math.floor -> px 4.5 to kanei 4
            // Math.ceil -> px 4.5 to kanei 5

            if (t.getText().toString().equals(String.valueOf(currentHours))) {
                t.setTypeface(null, Typeface.BOLD);
                t.setTextColor(Color.YELLOW);
                tv.setV(t);
                tv.setTime(t.getText().toString());
            }


            t.setPadding(0, (int) Math.ceil(padding), dpToPx(10), (int) Math.ceil(padding));
		}
		measures.setPadding((int) Math.ceil(padding));

        LinearLayout leftLinear = (LinearLayout) findViewById(R.id.left_layout);

		leftLinear.measure(leftLinear.getWidth(), leftLinear.getHeight());

		measures.setLeftLinearHeight(leftLinear.getMeasuredHeight());

        //Log.i("Linear Height", String.valueOf(leftLinear.getMeasuredHeight()));
        //Log.i("Difference", String.valueOf(Math.abs(activityHeight - leftLinear.getMeasuredHeight())));

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    //Calculate time for line animation
    private float getTimeCalculation(int currentHours, int currentMinutes, float height) {
        // float pixelPerHour = height / 25;
        float pixelPerHour = (height / 24) - (measures.getPadding() / 2);

        return currentHours * pixelPerHour + currentMinutes;
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

       finish();
    }

    @Override
    protected void onDestroy() {
        //if (tipWindow != null && tipWindow.isTooltipShown())
         //   tipWindow.dismissTooltip();
        super.onDestroy();
		finish();
    }

    private class ViewHolder {
        TextView hour;

    }

    private class CurrentTimeView {
        View v;
        String time;

        public View getV() {
            return v;
        }

        public String getTime() {
            return time;
        }


        public void setV(View v) {
            this.v = v;
        }

        public void setTime(String time) {
            this.time = time;
        }
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
