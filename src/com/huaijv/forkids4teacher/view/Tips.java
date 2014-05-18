package com.huaijv.forkids4teacher.view;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.huaijv.forkids4teacher.viewElems.TipsAdapter;

/**
 * Tips[activity]: 育英知识界面(几乎和Journal.java一样)
 * 
 * @author chaos
 * 
 */
public class Tips extends Activity {

	private ListView listView;
	private TipsAdapter tipsAdapter;
	private List<Map<String, Object>> listItems;

	private GlobalApplication app = null;
	private ChildThread childThread = null;
	private Handler mainHandler = null, childHandler = null;
	private Map<String, Object> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips);

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
				String rev = null;
				if (app.MSG_GET == msg.what) {
					rev = NetUtils
							.getDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidnursingknowledges?from=Teacher&page=1&size=10",
									app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						try {
							listItems = JsonUtils.jsonArray2List(rev);
							for (int i = 0; i < listItems.size(); i++) {
								map = JsoupUtils
										.html2MapWithContentAndFirstImage(listItems
												.get(i).get("content")
												.toString());
								listItems.get(i).put("textContent",
										map.get("textContent").toString());
								if (map.containsKey("img")) {
									listItems.get(i).put("img",
											map.get("img").toString());
								}
								String timeChangedString = listItems.get(i)
										.get("changeAt").toString();
								String timeCreatedString = listItems.get(i)
										.get("createAt").toString();
								if (!(timeChangedString
										.equalsIgnoreCase("null") && timeCreatedString
										.equalsIgnoreCase("null"))) {
									String timeString = (timeChangedString
											.equalsIgnoreCase("null")) ? timeCreatedString
											: timeChangedString;
									String[] timeStrings = timeString
											.split(" ");
									listItems.get(i)
											.put("time", timeStrings[0]);
								}
							}
							mainHandler.sendEmptyMessage(app.MSG_GET);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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

	private void setView() {
		listView = (ListView) findViewById(R.id.listview);
		tipsAdapter = new TipsAdapter(this, listItems);
		listView.setAdapter(tipsAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						TipsDetail.class);
				intent.putExtra("knId", listItems.get(arg2).get("knId")
						.toString());
				startActivity(intent);
			}
		});
	}

	private void setListener() {
		findViewById(R.id.back_tips).setOnClickListener(new OnClickListener() {

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
						intent.setClass(Tips.this, TipsEdit.class);
						intent.putExtra("from", "tips_home");
						startActivity(intent);
					}
				});
	}

}
