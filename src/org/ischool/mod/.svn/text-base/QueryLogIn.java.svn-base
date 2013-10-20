package org.ischool.mod;

import org.ischool.QueryScheduleDetail2;
import org.ischool.R;
import org.ischool.config.Security;
import org.ischool.config.UrlConfig;
import org.ischool.web.SyncHttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class QueryLogIn extends Activity
{
	private static final String PREF_FILE_NAME = "user_profiles";
	private String mAction;
	private Context mContext;
	private RelativeLayout mRelativeLayout;
	private LinearLayout mLonInLayout;
	private RelativeLayout mResultLayout;

	private EditText userNameEditText;
	private EditText userPwdEditText;
	private Button button;
	private CheckBox checkBox;
	private CheckBox checkBox2;
	

	private final static int CORRECT = 0;
	private final static int WRONG = 1;
	private final static int NETERROR = 2;
	
	private String url;
	private String userName;

	private String pwdMD5;

	public QueryLogIn(Context pContext, RelativeLayout pRelativeLayout, LinearLayout pLoninLayout, RelativeLayout pResultLayout, String pAction)
	{
		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		mLonInLayout = pLoninLayout;
		mResultLayout = pResultLayout;
		mAction = pAction;
		
		pRelativeLayout.removeAllViews();
		pRelativeLayout.addView(mLonInLayout);

		//从配置文件中取出用户名和密码，并设置到EditText中
		SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		String username = preferences.getString("username_1", "");
		String password = preferences.getString("password_1", "");
		boolean checked = preferences.getBoolean("checked_1", false);
		boolean checked2 = preferences.getBoolean("autoIn_1", false);
		

		userNameEditText = (EditText) mRelativeLayout.findViewById(R.id.result_UserID);
		userPwdEditText = (EditText) mRelativeLayout.findViewById(R.id.result_UserPWD);
		checkBox = (CheckBox) mRelativeLayout.findViewById(R.id.result_checkBox);
		checkBox2 = (CheckBox) mRelativeLayout.findViewById(R.id.result_checkBox_autoLogin);

		checkBox.setChecked(checked);
		checkBox2.setChecked(checked2);
		userNameEditText.setText(username);
		userPwdEditText.setText(password);

		button = (Button) mRelativeLayout.findViewById(R.id.result_btn);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				autoLogIn();
			}
		});

		userNameEditText.setVisibility(View.VISIBLE);
		userPwdEditText.setVisibility(View.VISIBLE);
		checkBox.setVisibility(View.VISIBLE);
		checkBox2.setVisibility(View.VISIBLE);
		button.setVisibility(View.VISIBLE);

		if (checked2)
		{
			autoLogIn();
		}
	}

	private void autoLogIn()
	{
		userName = userNameEditText.getText().toString();
		pwdMD5 = userPwdEditText.getText().toString();

		//如果密码以加密就直接将密码赋给pwdMD5，否则，加密后赋给pwdMD5
		if (pwdMD5.length() < 30)
		{
			pwdMD5 = Security.MD5(userPwdEditText.getText().toString());
		}

		if (userName != null && !userName.equals("") && pwdMD5 != null && !pwdMD5.equals(""))
		{
			//保存账户信息
			saveCountIfo(userName, pwdMD5);

			url = UrlConfig.URL;
			LogInAsync logInAsync = new LogInAsync();
			logInAsync.execute(url, userName, pwdMD5);

		} else
		{ //如果账号信息填写不全，不跳转，并弹出提示
			Toast.makeText(mContext, "账号信息不完整，请重新输入", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 保存用户的账号信息
	 * 
	 * @param userName
	 * @param pwd
	 */
	protected void saveCountIfo(String userName, String pwd)
	{
		SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		if (checkBox.isChecked())
		{
			editor.putString("username_1", userName);
			editor.putString("password_1", pwd);
			editor.putBoolean("checked_1", true);
			editor.putBoolean("autoIn_1", checkBox2.isChecked());
		} else
		{
			editor.putString("username_1", "");
			editor.putString("password_1", "");
			editor.putBoolean("checked_1", false);
			editor.putBoolean("autoIn_1", false);
		}
		editor.commit();
	}

	/**
	 * 执行异步任务的类
	 */
	private class LogInAsync extends AsyncTask<String, Integer, Integer>
	{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(mContext, "请稍等", "正在登陆……", true, false);
			//将之前显示的控件消除
			userNameEditText.setVisibility(View.GONE);
			userPwdEditText.setVisibility(View.GONE);
			checkBox.setVisibility(View.GONE);
			checkBox2.setVisibility(View.GONE);
			button.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return LogIn(params[0], params[1], params[2]);
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(Integer result)
		{
			dialog.dismiss();
			switch (result)
			{
			case CORRECT:
				String userName = userNameEditText.getText().toString();
				//String pwd = userPwdEditText.getText().toString();
				String pwd = pwdMD5;

				if (mAction.equals("result"))
				{
					String mParam = "action=result" + "&userName=" + userName + "&pwd=" + pwd;
					mRelativeLayout.removeView(mLonInLayout);
					mRelativeLayout.addView(mResultLayout);
					//setClassDetail(mParam);
					QueryResultDetail queryResultDetail = new QueryResultDetail(mContext, mParam, mResultLayout);
				} else
				{
					Intent intent = new Intent(mContext, QueryScheduleDetail2.class);
					String mParam = "action=schedule" + "&userName=" + userName + "&pwd=" + pwd;
					intent.putExtra("param", mParam);
					mContext.startActivity(intent);
				}

				break;

			case WRONG:
				userNameEditText.setVisibility(View.VISIBLE);
				userPwdEditText.setVisibility(View.VISIBLE);
				checkBox.setVisibility(View.VISIBLE);
				checkBox2.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);

				Toast.makeText(mContext, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
				break;

			case NETERROR:
				userNameEditText.setVisibility(View.VISIBLE);
				userPwdEditText.setVisibility(View.VISIBLE);
				checkBox.setVisibility(View.VISIBLE);
				checkBox2.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);

				Toast.makeText(mContext, "网络错误，请重新登陆", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	}

	/**
	 * 登陆
	 * 
	 * @param url
	 * @param userName
	 * @param pwd
	 * @return 验证正确，账号或密码错误，网络错误
	 */
	public Integer LogIn(String url, String userName, String pwd)
	{
		String param = "action=query_login" + "&userName=" + userName + "&pwd=" + pwd;
		SyncHttp syncHttp = new SyncHttp();

		try
		{
			String result = syncHttp.httpGet(url, param);
			if (result.equals("ok"))
			{
				return CORRECT;
			} else
			{
				return WRONG;
			}
		} catch (Exception e)
		{
			Log.i("http", "query_log_in" + e.toString());
			return NETERROR;
		}
	}
}
