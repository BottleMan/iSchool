package org.ischool.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ischool.mod.ClassIfo;
import org.ischool.mod.ClassRoomIfo;
import org.ischool.mod.MsgInfo;
import org.ischool.mod.ResultIfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParase
{
	public JsonParase()
	{
		
	}
	
	/**
	 * 解析新闻的Json数据，返回一个新闻的列表数据
	 * @param jsonArray
	 * @return
	 */
	public List<HashMap<String, String>> ParaseNewsArray(JSONArray jsonArray)
	{
		List<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();
		try
		{
			for (int i = 0; i < jsonArray.length(); i++)
			{
				//解析获得的Json数据，放入List中生成新的ClassRoomIfo实例，然后加入到其List中
				JSONObject temp = jsonArray.getJSONObject(i);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("newslist_item_title", temp.getString("Title"));
				hashMap.put("newslist_item_digest", temp.getString("Digest"));
				hashMap.put("newslist_item_ptime", temp.getString("Ptime"));
				hashMap.put("newslist_item_source", temp.getString("Source"));
				hashMap.put("newslist_item_body", temp.getString("Body"));
				newsList.add(hashMap);
			}
		} catch (Exception e)
		{
			Log.i("http", "Parase news " + e.toString());
			return null;
		}
		
		return newsList;
	}
	
	
	/**
	 * 解析Json格式的数据，返回一个List
	 * 
	 * @return 返回一个所有的教室信息组成的List
	 */
	public List<ClassRoomIfo> ParaseClassroomIfo(String retStr2)
	{
		List<ClassRoomIfo> classRoomIfosTemp = new ArrayList<ClassRoomIfo>();
		try
		{
			JSONArray jsonArray = new JSONArray(retStr2);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				//解析获得的Json数据，放入List中生成新的ClassRoomIfo实例，然后加入到其List中
				JSONObject temp = jsonArray.getJSONObject(i);
				if (temp == null)
				{
					continue;
				}
				List<String> list = new ArrayList<String>();
				list.add(temp.getString("Num"));
				list.add(temp.getString("Name"));
				list.add(temp.getString("Area"));
				list.add(temp.getString("Type"));
				list.add(temp.getString("Volume"));
				list.add(temp.getString("Time"));
				list.add(temp.getString("State"));
				list.add(temp.getString("Belong"));
				list.add(temp.getString("PowerNum"));
				list.add(temp.getString("NetType"));
				list.add(temp.getString("AirCondition"));
				ClassRoomIfo classRoomIfo = new ClassRoomIfo(list);
				classRoomIfosTemp.add(classRoomIfo);
			}
			return classRoomIfosTemp;

		} catch (Exception e)
		{
			Log.i("http", "JsonParase " + e.toString());
			return null;
		}
	}

	
	public List<MsgInfo> ParaseMsgInfo(String retStr)
	{
		List<MsgInfo> msgInfos = new ArrayList<MsgInfo>();
		try
		{
			JSONArray jsonArray = new JSONArray(retStr);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				MsgInfo msgInfo = new MsgInfo();
				msgInfo.setMsgID(jsonObject.getString("MsgID"));
				msgInfo.setMsgTitle(jsonObject.getString("MsgTitle"));
				msgInfo.setMsgType(jsonObject.getString("MsgType"));
				msgInfo.setContent(jsonObject.getString("Content"));
				msgInfo.setUserName(jsonObject.getString("UserID"));
				msgInfo.setLatitude(Double.valueOf(jsonObject.getString("Latitude")));
				msgInfo.setLongitude(Double.valueOf(jsonObject.getString("Longitude")));
				msgInfo.setPostTime(jsonObject.getString("PostTime"));
				msgInfo.setRadius(Integer.valueOf(jsonObject.getString("Radius")));
				
				msgInfos.add(msgInfo);
			}
		} catch (Exception e)
		{
			Log.i("Parase MsgInfo", e.toString());
		}
		return msgInfos;
	}
	
	/**
	 * 解析Json格式的数据，返回ResultIfor的List
	 * @param retString
	 * @return
	 */
	public List<ResultIfo> ParaseResultIfo(String retString)
	{
		List<ResultIfo> resultIfos = new ArrayList<ResultIfo>();
		try
		{
			JSONArray jsonArray = new JSONArray(retString);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				List<String> list = new ArrayList<String>();
				
				list.add(jsonObject.getString("Subject"));
				list.add(jsonObject.getString("Point"));
				list.add(jsonObject.getString("Result"));
				list.add(jsonObject.getString("SClass"));
				list.add(jsonObject.getString("SCode"));
				list.add(jsonObject.getString("InsSubject"));
				list.add(jsonObject.getString("InsSCode"));
				list.add(jsonObject.getString("InsPoint"));
				list.add(jsonObject.getString("InsResult"));
				
				ResultIfo resultIfo = new ResultIfo(list);
				resultIfos.add(resultIfo);
			}
			return resultIfos;
			
		} catch (JSONException e)
		{
			Log.i("http", "parase result " + e.toString());
			return null;
		}
	}

	/**
	 * 解析Json格式的数据，返回ClassIfoR格式的List
	 * @param retString
	 * @return
	 */
	public List<ClassIfo> ParaseClassIfo(String retString)
	{
		List<ClassIfo> allClassIfo = new ArrayList<ClassIfo>();
		try
		{
			JSONArray jsonArray = new JSONArray(retString);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				List<String> list = new ArrayList<String>();
				list.add(jsonObject.getString("Name"));
				list.add(jsonObject.getString("Time"));
				list.add(jsonObject.getString("Place"));
				list.add(jsonObject.getString("Duration"));
				list.add(jsonObject.getString("Teacher"));
				list.add(jsonObject.getString("ComClass"));
				list.add(jsonObject.getString("StuClass"));
				list.add(jsonObject.getString("Name2"));
				list.add(jsonObject.getString("Time2"));
				list.add(jsonObject.getString("Place2"));
				list.add(jsonObject.getString("Duration2"));
				list.add(jsonObject.getString("Teacher2"));
				list.add(jsonObject.getString("ComClass2"));
				list.add(jsonObject.getString("StuClass2"));
				ClassIfo classIfo = new ClassIfo(list);
				allClassIfo.add(classIfo);
			}
			return allClassIfo;
		} catch (Exception e)
		{
			Log.i("http", "parase classIfo " + e.toString());
			return null;
		}
		
	}
}
