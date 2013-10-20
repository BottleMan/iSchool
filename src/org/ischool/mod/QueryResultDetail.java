package org.ischool.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.R;
import org.ischool.config.JsonParase;
import org.ischool.config.UrlConfig;
import org.ischool.util.ResultItemAdapter;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryResultDetail
{
	private ListView listView;
	private Button btnRefresh;
	private List<ResultIfo> resultIfos;
	private String userName;
	private String pwd;
	private String url;
	private String param;
	private RelativeLayout mRelativeLayout;
	private Context mContext;

	private static final int NETERROR = 0;
	private static final int OK = 1;
	private static final int NOINFO = 2;

	public QueryResultDetail(Context pContext, String pParam, RelativeLayout pRelativeLayout)
	{
		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		param = pParam;
		url = UrlConfig.URL;

		listView = (ListView) mRelativeLayout.findViewById(R.id.result_detail_list);
		btnRefresh = (Button) mRelativeLayout.findViewById(R.id.result_detail_refresh);
		btnRefresh.setOnClickListener(btnOnClickListener);

		ResultAsync resultAsync = new ResultAsync();
		resultAsync.execute(url, param);
	}

	private class ResultAsync extends AsyncTask<String, Integer, Integer>
	{
		ProgressDialog dialog;

		@Override
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(mContext, "请稍等", "正在获取数据……", true, false);
			btnRefresh.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			return GetResultData(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			dialog.dismiss();
			//如果获取数据成功，则显示ListView，并载入数据，否则，显示刷新按钮
			switch (result)
			{
			case OK:
				listView.setVisibility(View.VISIBLE);
				SetList();
				break;

			case NETERROR:
				btnRefresh.setVisibility(View.VISIBLE);
				Toast.makeText(mContext, "获取数据错误，请刷新或稍后再试", Toast.LENGTH_SHORT).show();
				break;

			case NOINFO:
				Toast.makeText(mContext, "没有找到需要的数据", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 获得成绩的信息
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private Integer GetResultData(String url, String params)
	{
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			String retString = "";
			retString = syncHttp.httpGet(url, params);
			if (retString.equals(""))
			{
				return NETERROR;
			}
			if (retString.equals("-1"))
			{
				return NOINFO;
			}
			JsonParase jsonParase = new JsonParase();
			resultIfos = jsonParase.ParaseResultIfo(retString);
			return OK;

		} catch (Exception e)
		{
			Log.i("http", "resultDetail " + e.toString());
			return NETERROR;
		}
	}

	/**
	 * 设置成绩的列表
	 */
	private void SetList()
	{
		List<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
		try
		{
			for (int i = 0; i < resultIfos.size(); i++)
			{
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("subject", resultIfos.get(i).GetSubject());
				hashMap.put("point", resultIfos.get(i).GetPoint());
				if (resultIfos.get(i).GetResult().equals(""))
				{
					hashMap.put("result", "/");

				} else if (resultIfos.get(i).GetResult().equals("1.0"))
				{
					hashMap.put("result", "通过");
				} else
				{
					hashMap.put("result", resultIfos.get(i).GetResult());
				}

				mData.add(hashMap);
			}
		} catch (Exception e)
		{
			Log.i("result", "result detail " + e.toString());
			Toast.makeText(mContext, "获取数据错误", Toast.LENGTH_SHORT).show();
		}

		ResultItemAdapter resultItemAdapter = new ResultItemAdapter(mContext, mData, R.layout.query_result_detail_item, new String[]
		{ "subject", "point", "result" }, new int[]
		{ R.id.result_detail_item_subject, R.id.result_detail_item_point, R.id.result_detail_item_result });

		listView.setAdapter(resultItemAdapter);
		listView.setOnItemClickListener(listItemListener);

	}

	private OnClickListener btnOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			ResultAsync resultAsync = new ResultAsync();
			resultAsync.execute(url, userName, pwd);
		}
	};

	private OnItemClickListener listItemListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.result_detail_dialog);
			dialog.setTitle("成绩详细信息");
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			dialogWindow.setAttributes(lp);

			ResultIfo resultIfo = resultIfos.get(position);
			TextView mClass = (TextView) dialog.findViewById(R.id.class_txt);
			TextView subj = (TextView) dialog.findViewById(R.id.subj_txt);
			TextView code = (TextView) dialog.findViewById(R.id.code_txt);
			TextView point = (TextView) dialog.findViewById(R.id.point_txt);
			TextView result = (TextView) dialog.findViewById(R.id.result_txt);
			TextView insSubj = (TextView) dialog.findViewById(R.id.ins_subj_txt);
			TextView insCode = (TextView) dialog.findViewById(R.id.ins_code_txt);
			TextView insPoint = (TextView) dialog.findViewById(R.id.ins_point_txt);
			TextView insResult = (TextView) dialog.findViewById(R.id.ins_result_txt);

			try
			{
				mClass.setText(resultIfo.GetSClass());
				subj.setText(resultIfo.GetSubject());
				code.setText(resultIfo.GetSCode());
				point.setText(resultIfo.GetPoint());

				if (resultIfo.GetResult().equals(""))
				{
					result.setText("/");
				} else if (resultIfo.GetResult().equals("1.0"))
				{
					result.setText("通过");
				} else
				{
					result.setText(resultIfo.GetResult());
				}

				if (resultIfo.GetInsSubject().equals(""))
				{
					insSubj.setText("/");
				} else
				{
					insSubj.setText(resultIfo.GetInsSubject());
				}

				if (resultIfo.GetInsCode().equals(""))
				{
					insCode.setText("/");
				} else
				{
					insCode.setText(resultIfo.GetInsCode());
				}

				if (resultIfo.GetInsPoint().equals(""))
				{
					insPoint.setText("/");
				} else
				{
					insPoint.setText(resultIfo.GetInsPoint());
				}
				if (resultIfo.GetInsResult().equals(""))
				{
					insResult.setText("/");
				} else
				{
					insResult.setText(resultIfo.GetInsResult());
				}

			} catch (Exception e)
			{
				Log.i("dialog", "result dialog " + e.toString());
			}

			dialog.show();

		}
	};
}
