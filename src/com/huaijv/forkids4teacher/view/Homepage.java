package com.huaijv.forkids4teacher.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.DownLoadAvatarWithCache;

/**
 * Homepage[activity]: 首页
 * 
 * @author chaos
 * 
 */
public class Homepage extends Activity {

	private ImageView avatarImageView = null;
	private long exitTime = 0;
	private GlobalApplication app = null;

	/*
	 * state: 判断程序是否崩溃过
	 */
	private int state = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);

		app = (GlobalApplication) this.getApplication();
		if (!app.activities.add(this))
			app.activities.add(this);

		try {
			/*
			 * 验证程序是否出现过崩溃，若崩溃过，则Application的数据也会消失，所以要重启app
			 */
			System.out.println(app.basicInfoMap.get("portrait").toString());
			state = 1;
		} catch (Exception e) {
			/*
			 * 重启ap
			 */
			int activityLength = app.activities.size();
			for (int i = activityLength - 1; i >= 0; i--) {
				if (app.activities.get(i) != Homepage.this) {
					Activity toExitActivity = app.activities.remove(i);
					toExitActivity.finish();
				}
			}
			startActivity(new Intent(this, Welcome.class));
			finish();
		}
		if (1 == state) {
			/*
			 * 设置老师头像/姓名
			 */
			avatarImageView = (ImageView) findViewById(R.id.homepage_avatar);
			new DownLoadAvatarWithCache(avatarImageView)
					.execute(app.basicInfoMap.get("portrait").toString());
			((TextView) findViewById(R.id.homepage_username))
					.setText(app.basicInfoMap.get("displayName").toString());
			setButtonClickListener();
		}
	}

	/**
	 * setButtonClickListener: 设置各个按钮的监听事件
	 */
	private void setButtonClickListener() {
		Homepage.this.findViewById(R.id.homepage_diary_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this, Journal.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_moments_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this, Upload.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_weekly_recipe_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// startActivity(new Intent(Homepage.this,
						// WeeklyRecipe.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_teaching_plan_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// startActivity(new Intent(Homepage.this,
						// TeachingPlan.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_messages_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this, Messages.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_bus_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this, Bus.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_tips_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this, Tips.class));
					}
				});
		Homepage.this.findViewById(R.id.homepage_attendance_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Homepage.this,
								Attendance.class));
					}
				});
	}

	/**
	 * onKeyDown: 在首页按两下返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			/*
			 * System.currentTimeMillis()无论何时调用，肯定大于2000
			 */
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				app.exitAll();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
