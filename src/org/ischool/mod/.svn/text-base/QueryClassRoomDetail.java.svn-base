package org.ischool.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.R;
import org.ischool.config.JsonParase;
import org.ischool.config.UrlConfig;
import org.ischool.util.ClassRoomItemAdapter;
import org.ischool.web.SyncHttp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class QueryClassRoomDetail
{

	private static final int SUCCESS = 0;
	private static final int NETERROR = 1;
	private String param;
	private String url;
	private List<ClassRoomIfo> classRoomIfos;
	private List<HashMap<String, String>> roomIfoList;
	private SimpleAdapter adapter;

	private AsyncClassroom asyncClassroom;

	private ListView listView;
	private Button refreshButton;

	private RelativeLayout mRelativeLayout;
	private LinearLayout mDetaiLayout;

	private Context mContext;

	public QueryClassRoomDetail(Context pContext, String pParam, RelativeLayout pRelativeLayout, LinearLayout pRelativeLayout2)
	{
		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		mDetaiLayout = pRelativeLayout2;

		mRelativeLayout.addView(mDetaiLayout);

		classRoomIfos = new ArrayList<ClassRoomIfo>();
		listView = (ListView) mRelativeLayout.findViewById(R.id.query_classroom_detail_list);
		try
		{
			refreshButton = (Button) mRelativeLayout.findViewById(R.id.classroom_detail_refresh2);
			refreshButton.setOnClickListener(refreshListener);
		} catch (Exception e)
		{
			Log.i("classroom detail", e.toString());
		}

		//得到上一页面传递过来的参数
		param = pParam;
		url = UrlConfig.URL;

		asyncClassroom = new AsyncClassroom();
		asyncClassroom.execute(url, param);

	}

	private OnClickListener refreshListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			asyncClassroom = new AsyncClassroom();
			asyncClassroom.execute(url, param);
		}
	};

	private OnItemClickListener listListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.classroom_detail_dialog);
			dialog.setTitle("教室详细信息");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			ClassRoomIfo classRoomIfo = classRoomIfos.get(position);

			TextView detail_Num = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_num_txt);
			TextView detail_Name = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_name_txt);
			TextView detail_Type = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_type_txt);
			TextView detail_Belong = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_belong_txt);
			TextView detail_Area = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_area_txt);
			TextView detail_Volume = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_volume_txt);
			TextView detail_State = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_state_txt);
			TextView detail_powerNum = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_powerNum_txt);
			TextView detail_netType = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_netType_txt);
			TextView detail_airCondition = (TextView) dialog.findViewById(R.id.classroom_detail_dialog_airCondition_txt);
			
			try
			{
				detail_Num.setText(classRoomIfo.GetNum());
				detail_Name.setText(classRoomIfo.GetName());
				if (classRoomIfo.GetState().equals("0"))//根据返回值判断教室是否可用
				{
					detail_State.setText("可用");
				} else
				{
					detail_State.setText("不可用");
				}
				//detail_Type.setText(classRoomIfo.GetType());
				//detail_Time.setText(classRoomIfo.GetTime().toCharArray(), 0, 8);//时间只显示前面的月日年部分
				if (classRoomIfo.GetBelong().equals(""))
				{
					detail_Belong.setText("无");
				} else
				{
					detail_Belong.setText(classRoomIfo.GetBelong());
				}
				detail_Area.setText(classRoomIfo.GetArea());
				detail_Volume.setText(classRoomIfo.GetVolume());
				detail_Type.setText(classRoomIfo.GetType());
				detail_powerNum.setText(classRoomIfo.GetPowerNum());
				
				if (classRoomIfo.GetNetType().equals("1"))
				{
					detail_netType.setText("无线网");
				} else
				{
					detail_netType.setText("有线网");
				}
				
				if (classRoomIfo.GetAirCondition().equals("1"))
				{
					detail_airCondition.setText("有");
				}
				else {
					detail_airCondition.setText("无");
				}
				

			} catch (Exception e)
			{
				Log.i("dialog", e.toString());
			}

			dialog.show();
		}
	};

	private class AsyncClassroom extends AsyncTask<String, Integer, Integer>
	{

		ProgressDialog dialog;

		@Override
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(mContext, "请稍等", "正在获取数据……", true, false);
			refreshButton.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return getClassroomData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			dialog.dismiss();
			switch (result)
			{
			case SUCCESS:
				refreshButton.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				SetListView();
				break;

			case NETERROR:
				refreshButton.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				Toast.makeText(mContext, "网络错误，请刷新重试", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	private void SetListView()
	{
		roomIfoList = new ArrayList<HashMap<String, String>>();

		if (classRoomIfos.isEmpty())
		{
			Toast.makeText(mContext, "没有查到需要的教室", Toast.LENGTH_SHORT).show();
		} else
		{
			for (int i = 0; i < classRoomIfos.size(); i++)
			{
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("name", classRoomIfos.get(i).GetName());
				hashMap.put("area", classRoomIfos.get(i).GetArea());
				hashMap.put("type", classRoomIfos.get(i).GetType());
				hashMap.put("volume", classRoomIfos.get(i).GetVolume());
				hashMap.put("num", classRoomIfos.get(i).GetNum());
				hashMap.put("state", classRoomIfos.get(i).GetState());
				hashMap.put("time", classRoomIfos.get(i).GetTime());
				hashMap.put("belong", classRoomIfos.get(i).GetBelong());
				hashMap.put("powerNum", classRoomIfos.get(i).GetPowerNum());
				hashMap.put("netType", classRoomIfos.get(i).GetNetType());
				hashMap.put("airCondition", classRoomIfos.get(i).GetAirCondition());
				roomIfoList.add(hashMap);
			}

			adapter = new ClassRoomItemAdapter(mContext, roomIfoList, R.layout.query_class_room_detail_item, new String[]
			{ "name", "area", "type", "volume" }, new int[]
			{ R.id.txt_classroomName, R.id.txt_classroomArea, R.id.txt_classroomType, R.id.txt_classroomVolume });
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listListener);
		}

	}

	public Integer getClassroomData(String pUrl, String pParam)
	{
		//获取网络数据
		SyncHttp syncHttp = new SyncHttp();

		try
		{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(pUrl, pParam);

			if (retStr.equals(""))
			{
				return NETERROR;
			} else
			{
				//解析返回的json数据
				JsonParase jsonParase = new JsonParase();
				classRoomIfos = jsonParase.ParaseClassroomIfo(retStr);
				return SUCCESS;
			}

		} catch (Exception e)
		{
			Log.i("http", "classroon detail " + e.toString());
			return NETERROR;
		}
	}
}
