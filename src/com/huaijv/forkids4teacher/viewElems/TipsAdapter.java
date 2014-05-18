package com.huaijv.forkids4teacher.viewElems;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huaijv.forkids4teacher.R;

public class TipsAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;

	public final class ListItemView {
		public TextView title;
		public TextView time;
		public TextView content;
	}

	public TipsAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(this.context);
		this.listItems = listItems;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.tips_listview_item,
					null);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.titleitem);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.timeitem);

			listItemView.content = (TextView) convertView
					.findViewById(R.id.contentitem);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		listItemView.title.setText((String) listItems.get(position)
				.get("title"));
		listItemView.time.setText((String) listItems.get(position).get("time"));
		listItemView.content.setText((String) listItems.get(position).get(
				"textContent"));

		return convertView;
	}

}
