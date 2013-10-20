package org.ischool.util;

/**
 * 
 * @author Bottle
 * 新闻类型的类
 *
 */
public class NewsCategory
{
	private int cid; // 新闻类型编号
	private String title;//新闻类型名称
	
	public NewsCategory()
	{
		super();
	}

	public NewsCategory(int pCid, String pTitle)
	{
		super();
		this.cid = pCid;
		this.title = pTitle;
	}
	
	public void setCid(int pCid)
	{
		this.cid = pCid;
	}
	
	public void setTitle(String pTitle)
	{
		this.title = pTitle;
	}
	
	public int getCid()
	{
		return this.cid;
	}
	
	public String getTitle()
	{
		return this.title;
	}
}
