package gepalcreations.canwemeet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.mViewHolder> {

	RecyclerView mRecyclerView;
	private LayoutInflater inflater;
	private LinearLayout rowLinearLayout;
	Context context;
	List<JodaZones> data = Collections.emptyList();

	public TimeZoneAdapter(){}

	public TimeZoneAdapter(Context context, List<JodaZones> data) {
		inflater = LayoutInflater.from(context);

	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.activity_search_list, container, false);
		mRecyclerView =(RecyclerView) (layout.findViewById(R.id.recycle_list));
		return layout;
	}



	@Override
	public mViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View  view = inflater.inflate(R.layout.list_row, rowLinearLayout, false);
		mViewHolder viewHolder = new mViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(mViewHolder holder, int position) {
		JodaZones current = data.get(position);
		holder.mTextView.setText(current.city);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class mViewHolder extends RecyclerView.ViewHolder{

		TextView mTextView;

		public mViewHolder(View itemView) {

			super(itemView);
			mTextView = (TextView) itemView.findViewById(R.id.search_result_View);


		}
	}
}

