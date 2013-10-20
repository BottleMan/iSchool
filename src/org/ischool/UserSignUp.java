package org.ischool;

import java.util.ArrayList;
import java.util.List;

import org.ischool.config.Security;
import org.ischool.config.UrlConfig;
import org.ischool.web.SyncHttp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserSignUp extends Activity
{
	private RelativeLayout relativeLayout;
	private EditText userNameText;
	private EditText nameText;
	private EditText pwdText;
	private EditText pwdText2;
	private EditText emailText;
	private EditText telNumText;
	private Spinner spinner;
	private Button btnConfirm;
	private Button btnCancel;
	private ProgressBar progressBar;
	private TextView textView;

	private String url;
	private String sex;

	private final static int PWDLONG = 0; //密码过长
	private final static int PWDSHORT = 1;//密码过短
	private final static int USERNAME = 2;//用户名重复
	private final static int ISEMPTY = 3;//必填位置出现空缺
	private final static int PWDERROR = 4;//两次输入的密码不相同
	private final static int OK = 5;//没问题
	private final static int NETERROR = 6;//网络问题

	AsyncSignup asyncSignup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_signup);

		url = UrlConfig.SMSURL;

		relativeLayout = (RelativeLayout) findViewById(R.id.signup_relative);
		userNameText = (EditText) findViewById(R.id.signup_userName);
		pwdText = (EditText) findViewById(R.id.signup_pwd);
		pwdText2 = (EditText) findViewById(R.id.signup_pwd2);
		nameText = (EditText) findViewById(R.id.signup_name);
		emailText = (EditText) findViewById(R.id.signup_email);
		telNumText = (EditText) findViewById(R.id.signup_telnum);
		spinner = (Spinner) findViewById(R.id.signup_spinner);
		btnConfirm = (Button) findViewById(R.id.signup_btn_confirm);
		btnCancel = (Button) findViewById(R.id.signup_btn_cancel);
		progressBar = (ProgressBar) findViewById(R.id.signup_progressBar);
		textView = (TextView) findViewById(R.id.signup_txt);

		List<String> list = new ArrayList<String>();
		list.add("保密");
		list.add("男");
		list.add("女");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserSignUp.this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(spinnerListener);

		//设置默认性别
		sex = "保密";

		btnConfirm.setOnClickListener(confirmListener);
		btnCancel.setOnClickListener(cancelListener);
	}

	private OnClickListener confirmListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			String userName = userNameText.getText().toString();
			String pwd = pwdText.getText().toString();
			String pwd2 = pwdText2.getText().toString();
			String nickName = nameText.getText().toString();
			String email = emailText.getText().toString();
			String telNum = telNumText.getText().toString();
			String userSex = "";

			//设置性别
			if (sex.equals("男"))
			{
				userSex = "0";
			}
			if (sex.equals("女"))
			{
				userSex = "1";
			}
			if (sex.equals("保密"))
			{
				userSex = "2";
			}

			//检查输入的内容是否合法
			switch (CheckInfoLegal(userName, pwd, pwd2, nickName, userSex))
			{
			case PWDLONG:
				Toast.makeText(UserSignUp.this, "输入的密码长度过长，请重新输入", Toast.LENGTH_SHORT).show();
				SetView(0);
				break;

			case PWDSHORT:
				Toast.makeText(UserSignUp.this, "输入的密码长度太短，请重新输入", Toast.LENGTH_SHORT).show();
				SetView(0);
				break;

			case PWDERROR:
				Toast.makeText(UserSignUp.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
				SetView(0);
				break;

			case ISEMPTY:
				Toast.makeText(UserSignUp.this, "必填内容不能为空", Toast.LENGTH_SHORT).show();
				SetView(2);
				break;

			case OK:

				String pwdMD5 = Security.MD5(pwd);

				String params = "action=signup" + "&userName=" + userName + "&pwd=" + pwdMD5 + "&nickName=" + nickName + "&email=" + email + "&telNum=" + telNum + "&sex=" + userSex;

				asyncSignup = new AsyncSignup();
				asyncSignup.execute(params);

				break;
			default:
				break;
			}
		}
	};

	private OnClickListener cancelListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			UserSignUp.this.finish();
		}
	};

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener()
	{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			sex = parent.getAdapter().getItem(position).toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
		}
	};

	private class AsyncSignup extends AsyncTask<String, Integer, Integer>
	{

		@Override
		protected void onPreExecute()
		{
			relativeLayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return GetSignupResult(params[0]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			switch (result)
			{
			case USERNAME:
				Toast.makeText(UserSignUp.this, "用户名已经被使用，请重新输入", Toast.LENGTH_SHORT).show();
				SetView(1);
				break;

			case NETERROR:
				Toast.makeText(UserSignUp.this, "网络错误，请重试或稍后继续", Toast.LENGTH_SHORT).show();
				SetView(2);
				break;

			case OK:
				SetView(3);
				SetDialog(userNameText.getText().toString(), nameText.getText().toString());
				break;
			default:
				break;
			}

		}

	}

	public Integer GetSignupResult(String params)
	{
		SyncHttp syncHttp = new SyncHttp();

		try
		{
			String retStr = syncHttp.httpGet(url, params);
			if (retStr.equals("OK"))
			{
				return OK;
			}
			if (retStr.equals("UserName Repetition"))
			{
				return USERNAME;
			}

			return NETERROR;
		} catch (Exception e)
		{
			Log.i("Sign up", "Sign up " + e.toString());
			return NETERROR;
		}
	}

	public void SetDialog(String userName, String nickName)
	{
		new AlertDialog.Builder(UserSignUp.this)
		/** 设置标题 **/
		.setTitle("欢迎加入校园通")
		/** 设置icon **/
		.setIcon(android.R.drawable.alert_dark_frame)
		/** 设置内容 **/
		.setMessage("您的账号：" + userName + "您的昵称：" + nickName)

		.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				/** 关闭窗口 **/
				UserSignUp.this.finish();
			}
		}).show();

	}

	public void SetView(int flag)
	{
		switch (flag)
		{
		case 0://将密码的内容置空
			pwdText.setText("");
			pwdText2.setText("");
			relativeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			break;

		case 1://将用户名置空
			userNameText.setText("");
			relativeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			break;

		case 2://不做改变
			relativeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			break;

		case 3://全不显示
			relativeLayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private int CheckInfoLegal(String userName2, String pwd, String pwd2, String nickName, String userSex)
	{
		//检测输入内容是否有空
		if (userName2.equals("") || pwd.equals("") || pwd2.equals("") || nickName.equals(""))
		{
			return ISEMPTY;
		}
		//两次输入的密码不相同
		if (!pwd.equals(pwd2))
		{
			return PWDERROR;
		}
		//输入的密码过长
		if (pwd.length() > 15)
		{
			return PWDLONG;
		}
		//输入的密码过短
		if (pwd.length() < 6)
		{
			return PWDSHORT;
		}

		return OK;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_CALL:
			return true;
		case KeyEvent.KEYCODE_SYM:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_STAR:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
