package gepalcreations.canwemeet;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> fullList;
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;

    public AutoCompleteAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {

        super(context, resource, textViewResourceId, objects);
        fullList = (ArrayList<String>) objects;
        mOriginalValues = new ArrayList<>(fullList);

    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public String getItem(int position) {
        return fullList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    //TODO: Error null with object lock needs to be fixed
    //====================================================
    private class ArrayFilter extends Filter {
        private Object lock;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            //lock throuws exceptions in logcat. Changed with thread
            //Although it never seem to pass in this if
            if (mOriginalValues == null) {
                //synchronized (lock) {
                Runnable runnable = new Runnable() {
                    public void run(){
                        mOriginalValues=new ArrayList<>(fullList); //
                        Log.e("mOriginalVa","Set--------------------------------------------");
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                // }
            }

            int counter = 0;

			//This is always null fix it, disabled for no error
			//=======================================================================================


            //prefix= prefix.toString().replaceAll(" ", "-");
            //!!!exception and details
            //  https://gist.github.com/chronvas/3f450e70292a84981a30
            prefix = prefix.toString().replaceAll(" ", "_");

            if (prefix == null || prefix.length() == 0) {
                Runnable runnable = new Runnable() {
                    public void run(){
                    ArrayList<String> list = new ArrayList<>(mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                        Log.e("PASS","-------------------------------------------------pas");
                    }
                };
                Thread mythread = new Thread(runnable);
                mythread.start();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> values = mOriginalValues;
                int count = values.size();

                ArrayList<String> newValues = new ArrayList<>(count);

                for (int i = 0; i < count; i++) {
                    String item = values.get(i);
                    if (item.toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                        counter++;
                    }
                }
                if (counter == 0) {
                    prefix = prefix.toString().replaceAll("_", "-");
                    prefixString = prefix.toString().toLowerCase();
                    values = mOriginalValues;
                    newValues = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        String item = values.get(i);
                        if (item.toLowerCase().contains(prefixString)) {
                            newValues.add(item);
                            counter++;
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }       return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                fullList = (ArrayList<String>) results.values;
            } else {
                fullList = new ArrayList<>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
