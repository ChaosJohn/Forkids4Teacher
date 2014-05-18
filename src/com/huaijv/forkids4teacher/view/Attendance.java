package com.huaijv.forkids4teacher.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.KidItem;
import com.huaijv.forkids4teacher.viewElems.AttendanceListViewAdapter;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView.RemoveDirection;
import com.huaijv.forkids4teacher.viewElems.SlideCutListView.RemoveListener;

/**
 * Attendance[activity]: 点名界面
 * 
 * @author chaos
 * 
 */
public class Attendance extends Activity {

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
	private List<Map<String, Object>> presentKitList = null;

	private AttendanceListViewAdapter attendanceListViewAdapter = null;
	private AttendanceListViewAdapter attendanceListViewAdapter_present = null;
	private AttendanceListViewAdapter attendanceListViewAdapter_current = null;

	private SlideCutListView attendanceListView = null;

	private int change_list_btn_flag = 1;
	private int check_all_btn_flag = 1;

	private GlobalApplication app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);

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
		getKidItemsList();
		presentKitList = new ArrayList<Map<String, Object>>();
	}

	/**
	 * setView: 设置数据显示
	 */
	private void setView() {

		attendanceListView = (SlideCutListView) Attendance.this
				.findViewById(R.id.attendance_listview);
		attendanceListView.setRemoveListener(new mRemoveListener());

		attendanceListViewAdapter = new AttendanceListViewAdapter(
				Attendance.this, kidItemList);
		attendanceListViewAdapter_current = attendanceListViewAdapter;
		attendanceListView.setAdapter(attendanceListViewAdapter_current);
		attendanceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				attendanceListViewAdapter_current.changeCheckStatus(position);
			}
		});
	}

	/**
	 * setListener: 设置监听事件
	 */
	private void setListener() {
		Attendance.this.findViewById(R.id.attendance_classid)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
		/*
		 * 返回键监听事件
		 */
		Attendance.this.findViewById(R.id.back_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						app.activities.remove(this);
						finish();
					}
				});

		/*
		 * ”已点名/未点名“按钮监听事件
		 */
		Attendance.this.findViewById(R.id.change_list_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (1 == change_list_btn_flag) {
							/*
							 * 切换到“未点名”视图
							 */
							attendanceListViewAdapter_present = new AttendanceListViewAdapter(
									Attendance.this, presentKitList);
							attendanceListViewAdapter_current = attendanceListViewAdapter_present;
							((Button) v).setText("未点名");
							change_list_btn_flag = 0;
						} else {
							/*
							 * 切换到“已点名”视图
							 */
							attendanceListViewAdapter = new AttendanceListViewAdapter(
									Attendance.this, kidItemList);
							attendanceListViewAdapter_current = attendanceListViewAdapter;
							((Button) v).setText("已点名");
							change_list_btn_flag = 1;
						}
						attendanceListView
								.setAdapter(attendanceListViewAdapter_current);
						((Button) Attendance.this
								.findViewById(R.id.select_all_btn))
								.setText("全选");
					}
				});
		/*
		 * “全选”按钮监听事件
		 */
		Attendance.this.findViewById(R.id.select_all_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (1 == check_all_btn_flag) {
							attendanceListViewAdapter_current.checkAll();
							((Button) v).setText("反选");
							check_all_btn_flag = 0;
						} else {
							attendanceListViewAdapter_current.unCheckAll();
							((Button) v).setText("全选");
							check_all_btn_flag = 1;
						}

					}
				});

		/*
		 * “确定”按钮监听事件
		 */
		Attendance.this.findViewById(R.id.confirm_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (1 == change_list_btn_flag) {
							/*
							 * 将选中的item从“未点名“视图中移除，添加到”已点名“视图中
							 */
							List<Map<String, Object>> list = attendanceListViewAdapter_current
									.removeAll();
							while (0 != list.size())
								presentKitList.add(0, list.remove(0));
						} else if (0 == change_list_btn_flag) {
							/*
							 * 将选中的item从“已点名“视图中移除，添加到”未点名“视图中
							 */
							List<Map<String, Object>> list = attendanceListViewAdapter_current
									.removeAll();
							while (0 != list.size())
								kidItemList.add(0, list.remove(0));
						}
					}
				});
	}

	/**
	 * getKidItemsList: 从假数据中得到list
	 */
	private void getKidItemsList() {
		kidItemList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < kidItems.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("avatar", kidItems[i].getAvatarUrlString());
			map.put("name", kidItems[i].getNameString());
			map.put("phone", kidItems[i].getPhoneString());
			kidItemList.add(map);
		}
	}

	/**
	 * mRemoveListener: 滑动删除回调接口
	 * 
	 * @author chaos
	 * 
	 */
	public class mRemoveListener implements RemoveListener {
		@Override
		public void removeItem(RemoveDirection direction, int position) {
			if ((1 == change_list_btn_flag)
					&& RemoveDirection.RIGHT == direction) {
				/**
				 * 在“未点名”视图中，向右滑动item将该item移除并且添加到“已点名”视图
				 */
				Map<String, Object> map = (Map<String, Object>) attendanceListViewAdapter
						.removeItem(position);
				presentKitList.add(0, map);
			} else if ((0 == change_list_btn_flag)
					&& RemoveDirection.LEFT == direction) {
				/**
				 * 在“已点名”视图中，向左滑动item将该item移除并且添加到“未点名”视图
				 */
				Map<String, Object> map = (Map<String, Object>) attendanceListViewAdapter_present
						.removeItem(position);
				kidItemList.add(0, map);
			}
		}

	}
}
