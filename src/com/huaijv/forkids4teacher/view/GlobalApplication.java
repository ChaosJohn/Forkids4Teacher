package com.huaijv.forkids4teacher.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.GlobalVariables;
import com.huaijv.forkids4teacher.utils.OtherUtils;

/**
 * GlobalApplication[Application]: 全局Application
 * 
 * @author chaos
 * 
 */
public class GlobalApplication extends Application {

	private static GlobalApplication mInstance = null;

	/*
	 * progressbar所需要的一些组件
	 */
	WindowManager windowManager = null;
	WindowManager.LayoutParams windowManagerLayoutParams = null;
	LayoutInflater inflater = null;
	View progressBarView = null;
	int imgIndex = 0;
	List<String> bitmapStringList = null;

	/*
	 * basicInfoMap: app登录时从服务器获取的用户基本信息
	 */
	Map<String, Object> basicInfoMap = null;
	List<Map<String, Object>> journalContentList = null;
	String journalTitle = null;
	String journalTime = null;
	List<Map<String, Object>> tipsContentList = null;
	String tipsTitle = null;
	String tipsTime = null;

	/*
	 * MSG_xxx: 线程操作的消息
	 */
	final int MSG_GET = 0;
	final int MSG_UPLOAD = 1;
	final int MSG_ERROR = -1;
	final int MSG_DOWNLOAD = 2;

	/*
	 * 与服务器数据交互时提供的权限信息"username:password"
	 */
	private static String loginInfoString = null;

	/*
	 * activities: 用于存储打开的所有activity，供app退出时使用
	 */
	public List<Activity> activities = null;

	/*
	 * 标志progressbar是否被移除
	 */
	int progressBarRemoved = 0;

	/**
	 * getLoginString: 从acount文件中获取登录权限"username:password"
	 * 
	 * @return
	 */
	public String getLoginString() {
		if (null == loginInfoString) {
			loginInfoString = (String) OtherUtils
					.loadFromFile("/sdcard/forkids4teacher/account");
		}
		return loginInfoString;
	}

	public void setLoginString(String loginString) {
		this.loginInfoString = loginString;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		activities = new ArrayList<Activity>();
		initProgressBar(this);
		setGlobalVariables();
	}

	/**
	 * setGlobalVariables: 设置全局变量，比如屏幕尺寸
	 */
	private void setGlobalVariables() {
		DisplayMetrics dm = this.getApplicationContext().getResources()
				.getDisplayMetrics();

		int screenWidthDip = dm.widthPixels; // 屏幕宽（dip，如：320dip）
		int screenHeightDip = dm.heightPixels; // 屏幕宽（dip，如：533dip）

		GlobalVariables.screenHeight = screenHeightDip;
		GlobalVariables.screenWidth = screenWidthDip;

	}

	/**
	 * exitAll: 杀死所有的activity，退出app
	 */
	public void exitAll() {
		int activityLength = activities.size();
		for (int i = activityLength - 1; i >= 0; i--) {
			Activity toExitActivity = activities.remove(i);
			toExitActivity.finish();
		}
	}

	/**
	 * initProgressBar: 初始化可全局调用的progressbar
	 * 
	 * @param context
	 */
	public void initProgressBar(Context context) {
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(WINDOW_SERVICE);
		windowManagerLayoutParams = new WindowManager.LayoutParams();
		windowManagerLayoutParams.type = LayoutParams.TYPE_PHONE;
		windowManagerLayoutParams.format = PixelFormat.RGBA_8888;
		windowManagerLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		windowManagerLayoutParams.x = 0;
		windowManagerLayoutParams.y = 0;
		windowManagerLayoutParams.height = 121;
		windowManagerLayoutParams.width = 121;
		windowManagerLayoutParams.gravity = Gravity.CENTER;
		inflater = LayoutInflater.from(context);
		progressBarView = inflater.inflate(R.layout.progressbar_layout, null);
	}

	/**
	 * showProgressBar: 显示progressbar
	 */
	public void showProgressBar() {
		windowManager.addView(progressBarView, windowManagerLayoutParams);
		progressBarRemoved = 0;
	}

	/**
	 * cancelProgressBar: 移除progressbar
	 */
	public void cancelProgressBar() {
		if (progressBarRemoved == 0) {
			windowManager.removeView(progressBarView);
			progressBarRemoved = 1;
		}
	}

	/**
	 * getInstance: 获取Application实例
	 * 
	 * @return
	 */
	public static GlobalApplication getInstance() {
		return mInstance;
	}

}