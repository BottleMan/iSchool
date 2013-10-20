package org.ischool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.config.ExitApplication;
import org.ischool.config.StateManager;
import org.ischool.mod.ClassIfo;
import org.ischool.mod.MsgAtMe;
import org.ischool.mod.MsgInfo;
import org.ischool.mod.MyMsg;
import org.ischool.mod.QueryClassRoom;
import org.ischool.mod.QueryLogIn;
import org.ischool.mod.SendSMS;
import org.ischool.mod.Setting;
import org.ischool.service.AlarmService;
import org.ischool.service.ISchoolService;
import org.ischool.sqllite.ScheduDBOperation;
import org.ischool.sqllite.UserMsgDBOperation;
import org.ischool.util.CustomSimpleAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ISchool2Activity extends Activity
{
	private SimpleAdapter categroyAdapter;
	private RelativeLayout searchLayout;
	private RelativeLayout msgLayout;
	private RelativeLayout setLayout;

	private TextView settingView;
	private TextView aboutView;

	private LinearLayout searchClassroomLayout;
	private LinearLayout searchClassroomDetail;
	private LinearLayout searchLogInLayout;
	private RelativeLayout searchResultLayout;

	private LinearLayout smsSendLayout;
	private RelativeLayout myMsgLayout;
	private RelativeLayout msgAtMeLayout;
	private LinearLayout msgDetailLayout;

	private ScrollView settingLayout;
	private LinearLayout aboutLayout;

	private LayoutInflater mInflater;

	private String dPosition; //下方菜单栏位置
	private String uPosition; //上方菜单栏位置

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ExitApplication.getInstance().addActivity(this);// 添加Activity到容器中，以便应用完全退出
		
		mInflater = getLayoutInflater();

		boolean noticeCome = getIntent().getBooleanExtra("notification", false);
		String msgID = getIntent().getStringExtra("msgID");

		boolean alarmSchedule = getIntent().getBooleanExtra("alarm", false);
		int index = getIntent().getIntExtra("index", -1);

		initialView();

		if (noticeCome)
		{
			setMsgDialog(msgID);
		} else
			if (alarmSchedule && index != -1)
			{
				setScheduleAlarm(index);
			} else
			{
				startAppService();
			}
	}

	/**
	 * 设置课程信息的提示
	 * 
	 * @param index
	 */
	private void setScheduleAlarm(int index)
	{
		ScheduDBOperation operation = new ScheduDBOperation(ISchool2Activity.this);
		List<ClassIfo> classIfos = operation.FindClassIfosByDayOfWeek();
		if (classIfos.size() == 0)
		{
			Toast.makeText(ISchool2Activity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
		} else
		{
			final Dialog dialog = new Dialog(ISchool2Activity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.schedule_alarm_dialog);
			dialog.setTitle("今日课程（星期" + index + ")");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			TextView unit1Name = (TextView) dialog.findViewById(R.id.unit1_name);
			TextView unit1Place = (TextView) dialog.findViewById(R.id.unit1_place);
			TextView unit2Name = (TextView) dialog.findViewById(R.id.unit2_name);
			TextView unit2Place = (TextView) dialog.findViewById(R.id.unit2_place);
			TextView unit3Name = (TextView) dialog.findViewById(R.id.unit3_name);
			TextView unit3Place = (TextView) dialog.findViewById(R.id.unit3_place);
			TextView unit4Name = (TextView) dialog.findViewById(R.id.unit4_name);
			TextView unit4Place = (TextView) dialog.findViewById(R.id.unit4_place);
			TextView unit5Name = (TextView) dialog.findViewById(R.id.unit5_name);
			TextView unit5Place = (TextView) dialog.findViewById(R.id.unit5_place);

			if (classIfos.get(index - 1).getName().equals(""))
			{
				unit1Name.setText("无");
			} else
			{
				unit1Name.setText("课程名称：" + classIfos.get(0).getName());
				unit1Place.setText("上课地点：" + classIfos.get(0).getPlace());
			}
			if (classIfos.get(index - 1 + 7).getName().equals(""))
			{
				unit2Name.setText("无");
			} else
			{
				unit2Name.setText("课程名称：" + classIfos.get(index - 1 + 7).getName());
				unit2Place.setText("上课地点：" + classIfos.get(index - 1 + 7).getPlace());
			}

			if (classIfos.get(index - 1 + 14).getName().equals(""))
			{
				unit3Name.setText("无");
			} else
			{
				unit3Name.setText("课程名称：" + classIfos.get(index - 1 + 14).getName());
				unit3Place.setText("上课地点：" + classIfos.get(index - 1 + 14).getPlace());
			}

			if (classIfos.get(index - 1 + 21).getName().equals(""))
			{
				unit4Name.setText("无");
			} else
			{
				unit4Name.setText("课程名称：" + classIfos.get(index - 1 + 21).getName());
				unit4Place.setText("上课地点：" + classIfos.get(index - 1 + 21).getPlace());
			}

			if (classIfos.get(index - 1 + 28).getName().equals(""))
			{
				unit5Name.setText("无");
			} else
			{
				unit5Name.setText("课程名称：" + classIfos.get(index - 1 + 28).getName());
				unit5Place.setText("上课地点：" + classIfos.get(index - 1 + 28).getPlace());
			}

			Button btnBack = (Button) dialog.findViewById(R.id.new_msg_dialog_btnConfirm);

			dialog.show();
			btnBack.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
		}

	}

	/**
	 * 显示新信息来的时候的Dialog
	 * 
	 * @param msgID
	 */
	private void setMsgDialog(String pMsgID)
	{
		UserMsgDBOperation dbOperation = new UserMsgDBOperation(ISchool2Activity.this);
		MsgInfo msgInfo = dbOperation.SearchByMsgID(pMsgID);
		if (msgInfo.equals(null))
		{
			Toast.makeText(ISchool2Activity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
		} else
		{
			final Dialog dialog = new Dialog(ISchool2Activity.this);
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.new_msg_dialog);
			dialog.setTitle("新消息");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			TextView titleView = (TextView) dialog.findViewById(R.id.new_msg_dialog_title);
			TextView userIdView = (TextView) dialog.findViewById(R.id.new_msg_dialog_userID);
			TextView timeView = (TextView) dialog.findViewById(R.id.new_msg_dialog_time);
			TextView contentView = (TextView) dialog.findViewById(R.id.new_msg_dialog_content);
			Button btnBack = (Button) dialog.findViewById(R.id.new_msg_dialog_btnConfirm);

			titleView.setText(msgInfo.getMsgTitle());
			userIdView.setText(msgInfo.getUserName());
			timeView.setText(msgInfo.getPostTime());
			contentView.setText(msgInfo.getContent());

			dialog.show();
			btnBack.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});
		}

	}

	/**
	 * 开启程序的后台服务
	 */
	private void startAppService()
	{
		//开启GPRS
		StateManager stateManager = new StateManager(ISchool2Activity.this);
		try
		{
			//stateManager.gprsEnable(true);
			stateManager.OpenGPS();
			Log.i("Open GPS", "GPS Open");
		} catch (Exception e)
		{
			Log.i("Open GPS", e.toString());
		}
		Intent service = new Intent(ISchool2Activity.this, ISchoolService.class);
		ISchool2Activity.this.startService(service);
		Intent alarmService = new Intent(ISchool2Activity.this, AlarmService.class);
		ISchool2Activity.this.startService(alarmService);
	}

	/**
	 * 初始化各个控件，并设置控件的可见性等内容
	 */
	private void initialView()
	{
		searchLayout = (RelativeLayout) findViewById(R.id.mainmenu_search_layout);
		searchLayout.setVisibility(View.VISIBLE);
		msgLayout = (RelativeLayout) findViewById(R.id.mainmenu_msg_layout);
		setLayout = (RelativeLayout) findViewById(R.id.mainmenu_set_layout);
		settingView = (TextView) findViewById(R.id.setting_title);
		settingView.setVisibility(View.GONE);
		aboutView = (TextView) findViewById(R.id.about_title);
		aboutView.setVisibility(View.GONE);

		searchLogInLayout = (LinearLayout) mInflater.inflate(R.layout.query_login, null);
		searchResultLayout = (RelativeLayout) mInflater.inflate(R.layout.query_result, null);
		searchClassroomLayout = (LinearLayout) mInflater.inflate(R.layout.main_search_classroom, null);
		searchClassroomDetail = (LinearLayout) mInflater.inflate(R.layout.main_search_classroom_detail, null);

		smsSendLayout = (LinearLayout) mInflater.inflate(R.layout.sms_send_msg, null);
		myMsgLayout = (RelativeLayout) mInflater.inflate(R.layout.sms_my_msg, null);
		msgAtMeLayout = (RelativeLayout) mInflater.inflate(R.layout.sms_msg_atme, null);
		msgDetailLayout = (LinearLayout) mInflater.inflate(R.layout.sms_msg_detail, null);

		settingLayout = (ScrollView) mInflater.inflate(R.layout.setting, null);
		aboutLayout = (LinearLayout) mInflater.inflate(R.layout.about, null);

		setToolBar();
	}

	/**
	 * 设置下方工具栏
	 */
	private void setToolBar()
	{
		final List<HashMap<String, Integer>> titilebar = new ArrayList<HashMap<String, Integer>>();
		Integer[] newsNameStrings =
		{ R.drawable.btn_null, R.drawable.btn_null, R.drawable.btn_null };

		for (int j = 0; j < 3; j++)
		{
			HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
			hashMap.put("category_title", newsNameStrings[j]);
			titilebar.add(hashMap);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, titilebar, R.layout.toolbar_item, new String[]
		{ "category_title" }, new int[]
		{ R.id.toolbar_image });

		//设置GridView
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		GridView category = new GridView(this);
		category.setColumnWidth(160);//设置单元格的宽度
		category.setNumColumns(3);//设置单元格的行数
		category.setGravity(Gravity.CENTER); //设置GridView的对齐方式
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置selector的背景色为透明，即当点击的时候不出现背景色
		category.setLayoutParams(params);//更新category宽度和高度，这样category在一行显示
		category.setAdapter(adapter);//设置适配器
		//把category放入容器中
		LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.mainmenu_toolbar);
		categoryLayout.addView(category);

		//初始化titlebar，默认是查询信息
		final List<HashMap<String, String>> titilebarInitial = new ArrayList<HashMap<String, String>>();
		String[] searchStrings =
		{ "空教室", "成绩", "课程表" };
		for (int j = 0; j < 3; j++)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("category_title", searchStrings[j]);
			titilebarInitial.add(hashMap);
		}

		dPosition = "0";

		setTitleBar(titilebarInitial);

		//添加单元格点击事件
		category.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ImageView categoryTitle;

				//恢复每个单元格背景色
				for (int i = 0; i < parent.getChildCount(); i++)
				{
					categoryTitle = (ImageView) parent.getChildAt(i);
					categoryTitle.setBackgroundDrawable(null);
				}

				//设置选择单元格的背景色
				categoryTitle = (ImageView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.dbtn_selected);

				final List<HashMap<String, String>> titilebar = new ArrayList<HashMap<String, String>>();

				switch (position)
				{
					case 0: //查询

						dPosition = "0";

						searchLayout.setVisibility(View.VISIBLE);
						msgLayout.setVisibility(View.GONE);
						setLayout.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);

						String[] searchStrings =
						{ "空教室", "成绩", "课程表" };
						for (int j = 0; j < 3; j++)
						{
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("category_title", searchStrings[j]);
							titilebar.add(hashMap);
						}
						setTitleBar(titilebar);
						break;

					case 1: //消息

						dPosition = "1";
						searchLayout.setVisibility(View.GONE);
						msgLayout.setVisibility(View.VISIBLE);
						setLayout.setVisibility(View.GONE);
						settingView.setVisibility(View.GONE);

						String[] msgStrings =
						{ "发送消息", "我的消息", "@我" };
						for (int j = 0; j < 3; j++)
						{
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("category_title", msgStrings[j]);
							titilebar.add(hashMap);
						}
						setTitleBar(titilebar);

						break;

					case 2: //设置
						dPosition = "2";
						searchLayout.setVisibility(View.GONE);
						msgLayout.setVisibility(View.GONE);
						setLayout.setVisibility(View.VISIBLE);
						settingView.setVisibility(View.VISIBLE);
						setSettingTitleBar();
						break;

					default:
						break;
				}
			}
		});

	}

	/**
	 * 设置当点击设置的时候的标题栏
	 */
	protected void setSettingTitleBar()
	{
		LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.maimenu_title_bar);
		categoryLayout.removeAllViews();
		categoryLayout.addView(settingView);
		aboutView.setVisibility(View.GONE);
		categoryLayout.addView(aboutView);

		setMyContentView(uPosition, dPosition);
	}

	/**
	 * 设置上方标题
	 * 
	 */
	private void setTitleBar(List<HashMap<String, String>> titilebar)
	{
		//为gridView创建adapter并加入内容
		//customSimpleAdapter是自己写的一个类，可以实现Adapter中的第一元素的字体颜色和背景显示为已经点击
		categroyAdapter = new CustomSimpleAdapter(ISchool2Activity.this, titilebar, R.layout.titlebar_item,
				new String[]
				{ "category_title" }, new int[]
				{ R.id.titlebar_item });

		//设置GridView
		GridView category = new GridView(ISchool2Activity.this);
		category.setColumnWidth(160);//设置单元格的宽度
		category.setNumColumns(3);//设置单元格的行数
		category.setGravity(Gravity.CENTER); //设置GridView的对齐方式
		category.setSelector(new ColorDrawable(Color.TRANSPARENT));//设置selector的背景色为透明，即当点击的时候不出现背景色

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		category.setLayoutParams(params);//更新category宽度和高度，这样category在一行显示
		//设置适配器
		category.setAdapter(categroyAdapter);
		//把category放入容器中
		LinearLayout categoryLayout = (LinearLayout) findViewById(R.id.maimenu_title_bar);
		categoryLayout.removeAllViews();
		categoryLayout.addView(category);

		uPosition = "0";

		//添加单元格点击事件
		category.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TextView categoryTitle;

				//恢复每个单元格背景色
				for (int i = 0; i < parent.getChildCount(); i++)
				{
					categoryTitle = (TextView) parent.getChildAt(i);
					categoryTitle = (TextView) (parent.getChildAt(i));
					categoryTitle.setBackgroundDrawable(null);
					categoryTitle.setTextColor(0xFFFFFFFF);
				}

				//设置选择单元格的背景色
				categoryTitle = (TextView) (parent.getChildAt(position));
				categoryTitle.setBackgroundResource(R.drawable.ubtn_selected);

				//这里的颜色不能使用R.color.xxxx，不然全是灰的
				categoryTitle.setTextColor(0xFF0000FF);

				switch (position)
				{
					case 0:
						uPosition = "0";
						break;

					case 1:
						uPosition = "1";
						break;

					case 2:
						uPosition = "2";
						break;
					default:
						break;
				}
				setMyContentView(uPosition, dPosition);
			}
		});

		setMyContentView(uPosition, dPosition);
	}

	/**
	 * 根据点击的位置设置不同的场景
	 * 
	 * @param pUpPosition
	 * @param pDownPosition
	 */
	@SuppressWarnings("unused")
	protected void setMyContentView(String pUpPosition, String pDownPosition)
	{
		//查询--空教室
		if (pUpPosition.equals("0") && pDownPosition.equals("0"))
		{
			QueryClassRoom queryClassRoom = new QueryClassRoom(ISchool2Activity.this, searchLayout,
					searchClassroomLayout, searchClassroomDetail);
		}

		//查询--成绩
		if (pUpPosition.equals("1") && pDownPosition.equals("0"))
		{
			QueryLogIn queryLogIn = new QueryLogIn(ISchool2Activity.this, searchLayout, searchLogInLayout,
					searchResultLayout, "result");
		}

		//查询--课程表
		if (pUpPosition.equals("2") && pDownPosition.equals("0"))
		{
			QueryLogIn queryLogIn = new QueryLogIn(ISchool2Activity.this, searchLayout, searchLogInLayout,
					searchResultLayout, "schedule");
		}

		//消息--发送消息
		if (pUpPosition.equals("0") && pDownPosition.equals("1"))
		{
			SendSMS sendSMS = new SendSMS(ISchool2Activity.this, msgLayout, smsSendLayout);
		}

		//消息--我的消息
		if (pUpPosition.equals("1") && pDownPosition.equals("1"))
		{
			MyMsg myMsg = new MyMsg(ISchool2Activity.this, msgLayout, myMsgLayout, msgDetailLayout);
		}

		//消息--@我的
		if (pUpPosition.equals("2") && pDownPosition.equals("1"))
		{
			MsgAtMe msgAtMe = new MsgAtMe(ISchool2Activity.this, msgLayout, msgAtMeLayout, msgDetailLayout);
		}

		//设置
		if (pDownPosition.equals("2"))
		{
			Setting setting = new Setting(ISchool2Activity.this, setLayout, settingLayout, aboutLayout, settingView,
					aboutView);
		}
	}

	/**
	 * 实现“退出确认”
	 */
	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(this).setTitle("退出").setMessage("确定要退出校园通吗？")
				.setIcon(android.R.drawable.ic_dialog_info).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// 点击“确认”后的操作
						//ISchool2Activity.this.finish();
						ExitApplication.getInstance().exit();
						
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						//点击返回后的操作
						dialog.dismiss();
					}
				}).show();
	}

}
