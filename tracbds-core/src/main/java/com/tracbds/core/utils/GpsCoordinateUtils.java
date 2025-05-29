package com.tracbds.core.utils;

import java.util.ArrayList;
import java.util.List;

public class GpsCoordinateUtils {
	private static final double PI = 3.1415926535897932384626433832795;
    //    private static final double PI = 3.14159265358979324;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    /**
     * 地球坐标系 WGS-84 to 火星坐标系 GCJ-02
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calWGS84toGCJ02(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude + dev.getLatitude();
        double retLon = longitude + dev.getLongitude();
        return new double[]{retLat, retLon};
    }

    /**
     * 地球坐标系 WGS-84 to 百度坐标系 BD-09
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calWGS84toBD09(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude + dev.getLatitude();
        double retLon = longitude + dev.getLongitude();
        return calGCJ02toBD09(retLat, retLon);
    }

    /**
     * 火星坐标系 GCJ-02 to 地球坐标系 WGS-84
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calGCJ02toWGS84(double latitude, double longitude) {
        Point dev = calDev(latitude, longitude);
        double retLat = latitude - dev.getLatitude();
        double retLon = longitude - dev.getLongitude();
        dev = calDev(retLat, retLon);
        retLat = latitude - dev.getLatitude();
        retLon = longitude - dev.getLongitude();
        return new double[]{retLat, retLon};
    }

    /**
     * 百度坐标系 BD-09 to 地球坐标系 WGS-84
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calBD09toWGS84(double latitude, double longitude) {
        double[] gcj = calBD09toGCJ02(latitude, longitude);
        return calGCJ02toWGS84(gcj[0], gcj[1]);
    }

    private static Point calDev(double latitude, double longitude) {
        if (isOutOfChina(latitude, longitude, false)) {
            return new Point(latitude, latitude);
        }
        double dLat = calLat(longitude - 105.0, latitude - 35.0);
        double dLon = calLon(longitude - 105.0, latitude - 35.0);
        double radLat = latitude / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        return new Point(dLat, dLon);
    }

    private static double calLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double calLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 火星坐标系 GCJ-02 to 百度坐标系 BD-09
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calGCJ02toBD09(double latitude, double longitude) {
        double x = longitude, y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        double retLat = z * Math.sin(theta) + 0.006;
        double retLon = z * Math.cos(theta) + 0.0065;
        return new double[]{retLat, retLon};
    }

    /**
     * 百度坐标系 BD-09 to 火星坐标系 GCJ-02
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return [纬度, 经度]
     */
    public static double[] calBD09toGCJ02(double latitude, double longitude) {
        double x = longitude - 0.0065, y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        double retLat = z * Math.sin(theta);
        double retLon = z * Math.cos(theta);
        return new double[]{retLat, retLon};
    }

    /**
     * 判断坐标是否在国内
     *
     * @param latitude
     * @param longitude
     * @param precision 是否精确判断范围
     * @return true 在国外，false 在国内
     */
    public static boolean isOutOfChina(double latitude, double longitude, boolean precision) {
        if (precision) {
            return CHINA_POLYGON.stream().noneMatch(point -> pointInPolygon(point, latitude, longitude));
        } else {
            if (longitude < 72.004 || longitude > 137.8347) {
                return true;
            }
            if (latitude < 0.8293 || latitude > 55.8271) {
                return true;
            }
            return false;
        }
    }

    /**
     * 检查坐标点是否在多边形区域内
     *
     * @param polygon   多边形
     * @param latitude  纬度
     * @param longitude 经度
     * @return true 在多边形区域内，false 在多边形区域外
     */
    private static boolean pointInPolygon(Point[] polygon, double latitude, double longitude) {
        int i, j = polygon.length - 1;
        boolean oddNodes = false;
        for (i = 0; i < polygon.length; i++) {
            if ((polygon[i].getLatitude() < latitude && polygon[j].getLatitude() >= latitude
                    || polygon[j].getLatitude() < latitude && polygon[i].getLatitude() >= latitude)
                    && (polygon[i].getLongitude() <= longitude || polygon[j].getLongitude() <= longitude)) {
                if (polygon[i].getLongitude()
                        + (latitude - polygon[i].getLatitude()) / (polygon[j].getLatitude() - polygon[i].getLatitude())
                        * (polygon[j].getLongitude() - polygon[i].getLongitude())
                        < longitude) {
                    oddNodes = !oddNodes;
                }
            }
            j = i;
        }
        return oddNodes;
    }

