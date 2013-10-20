package org.ischool.util;

import java.util.List;
import java.util.Map;

import org.ischool.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * customSimpleAdapter是自己写的一个类，可以实现Adapter中的第一元素的字体颜色和背景显示为已经点击
 * @author Bottle
 *
 */
public class CustomSimpleAdapter extends SimpleAdapter
{

	public CustomSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
	{
		super(context, data, resource, from, to);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = super.getView(position, convertView, parent);
		//更新第一个TextView的背景
		if (position == 0)
		{
			TextView categoryTitle = (TextView) v;
			categoryTitle.setBackgroundResource(R.drawable.ubtn_selected);
			//这里的颜色不能使用R.color.xxxx，不然全是灰的
			categoryTitle.setTextColor(0xFF0000FF);
		}
		return v;
	}
}
