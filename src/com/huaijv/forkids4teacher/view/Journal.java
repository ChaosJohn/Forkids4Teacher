package com.huaijv.forkids4teacher.view;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.JsoupUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;
import com.huaijv.forkids4teacher.viewElems.JournalAdapter;

/**
 * Journal[activity]: 活动日志显示界面
 * 
 * @author chaos
 * 
 */
public class Journal extends Activity {

	private ListView listView;
	private JournalAdapter diaryAdapter;
	private List<Map<String, Object>> listItems;
	private Handler childHandler = null, mainHandler = null;
	private ChildThread childThread = null;
	private GlobalApplication app = null;
	private Map<String, Object> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journal);

		app = (GlobalApplication) this.getApplication();
		if (!app.activities.contains(this))
			app.activities.add(this);

		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (app.MSG_ERROR == msg.what) {

				} else if (app.MSG_GET == msg.what) {
					setView();
					app.cancelProgressBar();
				} else if (app.MSG_UPLOAD == msg.what) {

				}
			}
		};
		childThread = new ChildThread(new WorkForMain() {

			@Override
			public void doJob(Message msg) {
				// TODO Auto-generated method stub
				Message toMain = mainHandler.obtainMessage();
				String rev = null;
				if (app.MSG_GET == msg.what) {
					rev = NetUtils
							.getDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidclassactivitys?from=Parent",
									app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						try {
							listItems = JsonUtils.jsonArray2List(rev);
							String actId = null;
							String rawContent = null;
							for (int i = 0; i < listItems.size(); i++) {
								actId = listItems.get(i).get("actId")
										.toString();
								rev = NetUtils.getDataByUrl(
										"http://huaijv-sap.eicp.net:8088/forkids/kidclassactivitys/"
												+ actId, app.getLoginString());
								if ("err".equalsIgnoreCase(rev)) {
									mainHandler.sendEmptyMessage(app.MSG_ERROR);
								} else {
									map = JsonUtils.jsonObjectString2Map(rev);
									rawContent = map.get("content").toString();
									/*
									 * 从正文中提取出全文文本和第一张图(如果有图的话)
									 */
									map = JsoupUtils
											.html2MapWithContentAndFirstImage(rawContent);
									listItems.get(i).put("content",
											map.get("textContent"));
									if (map.containsKey("img"))
										listItems.get(i).put("img",
												map.get("img"));
								}
							}
							mainHandler.sendEmptyMessage(app.MSG_GET);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		childThread.start();
		childHandler = childThread.getHandler();
		app.showProgressBar();
		childHandler.sendEmptyMessage(app.MSG_GET);

		setListener();

	}

	/**
	 * setView: 设置数据显示
	 */
	private void setView() {
		listView = (ListView) findViewById(R.id.listview);
		diaryAdapter = new JournalAdapter(this, listItems);
		listView.setAdapter(diaryAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getApplicationContext(),
						JournalDetail.class);
				intent.putExtra("actId", listItems.get(arg2).get("actId")
						.toString());
				startActivity(intent);
			}
		});
	}

	/**
	 * setListener: 设置数据监听
	 */
	private void setListener() {

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				app.activities.remove(this);
				finish();
			}
		});

		findViewById(R.id.input_imageview).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(Journal.this, JournalEdit.class);
						intent.putExtra("from", "journal_home");
						startActivity(intent);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Journal.this, Homepage.class);
			startActivity(intent);
			app.activities.remove(this);
			app.cancelProgressBar();
			finish();
		}
		return true;
	}

}
