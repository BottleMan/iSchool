package org.ischool.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @description
 * 自定义的类，继承自textview，可以同时实现多个跑马灯
 * @author Bottle
 *
 */
public class MyMarquee extends TextView
{

	public MyMarquee(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
	{
		if (focused)
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus)
	{
		if (hasWindowFocus)
			super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public boolean isFocused()
	{
		return true;
	}

}
