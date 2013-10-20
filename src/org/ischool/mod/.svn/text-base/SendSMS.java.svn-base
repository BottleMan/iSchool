package org.ischool.mod;

import java.util.ArrayList;
import java.util.List;

import org.ischool.R;
import org.ischool.config.GPSServiceListener;
import org.ischool.config.UrlConfig;
import org.ischool.web.SyncHttp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SendSMS
{
	private static final String PREF_FILE_NAME = "user_profiles";
	private static final int OK = 1;
	private static final int WRONG = 0;
	private static final int NETERROR = 2;

	private Spinner mySpinner;
	private Spinner toSpinner;
	private EditText contentText;
	private EditText titleText;
	private Button btnSend;

	private Context mContext;
	private LinearLayout mLinearLayout;
	private RelativeLayout mRelativeLayout;

	private static ProgressDialog progressDialog;

	private int radius;
	private int smsType;
	private String userName;
	private String latitude;
	private String longitude;
	private String smsTitle;
	private String smsContent;

	public SendSMS(Context pContext, RelativeLayout pRelativeLayout, LinearLayout pLinearLayout)
	{

		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		mLinearLayout = pLinearLayout;
		mRelativeLayout.removeAllViews();
		mRelativeLayout.addView(mLinearLayout);

		SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new GPSServiceListener(userName, "", mContext);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null)
		{
			double pLatitude = location.getLatitude(); //经度
			double pLongitude = location.getLongitude(); //纬度
			latitude = String.valueOf(pLatitude);
			longitude = String.valueOf(pLongitude);
		} else
		{
			latitude = preferences.getString("latitude", "38");
			longitude = preferences.getString("longitude", "120");
		}

		userName = preferences.getString("username_2", "");
		radius = 500;
		smsType = 0;

		contentText = (EditText) mLinearLayout.findViewById(R.id.sms_Send_editTextSMSContent);
		titleText = (EditText) mLinearLayout.findViewById(R.id.sms_Send_editTextSMSTitle);
		btnSend = (Button) mLinearLayout.findViewById(R.id.sms_send_confirm);
		mySpinner = (Spinner) mLinearLayout.findViewById(R.id.spinner1);
		toSpinner = (Spinner) mLinearLayout.findViewById(R.id.spinner2);

		btnSend.setOnClickListener(btnSendListener);

		setRangeSpinner();
		setToSpinner();

	}

	/**
	 * 发送按钮的监听
	 */
	private OnClickListener btnSendListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			smsTitle = titleText.getText().toString();
			smsContent = contentText.getText().toString();

			if (checkContentLeagle())
			{
				AsyncSend asyncSend = new AsyncSend();
				asyncSend.execute("");
			}
		}
	};

	/**
	 * 检测输入的内容是否合法
	 * 
	 * @return
	 */
	private boolean checkContentLeagle()
	{

		if (smsTitle.equals(""))
		{
			Toast.makeText(mContext, "标题名称不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (smsContent.equals(""))
		{
			Toast.makeText(mContext, "消息内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private class AsyncSend extends AsyncTask<String, Integer, Integer>
	{

		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(mContext, "发送中……", "正在发送……", true, false);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			String url = UrlConfig.SMSURL;
			String param = "action=sendMsg" + "&userName=" + userName + "&msgType=" + smsType + "&msgTitle=" + smsTitle + "&content=" + smsContent + "&latitude=" + latitude + "&longitude="
					+ longitude + "&radius=" + radius;
			SyncHttp syncHttp = new SyncHttp();

			try
			{
				String retStr = syncHttp.httpGet(url, param);
				if (retStr.equals("OK"))
				{
					return OK;
				} else
				{
					return WRONG;
				}

			} catch (Exception e)
			{
				Log.i("sendSMS", e.toString());
				return NETERROR;
			}
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			progressDialog.dismiss();
			switch (result)
			{
			case OK:
				titleText.setText("");
				contentText.setText("");
				Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
				break;

			case WRONG:
				Toast.makeText(mContext, "发送失败，请刷新重试", Toast.LENGTH_SHORT).show();
				break;

			case NETERROR:
				Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 设置选择发送范围的spinner
	 */
	private void setRangeSpinner()
	{
		//第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
		List<String> list = new ArrayList<String>();
		list.add(mContext.getString(R.string.select_500));
		list.add(mContext.getString(R.string.select_1000));
		list.add(mContext.getString(R.string.select_2000));

		//第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);

		//第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//第四步：将适配器添加到下拉列表上
		mySpinner.setAdapter(adapter);

		//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				if (arg2 == 0)
				{
					radius = 500;
				}
				if (arg2 == 1)
				{
					radius = 1000;
				}
				if (arg2 == 2)
				{
					radius = 2000;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 设置选择发送对象的spinner
	 */
	private void setToSpinner()
	{
		//第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
		List<String> list = new ArrayList<String>();
		list.add("发送给所有人");
		list.add("仅发送给好友");

		//第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);

		//第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//第四步：将适配器添加到下拉列表上
		toSpinner.setAdapter(adapter);

		//第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
		toSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				if (arg2 == 0)
				{
					smsType = 1;
				}else {
					smsType = 1;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});
	}
}
