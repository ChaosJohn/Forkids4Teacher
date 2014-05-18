package com.huaijv.forkids4teacher.view;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.OtherUtils;

/**
 * Login[activity]: 登录界面
 * 
 * @author chaos
 * 
 */
public class Login extends Activity {

	private GlobalApplication app = null;
	private EditText nameEditText = null;
	private EditText passwordEditText = null;
	private Button loginBtn = null;
	private String name = null;
	private String password = null;
	private String loginInfoString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		app = (GlobalApplication) this.getApplication();
		app.activities.add(this);
		nameEditText = (EditText) findViewById(R.id.login_name);
		passwordEditText = (EditText) findViewById(R.id.login_password);
		loginBtn = (Button) findViewById(R.id.login_btn);

		/*
		 * 点击登录按钮，实现登录操作，并且将登录信息（权限信息）存入本地文件
		 */
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name = nameEditText.getText().toString();
				password = passwordEditText.getText().toString();
				loginInfoString = name + ":" + password;
				String dirSrc = "/mnt/sdcard/forkids4teacher/";
				File fileDir = new File(dirSrc);
				if (!fileDir.exists())
					fileDir.mkdir();
				OtherUtils.saveToFile("/sdcard/forkids4teacher/account",
						loginInfoString);
				if (loginInfoString.equalsIgnoreCase((String) OtherUtils
						.loadFromFile("/sdcard/forkids4teacher/account"))) {
					app.setLoginString(loginInfoString);
					Intent intent = new Intent(Login.this, Welcome.class);
					startActivity(intent);
					app.activities.remove(this);
					finish();
				}
			}
		});

	}

}
