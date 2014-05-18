package com.huaijv.forkids4teacher.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;
import com.huaijv.forkids4teacher.viewElems.GridAdapter;
import com.huaijv.forkids4teacher.viewElems.SpinnerAdapter;

/**
 * Upload[activity]: 精彩瞬间图片上传界面
 * 
 * @author chaos
 * 
 */
public class Upload extends Activity {

	private ImageView imageView; // 图片
	private ImageView imageButton;
	private Bitmap bitmap;
	private GridView gridView;
	private byte[] mContent;
	private Map<String, Object> map;
	private GridAdapter gridViewAdapter;
	private List<Map<String, Object>> listItems = null;
	private ArrayList<String> spinnerList;// 传进spinner列表中的数据
	private LinearLayout layout;
	private ListView listView;// spinner列表
	private SpinnerAdapter spinnerAdapter;
	private PopupWindow popupWindow;// 弹出的下拉窗口
	private LinearLayout spinnerLayout;// 弹出下拉窗口的位置
	private LinearLayout layout2;// spinner下面的布局
	private TextView mTView;// 盛放spinner中的文字的TextView
	private ChildThread childThread;
	private Handler mainHandler, childHandler;
	private EditText contentEditText = null;
	private GlobalApplication app = null;
	private String albumId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		app = (GlobalApplication) this.getApplication();
		if (!app.activities.contains(this))
			app.activities.add(this);
		initView();
		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (app.MSG_GET == msg.what) {
					listItems = (List<Map<String, Object>>) msg.obj;
					setSpinner();
				} else if (app.MSG_ERROR == msg.what) {
					Toast.makeText(Upload.this, "err", Toast.LENGTH_SHORT)
							.show();
				} else if (app.MSG_UPLOAD == msg.what) {
					Toast.makeText(Upload.this, "success!", Toast.LENGTH_SHORT)
							.show();
					contentEditText.setText("");
					gridViewAdapter.clearAll();
				}
				app.cancelProgressBar();
			}
		};

		childThread = new ChildThread(new WorkForMain() {

			@Override
			public void doJob(Message msg) {
				if (app.MSG_GET == msg.what) {
					String rev = NetUtils
							.getDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidalbums?from=Parent",
									app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						Message toMain = mainHandler.obtainMessage(app.MSG_GET);
						try {
							toMain.obj = JsonUtils.jsonArray2List(rev);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mainHandler.sendMessage(toMain);
					}
				} else if (app.MSG_UPLOAD == msg.what) {
					String rev = NetUtils
							.postDataByUrl(
									"http://huaijv-sap.eicp.net:8088/forkids/kidmoments",
									msg.obj.toString(), app.getLoginString());
					if ("err".equalsIgnoreCase(rev)) {
						mainHandler.sendEmptyMessage(app.MSG_ERROR);
					} else {
						mainHandler.sendEmptyMessage(app.MSG_UPLOAD);
					}
				}
			}
		});
		childThread.start();
		childHandler = childThread.getHandler();
		app.showProgressBar();
		childHandler.sendEmptyMessage(app.MSG_GET);
		findViewById(R.id.send_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.showProgressBar();
				Message toChild = childHandler.obtainMessage(app.MSG_UPLOAD);
				List<String> list = gridViewAdapter.getBitmapStrBase64List();
				List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
				String mmName = contentEditText.getText().toString();
				for (int i = 0; i < list.size(); i++) {
					map = new HashMap<String, Object>();
					map.put("albumId", albumId);
					map.put("rawData", list.get(i));
					map.put("mmName", mmName);
					mapList.add(map);
				}
				try {
					toChild.obj = JsonUtils.list2JsonArrayString(mapList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				childHandler.sendMessage(toChild);
			}
		});
	}

	/**
	 * initView: 初始化界面显示
	 */
	private void initView() {

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.activities.remove(this);
				finish();
			}
		});

		contentEditText = (EditText) findViewById(R.id.upload_content);

		gridView = (GridView) findViewById(R.id.gridView);
		gridViewAdapter = new GridAdapter(Upload.this);
		gridView.setAdapter(gridViewAdapter);

		imageButton = new ImageButton(Upload.this);
		gridViewAdapter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						Upload.this);
				builder.setTitle("选择照片");
				builder.setPositiveButton("相机",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										"android.media.action.IMAGE_CAPTURE");
								startActivityForResult(intent, 0);
							}
						});
				builder.setNegativeButton("相册",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent, 1);
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	/**
	 * setSpinner: 设置相册名称的spinner
	 */
	private void setSpinner() {
		mTView = (TextView) findViewById(R.id.group_value);
		spinnerList = new ArrayList<String>();
		for (int i = 0; i < listItems.size(); i++) {
			spinnerList.add(listItems.get(i).get("abName").toString());
		}
		spinnerAdapter = new SpinnerAdapter(this, spinnerList);
		mTView.setText((CharSequence) spinnerAdapter.getItem(0));
		spinnerLayout = (LinearLayout) findViewById(R.id.group_spinner);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		spinnerLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(spinnerLayout, mTView);
			}
		});
	}

	protected void onResume() {
		super.onResume();
	}

	/**
	 * showWindow: 设置自定义spinner的弹窗
	 * 
	 * @param position
	 * @param txt
	 */
	public void showWindow(View position, final TextView txt) {
		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.spinner_window, null);
		listView = (ListView) layout.findViewById(R.id.listview);
		listView.setAdapter(spinnerAdapter);
		popupWindow = new PopupWindow(position);
		// 设置弹窗宽度
		popupWindow.setWidth(spinnerLayout.getWidth());
		popupWindow.setHeight((layout2.getHeight()) / 2);
		// 设置透明背景，不然无法实现点击弹窗外弹框消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击弹窗外部弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置
		popupWindow.showAsDropDown(position, 0, 0);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
			}
		});
		// listview item的点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				txt.setText(spinnerList.get(arg2));// 设置所选的item为下拉框的标题
				albumId = listItems.get(arg2).get("albumId").toString();
				Toast.makeText(Upload.this, albumId, Toast.LENGTH_SHORT).show();
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ContentResolver resolver = getContentResolver();
		/**
		 * 如果不拍照 或者不选择图片返回 不执行任何操作
		 */

		if (data != null) {
			/**
			 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法
			 * ， 所以为了区别到底选择了那个方式获取图片要进行判断
			 * ，这里的requestCode跟startActivityForResult里面第二个参数对应 1== 相册 0 ==相机
			 */
			if (requestCode == 1) {

				try {
					// 获得图片的uri
					Uri originalUri = data.getData();
					// 将图片内容解析成字节数组
					mContent = readStream(resolver.openInputStream(Uri
							.parse(originalUri.toString())));
					// 将字节数组转换为ImageView可调用的Bitmap对象
					bitmap = getPicFromBytes(mContent, null);
					// //把得到的图片绑定在控件上显示
					// imageView.setImageBitmap(myBitmap);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			} else if (requestCode == 0) {

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					System.out.println("sd卡可用");
					return;
				}
				Bundle bundle = data.getExtras();
				bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			}
			Upload.this.gridViewAdapter.addImage(bitmap);
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

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
