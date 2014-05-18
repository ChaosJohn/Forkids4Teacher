package com.huaijv.forkids4teacher.viewElems;

import java.util.ArrayList;
import java.util.List;

import com.huaijv.forkids4teacher.R;
import com.huaijv.forkids4teacher.utils.ImageUtils;
import com.huaijv.forkids4teacher.utils.JsonUtils;
import com.huaijv.forkids4teacher.utils.OtherUtils;

import android.R.raw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	private List<Bitmap> list = null;
	private Context context = null;
	private LayoutInflater inflater = null;
	private OnClickListener onClickListener = null;
	private List<String> bitmapStringList = null;


	public GridAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		list = new ArrayList<Bitmap>();		
		list.add(BitmapFactory.decodeResource(this.context.getResources(),
				R.drawable.add));
		bitmapStringList = new ArrayList<String>();
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.imageview_item, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageview);
			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}
		imageView.setImageBitmap(list.get(position));
		imageView.setOnClickListener(onClickListener);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(100, 100);
		imageView.setLayoutParams(params);
		if (position == list.size() - 1) {
			imageView.setClickable(true);
		} else {
			imageView.setClickable(false);
		}
		return convertView;
	}

	public void addImage(Bitmap bitmap) {
		list.add(list.size() - 1, bitmap);
		bitmapStringList.add(OtherUtils.getBitmapStrBase64(bitmap));
		notifyDataSetChanged();
	}
	
	public List<String> getBitmapStrBase64List() {
		return bitmapStringList;
	}
	
	public void clearAll() {
		for (int i = 0; i < bitmapStringList.size(); i++) {
			bitmapStringList.remove(i); 
			list.remove(i);
		}
		notifyDataSetChanged();
	}

}
