package com.huaijv.forkids4teacher.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.KidItem;
import com.huaijv.forkids4teacher.viewElems.BusListViewAdapter;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView.RemoveDirection;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView.RemoveListener;

/**
 * Bus[activity]: 校车通知界面
 * 
 * @author chaos
 * 
 */
public class Bus extends Activity {

	private KidItem[] kidItems = {
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"0.张三", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"1.王五", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"2.李四", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"3.赵六", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"4.秦始皇", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"5.毛泽东", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"6.李四光", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"7.陈二狗", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"8.周拔皮", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"9.张三", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"10.王五", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"11.李四", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"12.赵六", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"13.秦始皇", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"14.毛泽东", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"15.李四光", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"16.陈二狗", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"17.周拔皮", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"18.张三", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"19.王五", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"20.李四", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"21.赵六", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"22.秦始皇", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"23.毛泽东", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"24.李四光", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"25.陈二狗", "18605213506"),
			new KidItem(
					"http://t2.baidu.com/it/u=2923210268,2460374937&fm=23&gp=0.jpg",
					"26.周拔皮", "18605213506"), };

	private List<Map<String, Object>> kidItemList = null;
	// private List<Map<String, Object>> leavedKitList = null;
	private Map<String, Object> map = null;

	private SlideCutListView busListView = null;

	private BusListViewAdapter busListViewAdapter = null;

	private int check_all_btn_flag = 1;
	private GlobalApplication app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);

		app = (GlobalApplication) this.getApplication();
		if (!app.activities.contains(this))
			app.activities.add(this);

		init();
		setView();
		setListener();
	}

	/**
	 * init: 初始化
	 */
	private void init() {
		getKidWithStopItemsList();
	}

	/**
	 * setView: 设置数据显示
	 */
	private void setView() {
		busListView = (SlideCutListView) Bus.this
				.findViewById(R.id.bus_listview);
		busListView.setRemoveListener(new mRemoveListener());
		busListViewAdapter = new BusListViewAdapter(Bus.this, kidItemList);
		busListView.setAdapter(busListViewAdapter);
	}

	/**
	 * setListener: 设置监听事件
	 */
	private void setListener() {
		/*
		 * 点击item，切换item的选中状态
		 */
		busListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				busListViewAdapter.changeCheckStatus(arg2);
			}
		});

		/*
		 * “全选”按钮的监听事件
		 */
		Bus.this.findViewById(R.id.select_all_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (1 == check_all_btn_flag) {
							busListViewAdapter.checkAll();
							((Button) v).setText("反选");
							check_all_btn_flag = 0;
						} else {
							busListViewAdapter.unCheckAll();
							((Button) v).setText("全选");
							check_all_btn_flag = 1;
						}
					}
				});

		/*
		 * “确定“按钮监听事件
		 */
		Bus.this.findViewById(R.id.confirm_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						busListViewAdapter.removeAll();
					}
				});

		/*
		 * “返回”按钮监听事件
		 */
		Bus.this.findViewById(R.id.back_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						app.activities.remove(this);
						finish();
					}
				});

		/*
		 * “发送”按钮监听事件
		 */
		Bus.this.findViewById(R.id.sent_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
	}

	/**
	 * getKidWithStopItemsList: 从假数据中得到list
	 */
	private void getKidWithStopItemsList() {
		kidItemList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < kidItems.length; i++) {
			if (0 == i % 5) {
				map = new HashMap<String, Object>();
				Log.i("bus stop index", "[" + i + "-" + (i / 5 + 1) + "] ");
				map.put("stop", "第" + Integer.toString(i / 5 + 1) + "站：xxxx站");
				kidItemList.add(map);
			}
			map = new HashMap<String, Object>();
			map.put("avatar", kidItems[i].getAvatarUrlString());
			map.put("name", kidItems[i].getNameString());
			map.put("phone", kidItems[i].getPhoneString());
			kidItemList.add(map);
		}
	}

	/**
	 * mRemoveListener: 滑动删除的回调接口
	 * 
	 * @author chaos
	 * 
	 */
	public class mRemoveListener implements RemoveListener {
		@Override
		public void removeItem(RemoveDirection direction, int position) {
			busListViewAdapter.removeItem(position);
		}

	}
}
