package org.ischool.mod;

import java.util.List;

public class MsgInfo
{
	private String msgID;
	private String msgTitle;
	private String msgType;
	private String postTime;
	private String userName;
	private String content;
	private double latitude;
	private double longitude;
	private int radius;

	public MsgInfo()
	{
		this.msgID = "";
		this.msgTitle = "";
		this.msgType = "";
		this.postTime = "";
		this.userName = "";
		this.content = "";
		this.latitude = 0;
		this.longitude = 0;
		this.radius = 0;
	}

	public MsgInfo(List<Object> list)
	{
		this.msgID = list.get(0).toString();
		this.msgTitle = list.get(1).toString();
		this.msgType = list.get(2).toString();
		this.postTime = list.get(3).toString();
		this.userName = list.get(4).toString();
		this.content = list.get(5).toString();
		this.latitude = Double.valueOf(list.get(6).toString());
		this.longitude = Double.valueOf(list.get(7).toString());
		this.radius = Integer.valueOf(list.get(8).toString());
	}

	public String getMsgID()
	{
		return this.msgID;
	}

	public String getMsgTitle()
	{
		return this.msgTitle;
	}

	public String getMsgType()
	{
		return this.msgType;
	}

	public String getPostTime()
	{
		return this.postTime;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	public int getRadius()
	{
		return this.radius;
	}

	public String getUserName()
	{
		return this.userName;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setMsgID(String value)
	{
		this.msgID = value;
	}

	public void setMsgTitle(String value)
	{
		this.msgTitle = value;
	}

	public void setMsgType(String value)
	{
		this.msgType = value;
	}

	public void setUserName(String value)
	{
		this.userName = value;
	}

	public void setContent(String value)
	{
		this.content = value;
	}

	public void setPostTime(String value)
	{
		this.postTime = value;
	}

	public void setLatitude(double value)
	{
		this.latitude = value;
	}

	public void setLongitude(double value)
	{
		this.longitude = value;
	}

	public void setRadius(int value)
	{
		this.radius = value;
	}
}
