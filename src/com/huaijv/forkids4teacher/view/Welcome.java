package com.huaijv.forkids4teacher.view;

import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;
import com.huaijv.forkids4teacher.utils.OtherUtils;

/**
 * Welcome[activity]: 欢迎界面
 * 
 * @author chaos
 * 
 */
public class Welcome extends Activity {

	private Handler mainHandler = null, childHandler = null;
	private ChildThread childThread = null;
	private GlobalApplication app = null;
	private String loginInfoString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		app = (GlobalApplication) this.getApplication();
		app.activities.add(this);

		/*
		 * 从sdcard中获取登录信息
		 */
		loginInfoString = (String) OtherUtils
				.loadFromFile("/sdcard/forkids4teacher/account");

		if (null == loginInfoString) {
			/*
			 * 获取不到，则跳转到登录界面
			 */
			Intent intent = new Intent(Welcome.this, Login.class);
			app.activities.remove(this);
			startActivity(intent);
			finish();
		} else {
			app.setLoginString(loginInfoString);
			mainHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (app.MSG_ERROR == msg.what) {
						Intent intent = new Intent(Welcome.this, Login.class);
						app.activities.remove(this);
						Toast.makeText(Welcome.this, "登录出错!", Toast.LENGTH_LONG)
								.show();
						startActivity(intent);
						finish();
					} else if (app.MSG_GET == msg.what) {
						app.basicInfoMap = (Map<String, Object>) msg.obj;
						// Toast.makeText(Welcome.this,
						// app.basicInfoMap.toString()
						// .toString(), Toast.LENGTH_LONG).show();
						Intent intent = new Intent(Welcome.this, Homepage.class);
						app.activities.remove(this);
						startActivity(intent);
						finish();
					}
				}
			};

			childThread = new ChildThread(new WorkForMain() {

				@Override
				public void doJob(Message msg) {
					// TODO Auto-generated method stub
					if (app.MSG_GET == msg.what) {
						/*
						 * 获取用户基本信息
						 */
						String rev = NetUtils
								.getDataByUrl(
										"http://huaijv-sap.eicp.net:8088/forkids/logon?from=Teacher",
										app.getLoginString());
						if ("err".equalsIgnoreCase(rev)) {
							mainHandler.sendEmptyMessage(app.MSG_ERROR);
						} else {
							Message toMain = mainHandler
									.obtainMessage(app.MSG_GET);
							try {
								toMain.obj = JsonUtils
										.jsonObjectString2Map(rev);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							mainHandler.sendMessage(toMain);
						}
					}
				}
			});

			childThread.start();
			childHandler = childThread.getHandler();
			childHandler.sendEmptyMessage(app.MSG_GET);
		}
	}
}
