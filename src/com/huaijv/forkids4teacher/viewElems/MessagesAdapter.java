package com.huaijv.forkids4teacher.viewElems;

import java.util.List;
import java.util.Map;

import com.huaijv.forkids4teacher.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessagesAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> listItems;
	private LayoutInflater listContainer;
	private String timeString; 
	private String[] timeStrings;

	public final class ListItemView {
		public TextView time;
		public TextView content;
	}

	public MessagesAdapter(Context context, List<Map<String, Object>> listItems) {
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
			convertView = listContainer.inflate(
					R.layout.messages_listview_item, null);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.messages_listview_item_time);

			listItemView.content = (TextView) convertView
					.findViewById(R.id.messages_listview_item_content);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		timeString = listItems.get(position).get("createAt").toString();
		if (null != timeString && "null" != timeString) {
			timeStrings = timeString.split(" "); 
			timeString = timeStrings[0];
		}
		listItemView.time.setText(timeString);
		listItemView.content.setText((String) listItems.get(position).get(
				"annoContent"));

		return convertView;
	}

}
