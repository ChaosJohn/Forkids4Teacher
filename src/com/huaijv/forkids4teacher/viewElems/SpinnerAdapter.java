package com.huaijv.forkids4teacher.viewElems;

import java.util.ArrayList;
import java.util.List;

import com.huaijv.forkids4teacher.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SpinnerAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	ArrayList<String> list;

	public SpinnerAdapter(Context context, ArrayList<String> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_item, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = (RelativeLayout) convertView
					.findViewById(R.id.spinner_week_dropdown_layout);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.week);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.layout.setBackgroundResource(R.drawable.spinner_border);
		viewHolder.textView.setText(list.get(position));
		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout;
		TextView textView;
		EditText edittext;
	}

	public void refresh(List<String> l) {
		this.list.clear();
		list.addAll(l);
		notifyDataSetChanged();
	}

	public void add(String str) {
		list.add(str);
		notifyDataSetChanged();
	}

	public void add(ArrayList<String> str) {
		list.addAll(str);
		notifyDataSetChanged();

	}

}
