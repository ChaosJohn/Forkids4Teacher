package com.huaijv.forkids4teacher.viewElems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.GlobalVariables;
import com.huaijv.forkids4teacher.utils.DownLoadAvatarWithCache;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class AttendanceListViewAdapter extends BaseAdapter {

	public Context context = null;

	private List<Map<String, Object>> listItems = null;

	private LayoutInflater listContainer = null;

	private List<Boolean> boolList;

	private final class AttendanceListViewItem {
		public ImageView avatarImageView;
		public TextView nameTextView;
		public TextView phoneTextView;
		public LinearLayout linearLayout;
		public ImageView callBtn;
	}

	public AttendanceListViewAdapter(Context context,
			List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(this.context);
		this.listItems = listItems;
		initBoolList();
	}

	private void initBoolList() {
		boolList = new ArrayList<Boolean>();
		for (int i = 0; i < getCount(); i++)
			boolList.add(false);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AttendanceListViewItem attendanceListViewItem = null;
		if (null == convertView) {
			attendanceListViewItem = new AttendanceListViewItem();
			convertView = listContainer.inflate(
					R.layout.attendance_listview_item, null);
			attendanceListViewItem.avatarImageView = (ImageView) convertView
					.findViewById(R.id.attendance_listview_item_kid_avatar);
			attendanceListViewItem.nameTextView = (TextView) convertView
					.findViewById(R.id.attendance_listview_item_kid_name);
			attendanceListViewItem.phoneTextView = (TextView) convertView
					.findViewById(R.id.attendance_listview_item_kid_phone);
			attendanceListViewItem.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.attendance_listview_item_layout);
			attendanceListViewItem.callBtn = (ImageView) convertView
					.findViewById(R.id.call_btn);
			convertView.setTag(attendanceListViewItem);
		} else {
			attendanceListViewItem = (AttendanceListViewItem) convertView
					.getTag();
		}

		new DownLoadAvatarWithCache(attendanceListViewItem.avatarImageView)
				.execute((String) (listItems.get(position).get("avatar")));
		attendanceListViewItem.nameTextView.setText((String) (listItems
				.get(position).get("name")));
		attendanceListViewItem.phoneTextView.setText((String) (listItems
				.get(position).get("phone")));
		attendanceListViewItem.callBtn.setTag(position);

		if (boolList.get(position)) {
			attendanceListViewItem.linearLayout.setBackgroundColor(Color
					.parseColor("#E9CBCA"));
		} else
			attendanceListViewItem.linearLayout.setBackgroundColor(Color
					.parseColor("#EEEEEE"));

		AbsListView.LayoutParams linearLayoutParams = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		linearLayoutParams.height = GlobalVariables.screenHeight / 9;
		(attendanceListViewItem.linearLayout)
				.setLayoutParams(linearLayoutParams);

		attendanceListViewItem.callBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = Integer.parseInt(v.getTag().toString());
						AttendanceListViewAdapter.this.context
								.startActivity(new Intent(
										"android.intent.action.CALL", Uri
												.parse("tel:"
														+ listItems.get(
																position).get(
																"phone"))));
					}
				});

		return convertView;
	}

	public Map<String, Object> removeItem(int position) {
		Map<String, Object> map = listItems.remove(position);
		boolList.remove(position);
		notifyDataSetChanged();
		return map;
	}

	public Map<String, Object> removeItemWithoutUpdate(int position) {
		Map<String, Object> map = listItems.remove(position);
		boolList.remove(position);
		return map;
	}

	public List<Map<String, Object>> removeAll() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String log = "";
		for (int i = getCount() - 1; i >= 0; i--) {
			if (boolList.get(i)) {
				Map<String, Object> map = listItems.remove(i);
				boolList.remove(i);
				list.add(map);
				log += i + "--" + map.get("name");
			}
		}
		Log.i("removeAll", log);
		notifyDataSetChanged();
		return list;
	}

	public void addItem(Map<String, Object> map) {
		listItems.add(map);
		notifyDataSetChanged();
	}

	public void checkAll() {
		String log = "";
		for (int i = 0; i < getCount(); i++) {
			boolList.set(i, true);
			log += "[" + i + ", " + boolList.get(i) + "] ";
		}
		Log.i("checkAll()", log);
		notifyDataSetChanged();
	}

	public void unCheckAll() {
		for (int i = 0; i < getCount(); i++)
			boolList.set(i, false);
		notifyDataSetChanged();
	}

	public void changeCheckStatus(int position) {
		if (false == boolList.get(position)) {
			boolList.set(position, true);
			Log.i("changeCheckStatus()",
					"[" + position + ", " + boolList.get(position) + "] ");
		} else {
			boolList.set(position, false);
			Log.i("changeCheckStatus()",
					"[" + position + ", " + boolList.get(position) + "] ");
		}
		notifyDataSetChanged();
	}
}
