package com.huaijv.forkids4teacher.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.GlobalVariables;
import com.huaijv.forkids4teacher.model.MessagesItem;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;
import com.huaijv.forkids4teacher.viewElems.MessagesAdapter;

public class Messages extends Activity {

	private ListView messagesListView = null;
	private List<Map<String, Object>> listItems = null;
	private MessagesAdapter messagesAdapter = null;
	private Map<String, Object> map = null;
	private ProgressBar progressBar = null;
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerLayoutParams = null;
	private LayoutInflater inflater = null;
	private View progressBarView = null;
	private EditText editText = null;
	private Handler childHandler = null, mainHandler = null;
	private ChildThread childThread = null;
	private GlobalApplication app = null;
	private String messageString = null;

	private MessagesItem[] messagesItems = {
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"),
			new MessagesItem("2014-03-05",
					"今天由于天气原因，学校将提前至3点放学，请各位家长提前准备好来接自己的宝宝，谢谢"), };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);

		app = (GlobalApplication) this.getApplication();
		app.activities.add(this);

		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (app.MSG_ERROR == msg.what) {
					Toast.makeText(Messages.this, "err", Toast.LENGTH_LONG)
							.show();
					app.cancelProgressBar();
				} else if (app.MSG_GET == msg.what) {
					setView();
					app.cancelProgressBar();
				} else if (app.MSG_UPLOAD == msg.what) {
					editText.setText("");
					childHandler.sendEmptyMessage(app.MSG_GET);
				}
			}
		};

		childThread = new ChildThread(new WorkForMain() {

			@Override
			public void doJob(Message msg) {
				// TODO Auto-generated method stub
				Message toMain = mainHandler.obtainMessage();
				if (app.MSG_UPLOAD == msg.what) {
					map = new HashMap<String, Object>();
					map.put("annoContent", (String) msg.obj);
					if (!("err".equalsIgnoreCase(NetUtils
							.postDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidannouncements",
									JsonUtils.map2JsonObjectString(map),
									app.getLoginString())))) {
						Toast.makeText(Messages.this, map.toString(),
								Toast.LENGTH_LONG).show();
						toMain.obj = map;
						toMain.what = app.MSG_UPLOAD;
						mainHandler.sendMessage(toMain);
					} else {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					}
				} else if (app.MSG_GET == msg.what) {
					String rev = NetUtils
							.getDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidannouncements?from=Teacher",
									app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						try {
							listItems = JsonUtils.jsonArray2List(rev);
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

		init();
		// setView();
		setListener();

		app.showProgressBar();
		childHandler.sendEmptyMessage(app.MSG_GET);

		// app.showProgressBar();
	}

	private void init() {
		inflater = LayoutInflater.from(Messages.this);
		// setListItems();
	}

	private void setListItems() {
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < messagesItems.length; i++) {
			map = new HashMap<String, Object>();
			map.put("time", messagesItems[i].getTime());
			map.put("content", messagesItems[i].getContent());
			listItems.add(map);
		}
	}

	private void setView() {
		editText = (EditText) findViewById(R.id.messages_edittext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.height = GlobalVariables.screenHeight / 10;
		params2.height = GlobalVariables.screenHeight / 12;
		findViewById(R.id.message_header).setLayoutParams(params);
		findViewById(R.id.message_footer).setLayoutParams(params2);
		messagesListView = (ListView) findViewById(R.id.listview);
		messagesAdapter = new MessagesAdapter(Messages.this, listItems);
		messagesListView.setAdapter(messagesAdapter);
	}

	private void setListener() {
		findViewById(R.id.messages_send).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						messageString = editText.getText().toString();
						if (!"".equalsIgnoreCase(messageString)) {
							app.showProgressBar();
							Message toChild = childHandler
									.obtainMessage(app.MSG_UPLOAD);
							toChild.obj = messageString;
							childHandler.sendMessage(toChild);
						}
					}
				});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.activities.remove(this);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			app.activities.remove(this);
			app.cancelProgressBar();
			finish();
		}
		return true;
	}
}
