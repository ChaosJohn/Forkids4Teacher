package com.huaijv.forkids4teacher.viewElems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.GlobalVariables;
import com.huaijv.forkids4teacher.utils.DownLoadAvatarWithCache;

public class BusListViewAdapter extends BaseAdapter {

	public Context context = null;

	private List<Map<String, Object>> listItems = null;

	private LayoutInflater listContainer = null;

	private List<Boolean> boolList;

	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	private final class BusListViewItem {
		public ImageView avatarImageView;
		public TextView nameTextView;
		public TextView phoneTextView;
		public LinearLayout linearLayout;
		public ImageView callBtn;
	}

	private final class BusStopListViewItem {
		public TextView stopTextView;
		public LinearLayout linearLayout;
	}

	public BusListViewAdapter(Context context,
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
	public int getItemViewType(int position) {
		int p = position;
		if (listItems.get(position).containsKey("stop"))
			return TYPE_1;
		else
			return TYPE_2;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
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
		// Log.i("position",
		// "" + position + listItems.get(position).containsKey("stop"));
		//
		// if (listItems.get(position).containsKey("stop")) {
		// BusStopListViewItem busStopListViewItem = null;
		// if (null == convertView) {
		// busStopListViewItem = new BusStopListViewItem();
		// convertView = listContainer.inflate(
		// R.layout.bus_listview_item_stop, null);
		// busStopListViewItem.stopTextView = (TextView) convertView
		// .findViewById(R.id.bus_listview_item_stop_name);
		// busStopListViewItem.linearLayout = (LinearLayout) convertView
		// .findViewById(R.id.bus_stop_listview_item_layout);
		// busStopListViewItem.stopTextView.setText(listItems.get(position)
		// .get("stop").toString());
		// convertView.setTag(busStopListViewItem);
		// } else {
		// busStopListViewItem = (BusStopListViewItem) convertView
		// .getTag();
		// }
		//
		// AbsListView.LayoutParams linearLayoutParams = new
		// AbsListView.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// linearLayoutParams.height = GlobalVariables.screenHeight / 9;
		// (busStopListViewItem.linearLayout)
		// .setLayoutParams(linearLayoutParams);
		// busStopListViewItem.linearLayout.setBackgroundColor(Color
		// .parseColor("#EEEEEE"));
		// } else {
		// BusListViewItem busListViewItem = null;
		// if (null == convertView) {
		// busListViewItem = new BusListViewItem();
		// convertView = listContainer.inflate(R.layout.bus_listview_item_kid,
		// null);
		// busListViewItem.avatarImageView = (ImageView) convertView
		// .findViewById(R.id.bus_listview_item_kid_avatar);
		// busListViewItem.nameTextView = (TextView) convertView
		// .findViewById(R.id.bus_listview_item_kid_name);
		// busListViewItem.phoneTextView = (TextView) convertView
		// .findViewById(R.id.bus_listview_item_kid_phone);
		// busListViewItem.linearLayout = (LinearLayout) convertView
		// .findViewById(R.id.bus_listview_item_layout);
		// busListViewItem.callBtn = (ImageView) convertView
		// .findViewById(R.id.call_btn);
		// convertView.setTag(busListViewItem);
		// } else {
		// System.out.println(convertView.toString());
		// busListViewItem = (BusListViewItem) convertView.getTag();
		// }
		//
		// new DownLoadAvatarWithCache(busListViewItem.avatarImageView)
		// .execute((String) (listItems.get(position).get("avatar")));
		// busListViewItem.nameTextView.setText((String) (listItems
		// .get(position).get("name")));
		// busListViewItem.phoneTextView.setText((String) (listItems
		// .get(position).get("phone")));
		// busListViewItem.callBtn.setTag(position);
		//
		// if (boolList.get(position)) {
		// busListViewItem.linearLayout.setBackgroundColor(Color
		// .parseColor("#E9CBCA"));
		// } else
		// busListViewItem.linearLayout.setBackgroundColor(Color
		// .parseColor("#EEEEEE"));
		//
		// AbsListView.LayoutParams linearLayoutParams = new
		// AbsListView.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// linearLayoutParams.height = GlobalVariables.screenHeight / 9;
		// (busListViewItem.linearLayout).setLayoutParams(linearLayoutParams);
		//
		// busListViewItem.callBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int position = Integer.parseInt(v.getTag().toString());
		// BusListViewAdapter.this.context.startActivity(new Intent(
		// "android.intent.action.CALL", Uri.parse("tel:"
		// + listItems.get(position).get("phone"))));
		// }
		// });
		// }

		BusStopListViewItem busStopListViewItem = null;
		BusListViewItem busListViewItem = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case TYPE_1:
				busStopListViewItem = new BusStopListViewItem();
				convertView = listContainer.inflate(
						R.layout.bus_listview_item_stop, null);
				busStopListViewItem.stopTextView = (TextView) convertView
						.findViewById(R.id.bus_listview_item_stop_name);
				busStopListViewItem.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.bus_stop_listview_item_layout);
				Log.i("xxx", listItems.get(position).get("stop").toString());
				convertView.setTag(busStopListViewItem);
				break;
			case TYPE_2:
				busListViewItem = new BusListViewItem();
				convertView = listContainer.inflate(
						R.layout.bus_listview_item_kid, null);
				busListViewItem.avatarImageView = (ImageView) convertView
						.findViewById(R.id.bus_listview_item_kid_avatar);
				busListViewItem.nameTextView = (TextView) convertView
						.findViewById(R.id.bus_listview_item_kid_name);
				busListViewItem.phoneTextView = (TextView) convertView
						.findViewById(R.id.bus_listview_item_kid_phone);
				busListViewItem.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.bus_listview_item_layout);
				busListViewItem.callBtn = (ImageView) convertView
						.findViewById(R.id.call_btn);
				convertView.setTag(busListViewItem);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case TYPE_1:
				busStopListViewItem = (BusStopListViewItem) convertView
						.getTag();
				break;
			case TYPE_2:
				busListViewItem = (BusListViewItem) convertView.getTag();
				break;
			default:
				break;
			}
		}

		AbsListView.LayoutParams linearLayoutParams = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		linearLayoutParams.height = GlobalVariables.screenHeight / 9;

		switch (type) {
		case TYPE_1:
			busStopListViewItem.stopTextView.setText(listItems.get(position)
					.get("stop").toString());
			(busStopListViewItem.linearLayout)
					.setLayoutParams(linearLayoutParams);
			busStopListViewItem.linearLayout.setBackgroundColor(Color
					.parseColor("#EEEEEE"));
			break;
		case TYPE_2:
			new DownLoadAvatarWithCache(busListViewItem.avatarImageView)
					.execute((String) (listItems.get(position).get("avatar")));
			busListViewItem.nameTextView.setText((String) (listItems
					.get(position).get("name")));
			busListViewItem.phoneTextView.setText((String) (listItems
					.get(position).get("phone")));
			busListViewItem.callBtn.setTag(position);

			if (boolList.get(position)) {
				busListViewItem.linearLayout.setBackgroundColor(Color
						.parseColor("#E9CBCA"));
			} else
				busListViewItem.linearLayout.setBackgroundColor(Color
						.parseColor("#EEEEEE"));

			(busListViewItem.linearLayout).setLayoutParams(linearLayoutParams);

			busListViewItem.callBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int position = Integer.parseInt(v.getTag().toString());
					BusListViewAdapter.this.context.startActivity(new Intent(
							"android.intent.action.CALL", Uri.parse("tel:"
									+ listItems.get(position).get("phone"))));
				}
			});
			break;
		default:
			break;
		}

		return convertView;
	}

	public Map<String, Object> removeItem(int position) {
		if (!listItems.get(position).containsKey("stop")) {
			Map<String, Object> map = listItems.remove(position);
			boolList.remove(position);
			notifyDataSetChanged();
			return map;
		}
		return null;
	}

	// @Override
	// public boolean isEnabled(int position) {
	// if (listItems.get(position).containsKey("stop")) {
	// return false;
	// }
	// return super.isEnabled(position);
	// }

	public Map<String, Object> removeItemWithoutUpdate(int position) {
		if (!listItems.get(position).containsKey("stop")) {
			Map<String, Object> map = listItems.remove(position);
			boolList.remove(position);
			return map;
		}
		return null;
	}

	public List<Map<String, Object>> removeAll() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String log = "";
		for (int i = getCount() - 1; i >= 0; i--) {
			if (boolList.get(i) && !listItems.get(i).containsKey("stop")) {
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
			if (!listItems.get(i).containsKey("stop")) {
				boolList.set(i, true);
				log += "[" + i + ", " + boolList.get(i) + "] ";
			}
		}
		Log.i("checkAll()", log);
		notifyDataSetChanged();
	}

	public void unCheckAll() {
		for (int i = 0; i < getCount(); i++)
			if (!listItems.get(i).containsKey("stop"))
				boolList.set(i, false);
		notifyDataSetChanged();
	}

	public void changeCheckStatus(int position) {
		if (!listItems.get(position).containsKey("stop")) {
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
}
