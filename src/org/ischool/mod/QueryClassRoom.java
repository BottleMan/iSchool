package org.ischool.mod;

import org.ischool.R;
import org.ischool.util.MyList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class QueryClassRoom
{
	private MyList yearList;
	private MyList seasonList;
	private MyList weekList;
	private MyList dayList;
	private MyList areaList;
	private MyList classList;
	private MyList typeList;

	private Spinner yearSpinner;
	private Spinner seasonSpinner;
	private Spinner weekSpinner;
	private Spinner daySpinner;
	private Spinner areaSpinner;
	private Spinner classSpinner;
	private Spinner typeSpinner;

	private ArrayAdapter<String> yearAdapter;
	private ArrayAdapter<String> seasonAdapter;
	private ArrayAdapter<String> weekAdapter;
	private ArrayAdapter<String> dayAdapter;
	private ArrayAdapter<String> areaAdapter;
	private ArrayAdapter<String> classAdapter;
	private ArrayAdapter<String> typeAdapter;

	private String yearString = "";
	private String seasonString = "";
	private String weekString = "";
	private String dayString = "";
	private String areaString = "";
	private String classString = "";
	private String typeString = "";

	private Button btnConfirm;

	private Context mContext;
	private LinearLayout mClassRoomLayout;
	private RelativeLayout mSearchLayout;
	private LinearLayout mDetailLayout;
	private LinearLayout mChoiceLayout;
	private LinearLayout mAdvancedLayout;
	private Button btnQuick;
	private Button btnAdvanced;

	public QueryClassRoom(Context pContext, RelativeLayout pSearchLayout, LinearLayout plLayout, LinearLayout pRelativeLayout2)
	{
		mContext = pContext;
		mClassRoomLayout = plLayout;
		mSearchLayout = pSearchLayout;
		mDetailLayout = pRelativeLayout2;

		mSearchLayout.setVisibility(View.VISIBLE);

		mSearchLayout.removeAllViews();
		mSearchLayout.addView(mClassRoomLayout);

		mChoiceLayout = (LinearLayout) mSearchLayout.findViewById(R.id.search_classroom_choose);
		mAdvancedLayout = (LinearLayout) mSearchLayout.findViewById(R.id.search_classroom_advance);
		mChoiceLayout.setVisibility(View.VISIBLE);
		mAdvancedLayout.setVisibility(View.GONE);

		btnQuick = (Button) mSearchLayout.findViewById(R.id.search_classroom_btn_quick);
		btnAdvanced = (Button) mSearchLayout.findViewById(R.id.search_classroom_btn_advance);

		btnQuick.setOnClickListener(btnQuickListener);
		btnAdvanced.setOnClickListener(btnAdvancedListener);

	}

	private OnClickListener btnQuickListener = new OnClickListener()
	{

		@SuppressWarnings("unused")
		@Override
		public void onClick(View v)
		{
			mSearchLayout.setVisibility(View.VISIBLE);
			mSearchLayout.removeView(mClassRoomLayout);
			QuickQueryClassRoomDetail queryClassRoomDetail = new QuickQueryClassRoomDetail(mContext, mSearchLayout, mDetailLayout);

		}
	};

	private OnClickListener btnAdvancedListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			mChoiceLayout.setVisibility(View.GONE);
			mAdvancedLayout.setVisibility(View.VISIBLE);

			Initialization();

			//查询按钮的定义和监听
			btnConfirm = (Button) mClassRoomLayout.findViewById(R.id.btnConfirm);
			btnConfirm.setOnClickListener(new OnClickListener()
			{

				@SuppressWarnings("unused")
				@Override
				public void onClick(View v)
				{

					String param = "action=" + "query_classroom" + "&year=" + yearString + "&season=" + seasonString + "&week=" + weekString + "&day=" + dayString + "&area=" + areaString + "&class="
							+ classString + "&type=" + typeString;
					mSearchLayout.setVisibility(View.VISIBLE);
					mSearchLayout.removeView(mClassRoomLayout);
					QueryClassRoomDetail queryClassRoomDetail = new QueryClassRoomDetail(mContext, param, mSearchLayout, mDetailLayout);
				}
			});

		}
	};

	/**
	 * 初始化
	 */
	private void Initialization()
	{
		yearList = new MyList("Year", "config/Config_Classroom_Year.xml", mContext);
		seasonList = new MyList("Season", "config/Config_Classroom_Season.xml", mContext);
		weekList = new MyList("Week", "config/Config_Classroom_Week.xml", mContext);
		dayList = new MyList("Day", "config/Config_Classroom_Day.xml", mContext);
		areaList = new MyList("Area", "config/Config_Classroom_Area.xml", mContext);
		classList = new MyList("Class", "config/Config_Classroom_Class.xml", mContext);
		typeList = new MyList("Type", "config/Config_Classroom_Type.xml", mContext);

		yearSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerYear);
		seasonSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerSeason);
		weekSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerWeek);
		daySpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerDay);
		areaSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerArea);
		classSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerClass);
		typeSpinner = (Spinner) mSearchLayout.findViewById(R.id.spinnerType);

		AdapterDefine();

	}

	/**
	 * 定义adapter，为适配器设置下拉列表的样式，将适配器添加到列表上
	 */
	private void AdapterDefine()
	{
		//定义
		yearAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, yearList.list);
		seasonAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, seasonList.list);
		weekAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, weekList.list);
		dayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, dayList.list);
		areaAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, areaList.list);
		classAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, classList.list);
		typeAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, typeList.list);
		//设置样式
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将适配器添加到列表上
		try
		{
			yearSpinner.setAdapter(yearAdapter);
			seasonSpinner.setAdapter(seasonAdapter);
			weekSpinner.setAdapter(weekAdapter);
			daySpinner.setAdapter(dayAdapter);
			areaSpinner.setAdapter(areaAdapter);
			classSpinner.setAdapter(classAdapter);
			typeSpinner.setAdapter(typeAdapter);

			SetListener();
		} catch (Exception e)
		{
			Log.i("query_classroom", e.toString());
		}
	}

	/**
	 * 为spinner设置监听器
	 */
	private void SetListener()
	{
		yearSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				/* 将所选mySpinner 的值带入myTextView 中*/
				//myTextView.setText("您选择的是：" + adapter.getItem(arg2));
				yearString = yearAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		seasonSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				seasonString = seasonAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		weekSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				weekString = weekAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		daySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				dayString = dayAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		areaSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				areaString = areaAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		classSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				classString = classAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

		typeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				typeString = typeAdapter.getItem(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0)
			{
				arg0.setVisibility(View.VISIBLE);
			}
		});

	}

}
