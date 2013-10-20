package org.ischool.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.R;
import org.ischool.config.UrlConfig;
import org.ischool.web.SyncHttp;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyMsg
{
	private static final String PREF_FILE_NAME = "user_profiles";
	private final int SUCCESS = 0;//加载成功
	private int NEWSCOUNT; //返回新闻数目
	private final int NONEWS = 1;//该栏目下没有新闻
	private final int NOMORENEWS = 2;//该栏目下没有更多新闻
	private final int LOADERROR = 3;//加载失败

	private LoadNewsAsyncTask loadNewsAsyncTask;
	private Button mLoadMoreBtn;
	private ArrayList<HashMap<String, String>> mNewsData;
	private List<MsgInfo> msgInfos;
	private ListView mNewsList;
	private SimpleAdapter newsListAdapter;
	
	private String userName;

	private Context mContext;
	private RelativeLayout mRelativeLayout;
	private RelativeLayout mSmsLayout;
	private LinearLayout mMsgDetaiLayout;

	public MyMsg(Context pContext, RelativeLayout pSMSLayout, RelativeLayout pRelativeLayout, LinearLayout pMsgDetaiLayout)
	{

		mContext = pContext;
		mRelativeLayout = pRelativeLayout;
		mSmsLayout = pSMSLayout;
		mMsgDetaiLayout = pMsgDetaiLayout;
		
		mSmsLayout.removeAllViews();
		mSmsLayout.addView(mRelativeLayout);

		msgInfos = new ArrayList<MsgInfo>();

		SharedPreferences preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		userName = preferences.getString("username_2", "");
		String count = preferences.getString("msg_num", "5");
		NEWSCOUNT = Integer.valueOf(count);

		mNewsData = new ArrayList<HashMap<String, String>>();
		mNewsList = (ListView) mRelativeLayout.findViewById(R.id.sms_my_msg_list);
		mNewsList.setVisibility(View.VISIBLE);

		setListView();

	}

	/**
	 * 设置ListView，包括从通过网络取数据到本地,然后将内容填充到ListView中
	 */
	private void setListView()
	{
		//获取指定栏目的新闻列表
		//getSpeCateNews(mCid, mNewsData, 0, true);
		newsListAdapter = new SimpleAdapter(mContext, mNewsData, R.layout.news_list_item, new String[]
		{ "newslist_item_title", "newslist_item_source", "newslist_item_ptime" }, new int[]
		{ R.id.newslist_item_title, R.id.newslist_item_source, R.id.newslist_item_ptime });

		//loadMoreLayout = mInflater.inflate(R.layout.loadmore, null);
		
		//mNewsList.addFooterView(loadMoreLayout);

		mNewsList.setAdapter(newsListAdapter);
		
		
		//设置监听
		mNewsList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				mSmsLayout.removeAllViews();
				mSmsLayout.addView(mMsgDetaiLayout);
				
				TextView newsTitle = (TextView) mSmsLayout.findViewById(R.id.news_detail_content_newsTitle);
				TextView newsResource = (TextView) mSmsLayout.findViewById(R.id.news_detail_content_newsResource);
				TextView newsTime = (TextView) mSmsLayout.findViewById(R.id.news_detail_content_newsTime);
				TextView newsBody = (TextView) mSmsLayout.findViewById(R.id.news_detail_content_newsBody);
				Button btnBack = (Button) mSmsLayout.findViewById(R.id.btn_back);
				
				newsBody.setText(msgInfos.get(position).getContent());
				newsTime.setText(msgInfos.get(position).getPostTime());
				newsTitle.setText(msgInfos.get(position).getMsgTitle());
				newsResource.setText("作者：" + msgInfos.get(position).getUserName());
				
				btnBack.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						mSmsLayout.removeAllViews();
						mSmsLayout.addView(mRelativeLayout);
						
					}
				});
			}
		});

		mLoadMoreBtn = (Button) mRelativeLayout.findViewById(R.id.sms_my_msg_loadMore_btn);
		mLoadMoreBtn.setOnClickListener(loadMoreListener);

		loadNewsAsyncTask = new LoadNewsAsyncTask();
		loadNewsAsyncTask.execute(0, true);

	}

	/**
	 * “加载更多”按钮的监听
	 */
	private OnClickListener loadMoreListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			loadNewsAsyncTask = new LoadNewsAsyncTask();
			//获取该栏目下新闻
			//getSpeCateNews(mCid,mNewsData,mNewsData.size(),false);
			//通知ListView进行更新
			//mNewsListAdapter.notifyDataSetChanged();
			loadNewsAsyncTask.execute(mNewsData.size() - 1, false);

		}
	};

	/**
	 * 用于实现异步更新的内部类
	 * 
	 * @author Bottle
	 */
	private class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(mContext, "请稍等", "正在获取数据……", true, false);
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			return getSpeCateNews(mNewsData, (Integer) params[0], (Boolean) params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//根据返回值显示相关的Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(mContext, "您还没有发过消息", Toast.LENGTH_SHORT).show();
				break;
			case NOMORENEWS:
				Toast.makeText(mContext, "已经加载全部", Toast.LENGTH_SHORT).show();
				break;
			case LOADERROR:
				Toast.makeText(mContext, "加载失败，请刷新重试", Toast.LENGTH_SHORT).show();
				break;
			}

			progressDialog.dismiss();
			//通知新闻的列表进行更新
			newsListAdapter.notifyDataSetChanged();
			Log.i("mNewsData", mNewsData.toString());
		}
	}

	/**
	 * 获取指定类型的新闻列表
	 * 
	 * @param newsList
	 *            保存新闻信息的集合
	 * @param startnid
	 *            分页
	 * @param firstTimes
	 *            是否第一次加载
	 */
	private int getSpeCateNews(List<HashMap<String, String>> newsList, int startnid, Boolean firstTimes)
	{
		if (firstTimes)
		{
			//如果是第一次，则清空集合里数据
			newsList.clear();
		}
		//请求URL和字符串
		String url = UrlConfig.SMSURL;
		String params = "action=allMyData" + "&userName=" + userName + "&startnid=" + startnid + "&count=" + NEWSCOUNT;
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			//以Get方式请求，并获得返回结果
			String retStr = syncHttp.httpGet(url, params);

			if (retStr.equals(""))
			{
				if (firstTimes)
				{
					return NONEWS;
				} else
				{
					return NOMORENEWS;
				}
			}

			JSONArray jsonArray = new JSONArray(retStr);

			if (jsonArray.length() > 0)
			{

				//				JsonParase jsonParase = new JsonParase();
				//				newsList = jsonParase.ParaseNewsArray(jsonArray);
				/**
				 * 这里的函数只要一封装到其他的类中就会失败，不知道什么原因
				 */
				for (int i = 0; i < jsonArray.length(); i++)
				{
					//解析获得的Json数据，放入List中生成新的ClassRoomIfo实例，然后加入到其List中
					JSONObject temp = jsonArray.getJSONObject(i);

					MsgInfo msgInfo = new MsgInfo();
					msgInfo.setContent(temp.getString("Content"));
					msgInfo.setLatitude(Double.valueOf(temp.getString("Latitude")));
					msgInfo.setLongitude(Double.valueOf(temp.getString("Longitude")));
					msgInfo.setMsgID(temp.getString("MsgID"));
					msgInfo.setMsgTitle(temp.getString("MsgTitle"));
					msgInfo.setMsgType(temp.getString("MsgType"));
					msgInfo.setUserName(temp.getString("UserID"));
					msgInfo.setRadius(Integer.valueOf(temp.getString("Radius")));
					msgInfo.setPostTime(temp.getString("PostTime"));
					msgInfos.add(msgInfo);

					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("newslist_item_title", temp.getString("MsgTitle"));
					hashMap.put("newslist_item_ptime", temp.getString("PostTime"));
					
					if (temp.getString("UserID").equals(""))
					{
						hashMap.put("newslist_item_source", "作者：匿名");
					} else
					{
						hashMap.put("newslist_item_source", "作者：" + temp.getString("UserID"));
					}

					newsList.add(hashMap);
				}

				return SUCCESS;
			} else
			{
				if (firstTimes)
				{
					return NONEWS;
				} else
				{
					return NOMORENEWS;
				}
			}
		} catch (Exception e)
		{
			Log.i("http", "news get data " + e.toString());
			return LOADERROR;
		}
	}
}
