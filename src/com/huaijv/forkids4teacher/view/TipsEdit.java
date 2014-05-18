package com.huaijv.forkids4teacher.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.model.GlobalVariables;
import com.huaijv.forkids4teacher.utils.ChildThread;
import com.huaijv.forkids4teacher.utils.ChildThread.WorkForMain;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.NetUtils;
import com.huaijv.forkids4teacher.utils.OtherUtils;

/**
 * TipsEdit[activity]: 育英知识编辑界面(几乎和JournalEdit.java一样)
 * 
 * @author chaos
 * 
 */
public class TipsEdit extends Activity {

	private EditText editText = null;
	private EditText titleEditText = null;
	private LinearLayout header = null;
	private LinearLayout footer = null;
	private List<Map<String, Object>> contentList = null;
	private List<Map<String, Object>> rawList = null;
	private List<String> bitmapStringList = null;
	private Map<String, Object> map = null;
	private int imgIndex = 0;
	private String knId = "";
	private byte[] mContent;
	private Bitmap bitmap;
	private GlobalApplication app = null;
	private String from = null;
	private List<Map<String, Object>> journalContentList = null;
	private ImageView convertImageView = null;

	private Handler mainHandler, childHandler;
	private ChildThread childThread;
	private final int MSG_GET = 0;
	private final int MSG_UPLOAD = 1;
	private final int MSG_ERROR = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips_edit);

		from = this.getIntent().getExtras().getString("from");
		if ("tips_detail".equalsIgnoreCase(from))
			knId = this.getIntent().getExtras().getString("knId");
		app = (GlobalApplication) this.getApplication();
		if (!app.activities.contains(this))
			app.activities.add(this);
		contentList = new ArrayList<Map<String, Object>>();
		bitmapStringList = new ArrayList<String>();
		app.bitmapStringList = new ArrayList<String>();
		app.imgIndex = 0;
		titleEditText = (EditText) findViewById(R.id.titleInput);
		editText = (EditText) findViewById(R.id.contentInput);
		header = (LinearLayout) findViewById(R.id.layout_header);
		footer = (LinearLayout) findViewById(R.id.layout_footer);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.height = GlobalVariables.screenHeight / 10;
		header.setLayoutParams(params);
		params.height = GlobalVariables.screenHeight / 13;
		footer.setLayoutParams(params);

		if ("tips_detail".equalsIgnoreCase(from)) {
			titleEditText.setText(app.journalTitle);
			journalContentList = app.journalContentList;
			for (int i = 0; i < journalContentList.size(); i++) {
				if (journalContentList.get(i).containsKey("img")) {
					bitmap = (Bitmap) journalContentList.get(i).get("bitmap");
					app.bitmapStringList.add(OtherUtils
							.getBitmapStrBase64(bitmap));
					Drawable d = new BitmapDrawable(getResources(), bitmap);
					String indexTag = "<<<" + app.imgIndex + ">>>";
					app.imgIndex++;
					SpannableString ss = new SpannableString(indexTag);
					d.setBounds(0, 0, d.getIntrinsicWidth(),
							d.getIntrinsicHeight());
					ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
					ss.setSpan(span, 0, indexTag.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// 在光标所在处插入图片
					editText.getText().insert(editText.getSelectionStart(),
							"\n");
					editText.getText().insert(editText.getSelectionStart(), ss);
					editText.getText().insert(editText.getSelectionStart(),
							"\n");
				} else {
					editText.getText().insert(editText.getSelectionStart(),
							journalContentList.get(i).get("p").toString());
				}
			}
		}

		findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				app.showProgressBar();
				childHandler.sendEmptyMessage(MSG_UPLOAD);
			}
		});

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				app.activities.remove(this);
				finish();
			}
		});

		findViewById(R.id.camera_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});

		findViewById(R.id.photo_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
			}
		});

		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (MSG_GET == msg.what) {

				} else if (MSG_UPLOAD == msg.what) {
					editText.setText("");
					app.bitmapStringList = new ArrayList<String>();
					app.imgIndex = 0;
					titleEditText.setText("");
					startActivity(new Intent(TipsEdit.this, Tips.class));
				} else if (MSG_ERROR == msg.what) {
					Toast.makeText(TipsEdit.this, "err", Toast.LENGTH_SHORT)
							.show();
				}
				app.cancelProgressBar();
			}
		};

		childThread = new ChildThread(new WorkForMain() {
			@Override
			public void doJob(Message msg) {
				String rev = null;
				if (MSG_GET == msg.what) {
				} else if (MSG_UPLOAD == msg.what) {

					try {
						rev = postData();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (rev.equalsIgnoreCase("err")) {
						mainHandler.sendEmptyMessage(MSG_ERROR);
					} else {
						mainHandler.sendEmptyMessage(MSG_UPLOAD);
					}
				}

			}
		});
		childThread.start();
		childHandler = childThread.getHandler();
	}

	private String postData() throws JSONException {
		String rawContent = editText.getText().toString();
		rawList = OtherUtils.rawContentToList(rawContent);
		for (int i = 0; i < rawList.size(); i++) {
			if (rawList.get(i).containsKey("imgIndex")) {
				int index = Integer.parseInt(rawList.get(i).get("imgIndex")
						.toString());
				map = new HashMap<String, Object>();
				map.put("content", app.bitmapStringList.get(index));
				map.put("type", "image");
				contentList.add(map);
			} else if (rawList.get(i).containsKey("content")) {
				map = new HashMap<String, Object>();
				map.put("content", rawList.get(i).get("content"));
				map.put("type", "text");
				contentList.add(map);
			}
		}
		String title = titleEditText.getText().toString();

		return ("tips_detail".equalsIgnoreCase(from)) ? NetUtils
				.modifyDataByUrl(
						"http://huaijv-sap.eicp.net:8088/forkids/kidnursingknowledges/"
								+ knId, JsonUtils
								.list2JsonObjectStringWithTitle(contentList,
										title), app.getLoginString())
				: NetUtils
						.postDataByUrl(
								"http://huaijv-sap.eicp.net:8088/forkids/kidnursingknowledges/",
								JsonUtils.list2JsonObjectStringWithTitle(
										contentList, title), app
										.getLoginString());
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
					Uri originalUri = data.getData();
					mContent = readStream(resolver.openInputStream(Uri
							.parse(originalUri.toString())));
					bitmap = getPicFromBytes(mContent, null);
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
			app.bitmapStringList.add(OtherUtils.getBitmapStrBase64(bitmap));
			Drawable d = new BitmapDrawable(getResources(), bitmap);
			String indexTag = "<<<" + app.imgIndex + ">>>";
			app.imgIndex++;
			SpannableString ss = new SpannableString(indexTag);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
			ss.setSpan(span, 0, indexTag.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 在光标所在处插入图片
			editText.getText().insert(editText.getSelectionStart(), "\n");
			editText.getText().insert(editText.getSelectionStart(), ss);
			editText.getText().insert(editText.getSelectionStart(), "\n");
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
