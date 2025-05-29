package com.tracbds.core.utils;

public class DistanceCalculator {
	// 地球平均半径（单位：米）
    private static final double EARTH_RADIUS = 6371e3;

    /**
     * 计算两个经纬度坐标之间的距离（单位：米）
     * @param lat1 点1纬度
     * @param lon1 点1经度
     * @param lat2 点2纬度
     * @param lon2 点2经度
     * @return 两点间距离（米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将角度转换为弧度
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // 经纬度差值
        double deltaLat = radLat2 - radLat1;
        double deltaLon = radLon2 - radLon1;

        // Haversine公式计算
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                   Math.cos(radLat1) * Math.cos(radLat2) *
                   Math.pow(Math.sin(deltaLon / 2), 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        return EARTH_RADIUS * c;
    }

    public static void main(String[] args) {
        // 示例：北京天安门(39.9087, 116.3975) 到 上海外滩(31.2304, 121.4737)
        double distance = calculateDistance(39.9087, 116.3975, 31.2304, 121.4737);
        System.out.println("距离: " + distance + " 米"); 
        // 输出：距离: 1067594.5474436793 米（约1067公里）
       // System.out.println(JT808Utils.distance(39.9087, 116.3975, 31.2304, 121.4737));
    }
}
