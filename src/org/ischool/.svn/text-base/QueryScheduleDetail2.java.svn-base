package org.ischool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.config.JsonParase;
import org.ischool.config.UrlConfig;
import org.ischool.mod.ClassIfo;
import org.ischool.sqllite.ScheduDBOperation;
import org.ischool.web.SyncHttp;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class QueryScheduleDetail2 extends Activity
{
	private static final String PREF_FILE_NAME = "user_profiles";
	
	private final static int OK = 0;
	private final static int NETERROR = 1;
	
	private String param;
	private String url = UrlConfig.URL;
	private List<ClassIfo> classIfos;
	Button btnRefresh;
	TextView textView;
	ProgressBar progressBar;
	GridView gridView;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_schedule_detail2);
		
		classIfos = new ArrayList<ClassIfo>();

		gridView = (GridView) findViewById(R.id.schedule_gridView);
		progressBar = (ProgressBar) findViewById(R.id.schedule_detail_progressBar);
		textView = (TextView) findViewById(R.id.schedule_detail_getdata);
		btnRefresh = (Button) findViewById(R.id.schedule_detail_refresh);
		btnRefresh.setOnClickListener(refreshClickListener);

		//得到上一页面传递过来的参数
		param = getIntent().getStringExtra("param");

		SyncClassIfo syncClassIfo = new SyncClassIfo();
		syncClassIfo.execute(url, param);
		
	}

	private void SetListView()
	{
		SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
		List<HashMap<String, String>> roomIfoList = new ArrayList<HashMap<String, String>>();
		ContentValues values = new ContentValues();
		ScheduDBOperation operation = new ScheduDBOperation(QueryScheduleDetail2.this);
		boolean scheduleFirstTime = preferences.getBoolean("scheduleFirstTime", true);
		
		//总共应该有30个课程信息
		for (int i = 0; i < classIfos.size(); i++)
		{
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("0_name1", classIfos.get(i).getName());
			hashMap.put("0_teacher1", classIfos.get(i).getPlace());
			hashMap.put("0_name2", classIfos.get(i).getName2());
			hashMap.put("0_teacher2", classIfos.get(i).getPlace2());
		    roomIfoList.add(hashMap);
		    
		    values.put("time", classIfos.get(i).getTime());
			
			values.put("name", classIfos.get(i).getName());
			values.put("place", classIfos.get(i).getPlace());
			values.put("duration", classIfos.get(i).getDuration());
			values.put("teacher", classIfos.get(i).getTeacher());
			values.put("comClass", classIfos.get(i).getComClass());
			values.put("stuClass", classIfos.get(i).getStuClass());
			
			values.put("name2", classIfos.get(i).getName2());
			values.put("place2", classIfos.get(i).getPlace2());
			values.put("duration2", classIfos.get(i).getDuration2());
			values.put("teacher2", classIfos.get(i).getTeacher2());
			values.put("comClass2", classIfos.get(i).getComClass2());
			values.put("stuClass2", classIfos.get(i).getStuClass2());
			
			//第一次插入数据库，以后每次更新数据库
		    if (scheduleFirstTime)
			{
		    	values.put("classID", String.valueOf(i));
				
				operation.InsertDB(values);
			}else {
				String[] classID = new String[1];
				classID[0] = String.valueOf(i);
				operation.UpdateDB(classID, values);
			}
		    operation.CloseDB();
		}
		
		Editor editor = preferences.edit();
		editor.putBoolean("scheduleFirstTime", false);
		editor.commit();

		adapter = new SimpleAdapter(
				this, 
				roomIfoList, 
				R.layout.query_schedule_detail2_item, 
				new String[]
						{ "0_name1", "0_teacher1", "0_name2", "0_teacher2" },
				new int[]
				{ R.id.schedule_mo_className1, R.id.schedule_mo_classTeacher1, 
				  R.id.schedule_mo_className2, R.id.schedule_mo_classTeacher2});
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(gridViewItemListener);
	}

	private class SyncClassIfo extends AsyncTask<String, Integer, Integer>
	{

		@Override
		protected void onPreExecute()
		{
			gridView.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
			btnRefresh.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return GetDataInBackground(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			switch (result)
			{
			case OK:
				//设置ListView
				SetListView();
				gridView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
				btnRefresh.setVisibility(View.GONE);
				break;
				
			case NETERROR:
				gridView.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				textView.setVisibility(View.GONE);
				btnRefresh.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "网络错误，请刷新重试", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	public Integer GetDataInBackground(String pUrl, String pParam)
	{
		//获取网络数据
		SyncHttp syncHttp = new SyncHttp();

		try
		{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(url, param);

			//解析返回的json数据
			JsonParase jsonParase = new JsonParase();
			classIfos = jsonParase.ParaseClassIfo(retStr);
			
			return OK;

		} catch (Exception e)
		{
			Log.i("http", "schedule detail " + e.toString());
			return NETERROR;
		}
	}

	private OnClickListener refreshClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			SyncClassIfo syncClassIfo = new SyncClassIfo();
			syncClassIfo.execute(url, param);
		}
	};

	private OnItemClickListener gridViewItemListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Dialog dialog = new Dialog(QueryScheduleDetail2.this);
			dialog.setContentView(R.layout.schedule_detail_dialog);
			
			dialog.setTitle("课程详细信息");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);
			
			ClassIfo classIfo = classIfos.get(position);
			TextView nameView = (TextView) dialog.findViewById(R.id.name_txt);
			TextView timeView = (TextView) dialog.findViewById(R.id.time_txt);
			TextView placeView = (TextView) dialog.findViewById(R.id.place_txt);
			TextView durationView = (TextView) dialog.findViewById(R.id.duration_txt);
			TextView teacherView = (TextView) dialog.findViewById(R.id.teacher_txt);
			TextView comClassView = (TextView) dialog.findViewById(R.id.com_txt);
			TextView stuClassView = (TextView) dialog.findViewById(R.id.stu_txt);
			
			try
			{
				nameView.setText(classIfo.getName());
				timeView.setText(classIfo.getTime());
				placeView.setText(classIfo.getPlace());
				durationView.setText(classIfo.getDuration());
				teacherView.setText(classIfo.getTeacher());
				comClassView.setText(classIfo.getComClass());
				stuClassView.setText(classIfo.getStuClass());
				
			} catch (Exception e)
			{
				Log.i("dialog", "setDialog " + e.toString());
			}
			
			dialog.show();
		}
	};
}
