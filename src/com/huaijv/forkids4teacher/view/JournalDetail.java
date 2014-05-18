package com.huaijv.forkids4teacher.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.JsoupUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;

/**
 * JournalDetail[activity]: 活动日志的详细内容
 * 
 * @author chaos
 * 
 */
public class JournalDetail extends Activity {

	private TextView titleTextView;
	private TextView timeTextView;
	private LinearLayout layout = null;
	private GlobalApplication app = null;
	private ChildThread childThread = null;
	private Handler mainHandler = null, childHandler = null;
	private Map<String, Object> map = null;
	private List<Map<String, Object>> textList = null;

	/**
	 * actId: 活动日志的id
	 */
	private String actId = null;
	private String timeString = null;
	private List<ImageView> imageViews = null;
	private ImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journal_details);

		app = (GlobalApplication) this.getApplication();
		app.activities.add(this);
		actId = this.getIntent().getExtras().getString("actId").toString();
		imageViews = new ArrayList<ImageView>();
		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (app.MSG_ERROR == msg.what) {
					Toast.makeText(JournalDetail.this, "err", Toast.LENGTH_LONG)
							.show();
				} else if (app.MSG_GET == msg.what) {
					setView();
					app.cancelProgressBar();
				}
			}
		};

		childThread = new ChildThread(new WorkForMain() {
			@Override
			public void doJob(Message msg) {
				String rev = null;
				if (app.MSG_GET == msg.what) {
					rev = NetUtils.getDataByUrl(
							"http://huaijv-sap.eicp.net:8088/forkids/kidclassactivitys/"
									+ actId, app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						try {
							map = JsonUtils.jsonObjectString2Map(rev);
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
		titleTextView = (TextView) findViewById(R.id.title);
		timeTextView = (TextView) findViewById(R.id.time);
		layout = (LinearLayout) findViewById(R.id.content_layout);
		titleTextView.setText(map.get("title").toString());
		textList = JsoupUtils.html2List(map.get("content").toString(),
				JournalDetail.this);
		for (int i = 0; i < textList.size(); i++) {
			if (textList.get(i).containsKey("img")) {
				imageView = (ImageView) textList.get(i).get("imageview");
				imageViews.add(imageView);
				layout.addView(imageView);
			} else if (textList.get(i).containsKey("p")) {
				TextView textView = new TextView(JournalDetail.this);
				textView.setText(textList.get(i).get("p").toString());
				textView.setTextColor(Color.parseColor("#000000"));
				textView.setTextSize(20);
				layout.addView(textView);
			}
		}
		String timeChangedString = map.get("changeAt").toString();
		String timeCreatedString = map.get("createAt").toString();
		if (!(timeChangedString.equalsIgnoreCase("null") && timeCreatedString
				.equalsIgnoreCase("null"))) {
			timeString = (timeChangedString.equalsIgnoreCase("null")) ? timeCreatedString
					: timeChangedString;
			timeTextView.setText(timeString);
		}

	}

	/**
	 * setListener: 设置各个按钮的监听事件
	 */
	private void setListener() {
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.activities.remove(this);
				finish();
			}
		});

		findViewById(R.id.pen).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(JournalDetail.this,
						JournalEdit.class);
				intent.putExtra("from", "journal_detail");
				intent.putExtra("actId", map.get("actId").toString());
				int index = 0;
				for (int i = 0; i < textList.size(); i++) {
					if (textList.get(i).containsKey("img")) {
						textList.get(i).put("bitmap",
								(Bitmap) imageViews.get(index).getTag());
						index++;
					}
				}
				app.journalContentList = textList;
				app.journalTime = timeString;
				app.journalTitle = map.get("title").toString();
				startActivity(intent);
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
