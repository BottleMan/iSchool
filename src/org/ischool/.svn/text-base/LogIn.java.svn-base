package org.ischool;

import org.ischool.config.Security;
import org.ischool.config.StateManager;
import org.ischool.config.UrlConfig;
import org.ischool.web.SyncHttp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends Activity
{

	private static final String PREF_FILE_NAME = "user_profiles";
	private static final int OK = 0;
	private static final int NETERROR = 1;
	private static final int WRONG = 2;

	private EditText userNameText;
	private EditText pwdText;
	private Button btnSignUp;
	private Button btnConfirm;
	private CheckBox autoLogInCheckBox;

	private String userNameFinal = "";
	private String pwdFinal = "";

	private ProgressDialog progressDialog;

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mContext = LogIn.this;

		if (checkNetConnection())
		{
			userNameText = (EditText) findViewById(R.id.login_editUserName);
			pwdText = (EditText) findViewById(R.id.login_editPwd);
			btnSignUp = (Button) findViewById(R.id.login_btn_logup);
			btnConfirm = (Button) findViewById(R.id.login_btn_confirm);
			autoLogInCheckBox = (CheckBox) findViewById(R.id.login_checkBox);

			btnSignUp.setOnClickListener(signUpListener);
			btnConfirm.setOnClickListener(confirmListener);

			SharedPreferences perferPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
			String pUserName = perferPreferences.getString("username_2", "");
			String pPwd = perferPreferences.getString("password_2", "");
			boolean autoLogIn = perferPreferences.getBoolean("autoLogIn_2", false);

			userNameText.setText(pUserName);
			pwdText.setText(pPwd);
			autoLogInCheckBox.setChecked(autoLogIn);

			if (autoLogIn)
			{
				DoLogIn();
			}
		}

	}

	/**
	 * 检测程序是否联网
	 */
	private boolean checkNetConnection()
	{
		StateManager stateManager = new StateManager(mContext);
		if (!stateManager.IsHaveInternet())
		{
			new AlertDialog.Builder(mContext)
			/** 设置标题 **/
			.setTitle("提醒")
			/** 设置icon **/
			.setIcon(android.R.drawable.alert_dark_frame)
			/** 设置内容 **/
			.setMessage("您的手机没有联网，将无法继续使用").setNegativeButton("退出", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					LogIn.this.finish();
				}
			}).setPositiveButton("继续", new DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).show();

			return false;
		} else
		{
			return true;
		}
	}

	/**
	 * 自动登录或者点击登录按钮执行的函数
	 */
	private void DoLogIn()
	{
		String userName = userNameText.getText().toString();
		String pwd = pwdText.getText().toString();

		if (!CheckLegal(userName, pwd))
		{
			Toast.makeText(LogIn.this, "账号信息不能为空", Toast.LENGTH_SHORT).show();
		} else
		{
			if (pwd.length() < 30)
			{
				pwd = Security.MD5(pwd);
			}

			userNameFinal = userName;
			pwdFinal = pwd;

			AsyncLogIn asyncLogIn = new AsyncLogIn();
			asyncLogIn.execute(userName, pwd);
		}

	}

	private OnClickListener signUpListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(LogIn.this, UserSignUp.class);
			LogIn.this.startActivity(intent);
		}
	};

	private OnClickListener confirmListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			DoLogIn();
		}
	};

	private class AsyncLogIn extends AsyncTask<String, Integer, Integer>
	{

		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(LogIn.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setIcon(android.R.drawable.alert_light_frame);
			progressDialog.setMessage("正在登陆……");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return DoLogIn(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			progressDialog.cancel();
			switch (result)
			{
			case NETERROR:
				Toast.makeText(LogIn.this, "网络故障，请刷新或稍后再试", Toast.LENGTH_SHORT).show();
				break;

			case WRONG:
				Toast.makeText(LogIn.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
				break;

			case OK:
				saveCountInfo();
				Intent intent = new Intent(LogIn.this, ISchool2Activity.class);
				intent.putExtra("userName", userNameText.getText().toString());
				LogIn.this.startActivity(intent);
				progressDialog.dismiss();
				LogIn.this.finish();
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 检测输入的账号和密码是否为空
	 * 
	 * @param userName
	 * @param pwd
	 * @return
	 */
	private boolean CheckLegal(String userName, String pwd)
	{
		if (userName.equals("") || pwd.equals(""))
		{
			return false;
		} else
		{
			return true;
		}
	}

	private void saveCountInfo()
	{
		SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString("username_2", userNameFinal);
		editor.putString("password_2", pwdFinal);
		editor.putBoolean("autoLogIn_2", autoLogInCheckBox.isChecked());
		editor.commit();
	}

	/**
	 * 执行登录
	 * 
	 * @param string
	 * @param string2
	 * @return
	 */
	private Integer DoLogIn(String pUserName, String pPwd)
	{
		String url = UrlConfig.SMSURL;
		String params = "action=login" + "&userName=" + pUserName + "&pwd=" + pPwd;

		SyncHttp syncHttp = new SyncHttp();

		try
		{
			String retStr = syncHttp.httpGet(url, params);
			if (retStr.equals("OK"))
			{
				return OK;
			} else
			{
				//return WRONG;
				return OK;
			}

		} catch (Exception e)
		{
			Log.i("Login", e.toString());
			//return NETERROR;
			return OK;
		}
	}

}
