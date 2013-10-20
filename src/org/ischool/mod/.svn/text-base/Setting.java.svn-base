package org.ischool.mod;

import org.ischool.LogIn;
import org.ischool.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Setting
{
	private static final String PREF_FILE_NAME = "user_profiles";
	private Context mContext;
	private RelativeLayout mRelativeLayout;
	private ScrollView mSettingLayout;
	private LinearLayout mAboutLayout;
	private TextView mSettingText;
	private TextView mAboutText;

	private CheckBox checkBox;
	private LinearLayout clearLayout;
	private LinearLayout logoutLayout;
	private LinearLayout aboutLayout;
	private LinearLayout msgCountLayout;
	private LinearLayout minTimeLayout;
	private LinearLayout alarmTimeLayout;
	private TextView sysServiceTextView;
	private int mHour, mMinute;

	public Setting(Context pContext, RelativeLayout pRelativeLayout, ScrollView pSettingLayout, LinearLayout pAboutLayout, TextView pSettingView, TextView pAboutView)
	{
		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		mSettingLayout = pSettingLayout;
		mAboutLayout = pAboutLayout;
		mSettingText = pSettingView;
		mAboutText = pAboutView;

		mRelativeLayout.removeAllViews();
		mRelativeLayout.addView(mSettingLayout);

		checkBox = (CheckBox) mRelativeLayout.findViewById(R.id.set_checkBox);
		msgCountLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_msg_count);
		minTimeLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_minTime);
		alarmTimeLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_alarmTime);
		clearLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_clearLayout);
		logoutLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_logoutLayout);
		aboutLayout = (LinearLayout) mRelativeLayout.findViewById(R.id.set_aboutLayout);
		sysServiceTextView = (TextView) mRelativeLayout.findViewById(R.id.textView8);

		SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		boolean checked = preferences.getBoolean("doService", true);
		checkBox.setChecked(checked);

		if (checked)
		{
			sysServiceTextView.setText("已开启");
		} else
		{
			sysServiceTextView.setText("已关闭");
		}

		checkBox.setOnCheckedChangeListener(toggleListener);
		msgCountLayout.setOnClickListener(msgCountLayoutListener);
		minTimeLayout.setOnClickListener(minTimeLayoutListener);
		alarmTimeLayout.setOnClickListener(alarmTimeLayoutListener);
		clearLayout.setOnClickListener(btnClearListener);
		logoutLayout.setOnClickListener(btnlogOutListener);
		aboutLayout.setOnClickListener(btnAboutLitener);
	}
	
	/**
	 * 设置课程提醒时间的监听
	 */
	private OnClickListener alarmTimeLayoutListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			final Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.setting_alarm_time);
			dialog.setTitle("修改提醒时间");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
			Button btnConfirm = (Button) dialog.findViewById(R.id.setting_msg_num_btnConfirm);
			Button btnBack = (Button) dialog.findViewById(R.id.setting_msg_num_btnCancel);

			final SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
			mHour = preferences.getInt("hour", 7);
			mMinute = preferences.getInt("minute", 30);
			dialog.show();
			
			timePicker.setOnTimeChangedListener(timePickerListener);
			
			btnConfirm.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Editor editor = preferences.edit();
					editor.putInt("hour", mHour);
					editor.putInt("minute", mMinute);
					editor.commit();
					dialog.dismiss();
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
				}
			});
			
			btnBack.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
		}
		
		private OnTimeChangedListener timePickerListener = new OnTimeChangedListener()
		{

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
			{
				mHour = hourOfDay;
				mMinute = minute;
			}
			
		};
	};
	
	/**
	 * 设置修改同步时间的监听
	 */
	private OnClickListener minTimeLayoutListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			final Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.setting_msg_num);
			dialog.setTitle("输入时间 单位：分钟");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			final EditText editText = (EditText) dialog.findViewById(R.id.setting_msg_num_edit);
			Button btnConfirm = (Button) dialog.findViewById(R.id.setting_msg_num_btnConfirm);
			Button btnBack = (Button) dialog.findViewById(R.id.setting_msg_num_btnCancel);

			final SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
			long minTime = preferences.getLong("minTime", 120000);
			String minTimeText = String.valueOf(minTime/60000);
			editText.setText(minTimeText);
			editText.selectAll();
			
			dialog.show();
			
			btnConfirm.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Editor editor = preferences.edit();
					long minTime2 = Long.valueOf(editText.getText().toString());
					minTime2 = minTime2 * 60000;
					editor.putLong("minTime", minTime2);
					editor.commit();
					dialog.dismiss();
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
				}
			});
			
			btnBack.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
		}
	};

	/**
	 * 设置消息数量的监听
	 */
	private OnClickListener msgCountLayoutListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			final Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.setting_msg_num);
			dialog.setTitle("输入修改的数量");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			final EditText editText = (EditText) dialog.findViewById(R.id.setting_msg_num_edit);
			Button btnConfirm = (Button) dialog.findViewById(R.id.setting_msg_num_btnConfirm);
			Button btnBack = (Button) dialog.findViewById(R.id.setting_msg_num_btnCancel);

			final SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
			String num = preferences.getString("msg_num", "5");
			editText.setText(num);
			editText.selectAll();
			
			dialog.show();
			
			btnConfirm.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					Editor editor = preferences.edit();
					editor.putString("msg_num", editText.getText().toString());
					editor.commit();
					dialog.dismiss();
					Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
				}
			});
			
			btnBack.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
		}
	};

	/**
	 * 关于按钮的监听
	 */
	private OnClickListener btnAboutLitener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			mRelativeLayout.removeAllViews();
			mRelativeLayout.addView(mAboutLayout);
			mSettingText.setVisibility(View.GONE);
			mAboutText.setVisibility(View.VISIBLE);
		}
	};

	/**
	 * 注销按钮的监听
	 */
	private OnClickListener btnlogOutListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			new AlertDialog.Builder(mContext)
			/** 设置标题 **/
			.setTitle("注销")
			/** 设置icon **/
			.setIcon(android.R.drawable.ic_dialog_alert)
			/** 设置内容 **/
			.setMessage("是否确定注销？").setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
				{
					/** 关闭窗口 **/
					SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putBoolean("autoLogIn_2", false);
					editor.commit();
					Intent intent = new Intent(mContext, LogIn.class);
					mContext.startActivity(intent);
				}
			}).show();
		}
	};

	/**
	 * 清除按钮的监听
	 */
	private OnClickListener btnClearListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			new AlertDialog.Builder(mContext)
			/** 设置标题 **/
			.setTitle("清除")
			/** 设置icon **/
			.setIcon(android.R.drawable.ic_dialog_alert)
			/** 设置内容 **/
			.setMessage("是否确定清除数据？").setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
				{
					SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("username_1", "");
					editor.putString("password_1", "");
					editor.putBoolean("checked_1", false);
					editor.putBoolean("autoIn_1", false);
					editor.commit();
					Toast.makeText(mContext, "清除数据成功", Toast.LENGTH_SHORT).show();
				}
			}).show();
		}
	};

	/**
	 * 开关系统服务的监听
	 */
	private OnCheckedChangeListener toggleListener = new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			if (isChecked)
			{
				editor.putBoolean("doService", true);
				sysServiceTextView.setText("已开启");
				Toast.makeText(mContext, "开启系统服务", Toast.LENGTH_SHORT).show();
			} else
			{
				editor.putBoolean("doService", false);
				sysServiceTextView.setText("已关闭");
				Toast.makeText(mContext, "关闭系统服务", Toast.LENGTH_SHORT).show();
			}
			editor.commit();
		}
	};
}