    static class Point {
        private double longitude;
        private double latitude;

        Point(double latitude, double longitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return longitude + "," + latitude;
        }
    }

    //region 中国行政边界的WGS84坐标数据
    //Mainland
    private static final Point[] MAINLAND = new Point[]{
            new Point(27.32083, 88.91693),
            new Point(27.54243, 88.76464),
            new Point(28.00805, 88.83575),
            new Point(28.1168, 88.62435),
            new Point(27.86605, 88.14279),
            new Point(27.82305, 87.19275),
            new Point(28.11166, 86.69527),
            new Point(27.90888, 86.45137),
            new Point(28.15805, 86.19769),
            new Point(27.88625, 86.0054),
            new Point(28.27916, 85.72137),
            new Point(28.30666, 85.11095),
            new Point(28.59104, 85.19518),
            new Point(28.54444, 84.84665),
            new Point(28.73402, 84.48623),
            new Point(29.26097, 84.11651),
            new Point(29.18902, 83.5479),
            new Point(29.63166, 83.19109),
            new Point(30.06923, 82.17525),
            new Point(30.33444, 82.11123),
            new Point(30.385, 81.42623),
            new Point(30.01194, 81.23221),
            new Point(30.20435, 81.02536),
            new Point(30.57552, 80.207),
            new Point(30.73374, 80.25423),
            new Point(30.96583, 79.86304),
            new Point(30.95708, 79.55429),
            new Point(31.43729, 79.08082),
            new Point(31.30895, 78.76825),
            new Point(31.96847, 78.77075),
            new Point(32.24304, 78.47594),
            new Point(32.5561, 78.40595),
            new Point(32.63902, 78.74623),
            new Point(32.35083, 78.9711),
            new Point(32.75666, 79.52874),
            new Point(33.09944, 79.37511),
            new Point(33.42863, 78.93623),
            new Point(33.52041, 78.81387),
            new Point(34.06833, 78.73581),
            new Point(34.35001, 78.98535),
            new Point(34.6118, 78.33707),
            new Point(35.28069, 78.02305),
            new Point(35.49902, 78.0718),
            new Point(35.50133, 77.82393),
            new Point(35.6125, 76.89526),
            new Point(35.90665, 76.55304),
            new Point(35.81458, 76.18061),
            new Point(36.07082, 75.92887),
            new Point(36.23751, 76.04166),
            new Point(36.66343, 75.85984),
            new Point(36.73169, 75.45179),
            new Point(36.91156, 75.39902),
            new Point(36.99719, 75.14787),
            new Point(37.02782, 74.56543),
            new Point(37.17, 74.39089),
            new Point(37.23733, 74.91574),
            new Point(37.40659, 75.18748),
            new Point(37.65243, 74.9036),
            new Point(38.47256, 74.85442),
            new Point(38.67438, 74.35471),
            new Point(38.61271, 73.81401),
            new Point(38.88653, 73.70818),
            new Point(38.97256, 73.85235),
            new Point(39.23569, 73.62005),
            new Point(39.45483, 73.65569),
            new Point(39.59965, 73.95471),
            new Point(39.76896, 73.8429),
            new Point(40.04202, 73.99096),
            new Point(40.32792, 74.88089),
            new Point(40.51723, 74.8588),
            new Point(40.45042, 75.23394),
            new Point(40.64452, 75.58284),
            new Point(40.298, 75.70374),
            new Point(40.35324, 76.3344),
            new Point(41.01258, 76.87067),
            new Point(41.04079, 78.08083),
            new Point(41.39286, 78.39554),
            new Point(42.03954, 80.24513),
            new Point(42.19622, 80.23402),
            new Point(42.63245, 80.15804),
            new Point(42.81565, 80.25796),
            new Point(42.88545, 80.57226),
            new Point(43.02906, 80.38405),
            new Point(43.1683, 80.81526),
            new Point(44.11378, 80.36887),
            new Point(44.6358, 80.38499),
            new Point(44.73408, 80.51589),
            new Point(44.90282, 79.87106),
            new Point(45.3497, 81.67928),
            new Point(45.15748, 81.94803),
            new Point(45.13303, 82.56638),
            new Point(45.43581, 82.64624),
            new Point(45.5831, 82.32179),
            new Point(47.20061, 83.03443),
            new Point(46.97332, 83.93026),
            new Point(46.99361, 84.67804),
            new Point(46.8277, 84.80318),
            new Point(47.0591, 85.52257),
            new Point(47.26221, 85.70139),
            new Point(47.93721, 85.53707),
            new Point(48.39333, 85.76596),
            new Point(48.54277, 86.59791),
            new Point(49.1102, 86.87602),
            new Point(49.09262, 87.34821),
            new Point(49.17295, 87.8407),
            new Point(48.98304, 87.89291),
            new Point(48.88103, 87.7611),
            new Point(48.73499, 88.05942),
            new Point(48.56541, 87.99194),
            new Point(48.40582, 88.51679),
            new Point(48.21193, 88.61179),
            new Point(47.99374, 89.08514),
            new Point(47.88791, 90.07096),
            new Point(46.95221, 90.9136),
            new Point(46.57735, 91.07027),
            new Point(46.29694, 90.92151),
            new Point(46.01735, 91.02651),
            new Point(45.57972, 90.68193),
            new Point(45.25305, 90.89694),
            new Point(45.07729, 91.56088),
            new Point(44.95721, 93.5547),
            new Point(44.35499, 94.71735),
            new Point(44.29416, 95.41061),
            new Point(44.01937, 95.34109),
            new Point(43.99311, 95.53339),
            new Point(43.28388, 95.87901),
            new Point(42.73499, 96.38206),
            new Point(42.79583, 97.1654),
            new Point(42.57194, 99.51012),
            new Point(42.67707, 100.8425),
            new Point(42.50972, 101.8147),
            new Point(42.23333, 102.0772),
            new Point(41.88721, 103.4164),
            new Point(41.87721, 104.5267),
            new Point(41.67068, 104.5237),
            new Point(41.58666, 105.0065),
            new Point(42.46624, 107.4758),
            new Point(42.42999, 109.3107),
            new Point(42.64576, 110.1064),
            new Point(43.31694, 110.9897),
            new Point(43.69221, 111.9583),
            new Point(44.37527, 111.4214),
            new Point(45.04944, 111.873),
            new Point(45.08055, 112.4272),
            new Point(44.8461, 112.853),
            new Point(44.74527, 113.638),
            new Point(45.38943, 114.5453),
            new Point(45.4586, 115.7019),
            new Point(45.72193, 116.2104),
            new Point(46.29583, 116.5855),
            new Point(46.41888, 117.3755),
            new Point(46.57069, 117.425),
            new Point(46.53645, 117.8455),
            new Point(46.73638, 118.3147),
            new Point(46.59895, 119.7068),
            new Point(46.71513, 119.9315),
            new Point(46.90221, 119.9225),
            new Point(47.66499, 119.125),
            new Point(47.99475, 118.5393),
            new Point(48.01125, 117.8046),
            new Point(47.65741, 117.3827),
            new Point(47.88805, 116.8747),
            new Point(47.87819, 116.2624),
            new Point(47.69186, 115.9231),
            new Point(47.91749, 115.5944),
            new Point(48.14353, 115.5491),
            new Point(48.25249, 115.8358),
            new Point(48.52055, 115.8111),
            new Point(49.83047, 116.7114),
            new Point(49.52058, 117.8747),
            new Point(49.92263, 118.5746),
            new Point(50.09631, 119.321),
            new Point(50.33028, 119.36),
            new Point(50.39027, 119.1386),
            new Point(51.62083, 120.0641),
            new Point(52.115, 120.7767),
            new Point(52.34423, 120.6259),
            new Point(52.54267, 120.7122),
            new Point(52.58805, 120.0819),
            new Point(52.76819, 120.0314),
            new Point(53.26374, 120.8307),
            new Point(53.54361, 123.6147),
            new Point(53.18832, 124.4933),
            new Point(53.05027, 125.62),
            new Point(52.8752, 125.6573),
            new Point(52.75722, 126.0968),
            new Point(52.5761, 125.9943),
            new Point(52.12694, 126.555),
            new Point(51.99437, 126.4412),
            new Point(51.38138, 126.9139),
            new Point(51.26555, 126.8176),
            new Point(51.31923, 126.9689),
            new Point(51.05825, 126.9331),
            new Point(50.74138, 127.2919),
            new Point(50.31472, 127.334),
            new Point(50.20856, 127.5861),
            new Point(49.80588, 127.515),
            new Point(49.58665, 127.838),
            new Point(49.58443, 128.7119),
            new Point(49.34676, 129.1118),
            new Point(49.4158, 129.4902),
            new Point(48.86464, 130.2246),
            new Point(48.86041, 130.674),
            new Point(48.60576, 130.5236),
            new Point(48.3268, 130.824),
            new Point(48.10839, 130.6598),
            new Point(47.68721, 130.9922),
            new Point(47.71027, 132.5211),
            new Point(48.09888, 133.0827),
            new Point(48.06888, 133.4843),
            new Point(48.39112, 134.4153),
            new Point(48.26713, 134.7408),
            new Point(47.99207, 134.5576),
            new Point(47.70027, 134.7608),
            new Point(47.32333, 134.1825),
            new Point(46.64017, 133.9977),
            new Point(46.47888, 133.8472),
            new Point(46.25363, 133.9016),
            new Point(45.82347, 133.4761),
            new Point(45.62458, 133.4702),
            new Point(45.45083, 133.1491),
            new Point(45.05694, 133.0253),
            new Point(45.34582, 131.8684),
            new Point(44.97388, 131.4691),
            new Point(44.83649, 130.953),
            new Point(44.05193, 131.298),
            new Point(43.53624, 131.1912),
            new Point(43.38958, 131.3104),
            new Point(42.91645, 131.1285),
            new Point(42.74485, 130.4327),
            new Point(42.42186, 130.6044),
            new Point(42.71416, 130.2468),
            new Point(42.88794, 130.2514),
            new Point(43.00457, 129.9046),
            new Point(42.43582, 129.6955),
            new Point(42.44624, 129.3493),
            new Point(42.02736, 128.9269),
            new Point(42.00124, 128.0566),
            new Point(41.58284, 128.3002),
            new Point(41.38124, 128.1529),
            new Point(41.47249, 127.2708),
            new Point(41.79222, 126.9047),
            new Point(41.61176, 126.5661),
            new Point(40.89694, 126.0118),
            new Point(40.47037, 124.8851),
            new Point(40.09362, 124.3736),
            new Point(39.82777, 124.128),
            new Point(39.8143, 123.2422),
            new Point(39.67388, 123.2167),
            new Point(38.99638, 121.648),
            new Point(38.8611, 121.6982),
            new Point(38.71909, 121.1873),
            new Point(38.91221, 121.0887),
            new Point(39.09013, 121.6794),
            new Point(39.2186, 121.5994),
            new Point(39.35166, 121.7511),
            new Point(39.52847, 121.2283),
            new Point(39.62322, 121.533),
            new Point(39.81138, 121.4683),
            new Point(40.00305, 121.881),
            new Point(40.50562, 122.2987),
            new Point(40.73874, 122.0521),
            new Point(40.92194, 121.1775),
            new Point(40.1961, 120.4468),
            new Point(39.87242, 119.5264),
            new Point(39.15693, 118.9715),
            new Point(39.04083, 118.3273),
            new Point(39.19846, 117.889),
            new Point(38.67555, 117.5364),
            new Point(38.38666, 117.6722),
            new Point(38.16721, 118.0281),
            new Point(38.1529, 118.8378),
            new Point(37.87832, 119.0355),
            new Point(37.30054, 118.9566),
            new Point(37.14361, 119.2328),
            new Point(37.15138, 119.7672),
            new Point(37.35228, 119.8529),
            new Point(37.83499, 120.7371),
            new Point(37.42458, 121.58),
            new Point(37.55256, 122.1282),
            new Point(37.41833, 122.1814),
            new Point(37.39624, 122.5586),
            new Point(37.20999, 122.5972),
            new Point(37.02583, 122.4005),
            new Point(37.01978, 122.5392),
            new Point(36.89361, 122.5047),
            new Point(36.84298, 122.1923),
            new Point(37.00027, 121.9566),
            new Point(36.75889, 121.5944),
            new Point(36.61666, 120.7764),
            new Point(36.52638, 120.96),
            new Point(36.37582, 120.8753),
            new Point(36.42277, 120.7062),
            new Point(36.14075, 120.6956),
            new Point(36.0419, 120.3436),
            new Point(36.26345, 120.3078),
            new Point(36.19998, 120.0889),
            new Point(35.95943, 120.2378),
            new Point(35.57893, 119.6475),
            new Point(34.88499, 119.1761),
            new Point(34.31145, 120.2487),
            new Point(32.97499, 120.8858),
            new Point(32.63889, 120.8375),
            new Point(32.42958, 121.3348),
            new Point(32.11333, 121.4412),
            new Point(32.02166, 121.7066),
            new Point(31.67833, 121.8275),
            new Point(31.86639, 120.9444),
            new Point(32.09361, 120.6019),
            new Point(31.94555, 120.099),
            new Point(32.30638, 119.8267),
            new Point(32.26277, 119.6317),
            new Point(31.90388, 120.1364),
            new Point(31.98833, 120.7026),
            new Point(31.81944, 120.7196),
            new Point(31.30889, 121.6681),
            new Point(30.97986, 121.8828),
            new Point(30.85305, 121.8469),
            new Point(30.56889, 120.9915),
            new Point(30.33555, 120.8144),
            new Point(30.39298, 120.4586),
            new Point(30.19694, 120.15),
            new Point(30.31027, 120.5082),
            new Point(30.06465, 120.7916),
            new Point(30.30458, 121.2808),
            new Point(29.96305, 121.6778),
            new Point(29.88211, 122.1196),
            new Point(29.51167, 121.4483),
            new Point(29.58916, 121.9744),
            new Point(29.19527, 121.9336),
            new Point(29.18388, 121.8119),
            new Point(29.37236, 121.7969),
            new Point(29.19729, 121.7444),
            new Point(29.29111, 121.5611),
            new Point(29.1634, 121.4135),
            new Point(29.02194, 121.6914),
            new Point(28.9359, 121.4908),
            new Point(28.72798, 121.6113),
            new Point(28.84215, 121.1464),
            new Point(28.66993, 121.4844),
            new Point(28.34722, 121.6417),
            new Point(28.13889, 121.3419),
            new Point(28.38277, 121.1651),
            new Point(27.98222, 120.9353),
            new Point(28.07944, 120.5908),
            new Point(27.87229, 120.84),
            new Point(27.59319, 120.5812),
            new Point(27.45083, 120.6655),
            new Point(27.20777, 120.5075),
            new Point(27.28278, 120.1896),
            new Point(27.14764, 120.4211),
            new Point(26.89805, 120.0332),
            new Point(26.64465, 120.128),
            new Point(26.51778, 119.8603),
            new Point(26.78823, 120.0733),
            new Point(26.64888, 119.8668),
            new Point(26.79611, 119.7879),
            new Point(26.75625, 119.5503),
            new Point(26.44222, 119.8204),
            new Point(26.47388, 119.5775),
            new Point(26.33861, 119.658),
            new Point(26.36777, 119.9489),
            new Point(25.99694, 119.4253),
            new Point(26.14041, 119.0975),
            new Point(25.93788, 119.354),
            new Point(25.99069, 119.7058),
            new Point(25.67996, 119.5807),
            new Point(25.68222, 119.4522),
            new Point(25.35333, 119.6454),
            new Point(25.60649, 119.3149),
            new Point(25.42097, 119.1053),
            new Point(25.25319, 119.3526),
            new Point(25.17208, 119.2726),
            new Point(25.2426, 118.8749),
            new Point(24.97194, 118.9866),
            new Point(24.88291, 118.5729),
            new Point(24.75673, 118.7631),
            new Point(24.52861, 118.5953),
            new Point(24.53638, 118.2397),
            new Point(24.68194, 118.1688),
            new Point(24.44024, 118.0199),
            new Point(24.46019, 117.7947),
            new Point(24.25875, 118.1237),
            new Point(23.62437, 117.1957),
            new Point(23.65919, 116.9179),
            new Point(23.355, 116.7603),
            new Point(23.42024, 116.5322),
            new Point(23.23666, 116.7871),
            new Point(23.21083, 116.5139),
            new Point(22.93902, 116.4817),
            new Point(22.73916, 115.7978),
            new Point(22.88416, 115.6403),
            new Point(22.65889, 115.5367),
            new Point(22.80833, 115.1614),
            new Point(22.70277, 114.8889),
            new Point(22.53305, 114.8722),
            new Point(22.64027, 114.718),
            new Point(22.81402, 114.7782),
            new Point(22.69972, 114.5208),
            new Point(22.50423, 114.6136),
            new Point(22.55004, 114.2223),
            new Point(22.42993, 114.3885),
            new Point(22.26056, 114.2961),
            new Point(22.36736, 113.9056),
            new Point(22.50874, 114.0337),
            new Point(22.47444, 113.8608),
            new Point(22.83458, 113.606),
            new Point(23.05027, 113.5253),
            new Point(23.11724, 113.8219),
            new Point(23.05083, 113.4793),
            new Point(22.87986, 113.3629),
            new Point(22.54944, 113.5648),
            new Point(22.18701, 113.5527),
            new Point(22.56701, 113.1687),
            new Point(22.17965, 113.3868),
            new Point(22.04069, 113.2226),
            new Point(22.20485, 113.0848),
            new Point(21.8693, 112.94),
            new Point(21.96472, 112.824),
            new Point(21.70139, 112.2819),
            new Point(21.91611, 111.8921),
            new Point(21.75139, 111.9669),
            new Point(21.77819, 111.6762),
            new Point(21.61264, 111.7832),
            new Point(21.5268, 111.644),
            new Point(21.52528, 111.0285),
            new Point(21.21138, 110.5328),
            new Point(21.37322, 110.3944),
            new Point(20.84381, 110.1594),
            new Point(20.84083, 110.3755),
            new Point(20.64, 110.3239),
            new Point(20.48618, 110.5274),
            new Point(20.24611, 110.2789),
            new Point(20.2336, 109.9244),
            new Point(20.4318, 110.0069),
            new Point(20.92416, 109.6629),
            new Point(21.44694, 109.9411),
            new Point(21.50569, 109.6605),
            new Point(21.72333, 109.5733),
            new Point(21.49499, 109.5344),
            new Point(21.39666, 109.1428),
            new Point(21.58305, 109.1375),
            new Point(21.61611, 108.911),
            new Point(21.79889, 108.8702),
            new Point(21.59888, 108.7403),
            new Point(21.93562, 108.4692),
            new Point(21.59014, 108.5125),
            new Point(21.68999, 108.3336),
            new Point(21.51444, 108.2447),
            new Point(21.54241, 107.99),
            new Point(21.66694, 107.7831),
            new Point(21.60526, 107.3627),
            new Point(22.03083, 106.6933),
            new Point(22.45682, 106.5517),
            new Point(22.76389, 106.7875),
            new Point(22.86694, 106.7029),
            new Point(22.91253, 105.8771),
            new Point(23.32416, 105.3587),
            new Point(23.18027, 104.9075),
            new Point(22.81805, 104.7319),
            new Point(22.6875, 104.3747),
            new Point(22.79812, 104.1113),
            new Point(22.50387, 103.9687),
            new Point(22.78287, 103.6538),
            new Point(22.58436, 103.5224),
            new Point(22.79451, 103.3337),
            new Point(22.43652, 103.0304),
            new Point(22.77187, 102.4744),
            new Point(22.39629, 102.1407),
            new Point(22.49777, 101.7415),
            new Point(22.20916, 101.5744),
            new Point(21.83444, 101.7653),
            new Point(21.14451, 101.786),
            new Point(21.17687, 101.2919),
            new Point(21.57264, 101.1482),
            new Point(21.76903, 101.099),
            new Point(21.47694, 100.6397),
            new Point(21.43546, 100.2057),
            new Point(21.72555, 99.97763),
            new Point(22.05018, 99.95741),
            new Point(22.15592, 99.16785),
            new Point(22.93659, 99.56484),
            new Point(23.08204, 99.5113),
            new Point(23.18916, 98.92747),
            new Point(23.97076, 98.67991),
            new Point(24.16007, 98.89073),
            new Point(23.92999, 97.54762),
            new Point(24.26055, 97.7593),
            new Point(24.47666, 97.54305),
            new Point(24.73992, 97.55255),
            new Point(25.61527, 98.19109),
            new Point(25.56944, 98.36137),
            new Point(25.85597, 98.7104),
            new Point(26.12527, 98.56944),
            new Point(26.18472, 98.73109),
            new Point(26.79166, 98.77777),
            new Point(27.52972, 98.69699),
            new Point(27.6725, 98.45888),
            new Point(27.54014, 98.31992),
            new Point(28.14889, 98.14499),
            new Point(28.54652, 97.55887),
            new Point(28.22277, 97.34888),
            new Point(28.46749, 96.65387),
            new Point(28.35111, 96.40193),
            new Point(28.525, 96.34027),
            new Point(28.79569, 96.61373),
            new Point(29.05666, 96.47083),
            new Point(28.90138, 96.17532),
            new Point(29.05972, 96.14888),
            new Point(29.25757, 96.39172),
            new Point(29.46444, 96.08315),
            new Point(29.03527, 95.38777),
            new Point(29.33346, 94.64751),
            new Point(29.07348, 94.23456),
            new Point(28.6692, 93.96172),
            new Point(28.61876, 93.35194),
            new Point(28.3193, 93.22205),
            new Point(28.1419, 92.71044),
            new Point(27.86194, 92.54498),
            new Point(27.76472, 91.65776),
            new Point(27.945, 91.66277),
            new Point(28.08111, 91.30138),
            new Point(27.96999, 91.08693),
            new Point(28.07958, 90.3765),
            new Point(28.24257, 90.38898),
            new Point(28.32369, 89.99819),
            new Point(28.05777, 89.48749),
            new Point(27.32083, 88.91693)
    };

