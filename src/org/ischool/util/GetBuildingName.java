package org.ischool.util;

public class GetBuildingName
{
	private static final double EARTH_RADIUS = 6378.137;

	private static final double LATITUDE_2 = 39.929652;
	private static final double LONGITUDE_2 = 116.207044;

	private static final double LATITUDE_3 = 39.927896;
	private static final double LONGITUDE_3 = 116.205472;

	private static final double LATITUDE_4 = 39.930265;
	private static final double LONGITUDE_4 = 116.206601;

	private static final double LATITUDE_5 = 39.927121;
	private static final double LONGITUDE_5 = 116.207744;

	public GetBuildingName()
	{
		// TODO Auto-generated constructor stub
	}

	private static double rad(double d)
	{
		return d * Math.PI / 180.0;
	}

	private static double GetDistanceTo2(double latitude, double longitude)
	{
		double radLat1 = rad(latitude);
		double radLat2 = rad(LATITUDE_2);
		double a = radLat1 - radLat2;
		double b = rad(longitude) - rad(LONGITUDE_2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double GetDistanceTo3(double latitude, double longitude)
	{
		double radLat1 = rad(latitude);
		double radLat2 = rad(LATITUDE_3);
		double a = radLat1 - radLat2;
		double b = rad(longitude) - rad(LONGITUDE_3);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double GetDistanceTo4(double latitude, double longitude)
	{
		double radLat1 = rad(latitude);
		double radLat2 = rad(LATITUDE_4);
		double a = radLat1 - radLat2;
		double b = rad(longitude) - rad(LONGITUDE_4);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double GetDistanceTo5(double latitude, double longitude)
	{
		double radLat1 = rad(latitude);
		double radLat2 = rad(LATITUDE_5);
		double a = radLat1 - radLat2;
		double b = rad(longitude) - rad(LONGITUDE_5);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static String findNameByLocation(double latitude, double longitude)
	{
		String areaName = "";
		double distance2 = GetDistanceTo2(latitude, longitude);
		double distance3 = GetDistanceTo3(latitude, longitude);
		double distance4 = GetDistanceTo4(latitude, longitude);
		double distance5 = GetDistanceTo5(latitude, longitude);

		double distanceTemp = distance2;

		if (distanceTemp < distance3)
		{
			areaName = "二教";
		} else
		{
			distanceTemp = distance3;
			areaName = "三教";
		}

		if (distanceTemp > distance4)
		{
			distanceTemp = distance4;
			areaName = "四教";
		}

		if (distanceTemp > distance5)
		{
			areaName = "五教";
		}
		return areaName;
	}
}
