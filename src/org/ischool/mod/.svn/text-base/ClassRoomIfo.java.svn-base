package org.ischool.mod;

import java.util.ArrayList;
import java.util.List;

/**
 * 教室信息的类
 * @author Bottle
 * @description
 * 共计8个属性
 * 包括：编号，名称，分区，类型，容量，使用时间，状态，归属
 */
public class ClassRoomIfo
{
	private String num; //编号
	private String name;//名称
	private String area;//分区
	private String type;//类型
	private String volume;//容量
	private String time;//使用时间
	private String state;//状态
	private String belong;//归属
	private String powerNum;//电源数量
	private String netType;//网络类型
	private String airCondition;//是否有空调
	
	public ClassRoomIfo(List<String> list)
	{
		this.num = list.get(0);
		this.name = list.get(1);
		this.area = list.get(2);
		this.type = list.get(3);
		this.volume = list.get(4);
		this.time = list.get(5);
		this.state = list.get(6);
		this.belong = list.get(7);
		this.powerNum = list.get(8);
		this.netType = list.get(9);
		this.airCondition = list.get(10);
	}
	
	/**
	 * @Description
	 * 获取简单的教室信息的String类型的List
	 * @return
	 * 获取的顺序：名称，分区，类型，容量
	 */
	public List<String> GetSimpleIfo()
	{
		List<String> list = new ArrayList<String>();
		list.add(name);
		list.add(area);
		list.add(type);
		list.add(volume);
		return list;
	}
	
	/**
	 * @Description
	 * 获取详细的僵尸信息的String类型的List
	 * @return
	 * 获取的顺序：编号，名称，分区，类型，容量，使用时间，状态，归属
	 */
	public List<String> GetDetailIfo()
	{
		List<String> list = new ArrayList<String>();
		list.add(num);
		list.add(name);
		list.add(area);
		list.add(type);
		list.add(volume);
		list.add(time);
		list.add(state);
		list.add(belong);
		return list;
	}
	
	public String GetNum()
	{
		return num;
	}
	public String GetName()
	{
		return name;
	}
	public String GetArea()
	{
		return area;
	}
	public String GetType()
	{
		return type;
	}
	public String GetTime()
	{
		return time;
	}
	public String GetVolume()
	{
		return volume;
	}
	public String GetState()
	{
		return state;
	}
	public String GetBelong()
	{
		return belong;
	}
	public String GetPowerNum()
	{
		return powerNum;
	}
	public String GetNetType()
	{
		return netType;
	}
	public String GetAirCondition()
	{
		return airCondition;
	}

}