    //Taiwan
    private static final Point[] TAIWAN = new Point[]{
            new Point(25.13474, 121.4441),
            new Point(25.28361, 121.5632),
            new Point(25.00722, 122.0004),
            new Point(24.85028, 121.8182),
            new Point(24.47638, 121.8397),
            new Point(23.0875, 121.3556),
            new Point(21.92791, 120.7196),
            new Point(22.31277, 120.6103),
            new Point(22.54044, 120.3071),
            new Point(23.04437, 120.0539),
            new Point(23.61708, 120.1112),
            new Point(25.00166, 121.0017),
            new Point(25.13474, 121.4441)
    };

    //Hainan
    private static final Point[] HAINAN = new Point[]{
            new Point(19.52888, 110.855),
            new Point(19.16761, 110.4832),
            new Point(18.80083, 110.5255),
            new Point(18.3852, 110.0503),
            new Point(18.39152, 109.7594),
            new Point(18.19777, 109.7036),
            new Point(18.50562, 108.6871),
            new Point(19.28028, 108.6283),
            new Point(19.76, 109.2939),
            new Point(19.7236, 109.1653),
            new Point(19.89972, 109.2572),
            new Point(19.82861, 109.4658),
            new Point(19.99389, 109.6108),
            new Point(20.13361, 110.6655),
            new Point(19.97861, 110.9425),
            new Point(19.63829, 111.0215),
            new Point(19.52888, 110.855)
    };

    //Chongming
    private static final Point[] CHONGMING = new Point[]{
            new Point(31.80054, 121.2039),
            new Point(31.49972, 121.8736),
            new Point(31.53111, 121.5464),
            new Point(31.80054, 121.2039)
    };
    //endregion

    /**
     * 中国行政边界的WGS84坐标数据，
     * 光线投射算法 (Ray casting algorithm) 获得，
     * 沿海、国界周边地区可能会有误差，更高精度需要调整坐标点
     */
    private static final List<Point[]> CHINA_POLYGON = new ArrayList<>();

    static {
        CHINA_POLYGON.add(MAINLAND);
        CHINA_POLYGON.add(TAIWAN);
        CHINA_POLYGON.add(HAINAN);
        CHINA_POLYGON.add(CHONGMING);
    }
}
