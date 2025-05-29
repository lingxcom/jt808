package com.tracbds.core.utils;

public class LatLngUtils {
	static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
    static double PI = 3.1415926535897932384626;
    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;
    
    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */
    public static String  bd09togcj02(double bd_lon, double bd_lat){
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
       // Point point=new Point(gg_lng, gg_lat);
       // return point;
        return gg_lng+","+gg_lat;
    }
    
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public static String gcj02tobd09(double lng, double lat){
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        //Point point=new Point(bd_lng, bd_lat);
       // return point;
        return bd_lng+","+bd_lat;
    };
    
    /**
     * WGS84转GCj02
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public static String wgs84togcj02(double lng, double lat){
   	 if(!isLocationInChina(lng,lat)) return lng+","+lat;//当不在国内，不做处理
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        //Point point=new Point(mglng, mglat);
       // return point;
        return mglng+","+mglat;
    };
    
    /**
     * GCJ02 转换为 WGS84
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public static String gcj02towgs84(double lng, double lat) {
   	 if(!isLocationInChina(lng,lat)) return lng+","+lat;//当不在国内，不做处理
    	double[]aaa=GpsCoordinateUtils.calGCJ02toWGS84(lat,lng);
    	return aaa[1]+","+aaa[0];
    }
    
    public static String bd09towgs84(double lng, double lat) {
   	 if(!isLocationInChina(lng,lat)) return lng+","+lat;
    	double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        LatLonPoint bean=Converter.toWGS84Point(gg_lat, gg_lng);
        return bean.getLongitude()+","+bean.getLatitude();//gcj02towgs84(gg_lat,gg_lng);
    }
    /**
     * WGS84 转换为 BD-09
     * @param lng
     * @param lat
     * @returns {*[]}
     * 
     */
     public static String  wgs84tobd09(double lng, double lat){
    	 if(!isLocationInChina(lng,lat)) return lng+","+lat;//当不在国内，不做处理
    	 
           //第一次转换
           double dlat = transformlat(lng - 105.0, lat - 35.0);
           double dlng = transformlng(lng - 105.0, lat - 35.0);
           double radlat = lat / 180.0 * PI;
           double magic = Math.sin(radlat);
           magic = 1 - ee * magic * magic;
           double sqrtmagic = Math.sqrt(magic);
           dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
           dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
           double mglat = lat + dlat;
           double mglng = lng + dlng;

           //第二次转换
           double z = Math.sqrt(mglng * mglng + mglat * mglat) + 0.00002 * Math.sin(mglat * x_PI);
           double theta = Math.atan2(mglat, mglng) + 0.000003 * Math.cos(mglng * x_PI);
           double bd_lng = z * Math.cos(theta) + 0.0065;
           double bd_lat = z * Math.sin(theta) + 0.006;
           return bd_lng+","+bd_lat;
     }

    private static double transformlat(double lng,double lat){
        double ret= -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlng(double lng,double lat){
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    
    
    
    // 定义常量，表示中国的边界
    private static final double MIN_LATITUDE = 18.0; // 中国最南纬度
    private static final double MAX_LATITUDE = 53.55; // 中国最北纬度
    private static final double MIN_LONGITUDE = 73.66; // 中国最西经度
    private static final double MAX_LONGITUDE = 135.05; // 中国最东经度

    public static boolean isLocationInChina( String lng,String lat) {
    	return isLocationInChina(Double.parseDouble(lng),Double.parseDouble(lat));
    }
    public static boolean isLocationInChina( double lng,double lat) {
        // 检查纬度是否在范围内
        if (lat >= MIN_LATITUDE && lat <= MAX_LATITUDE) {
            // 检查经度是否在范围内
            return (lng >= MIN_LONGITUDE && lng <= MAX_LONGITUDE);
        }
        return false; // 否则返回false
    }

    public static void main(String[] args) {
           System.out.println(isLocationInChina(39.92259481602818,116.39185917531302));
           
    }
}
