package com.tracbds.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.tracbds.core.bean.Point;

public class FenceUtils {
	
	public static void main(String args[]){
		String latlngs="[[118.06419481048769,24.645814465810552],[118.05936025988464,24.64345238485365],[118.05360408332422,24.638431606950927],[118.04707399565143,24.631500110760754],[118.04644553374139,24.629703815985298],[118.04625239938755,24.628428100200022],[118.04659141752842,24.62709727233198],[118.04644644499474,24.626747244304244],[118.04306207555945,24.624125625023094],[118.04103042363785,24.625358929939107],[118.0384686362537,24.623866225083418]]";
		List<Point> poly=getPoints(latlngs);
		
		System.out.println(getShortestDistance(new Point(118.066113,24.6462756),poly));
	}
	/**
	   * @description 射线法判断点是否在多边形内部
	   * @param {Object} p 待判断的点，格式：{ x: X坐标, y: Y坐标 }
	   * @param {Array} poly 多边形顶点，数组成员的格式同 p
	   * @return {boolean} 点 p 和多边形 poly 的几何关系，true为in，false为out
	   */
	public static final boolean rayCasting(Point p, List<Point> poly) {
		Point arr[]=new Point[poly.size()];
		poly.toArray(arr);
		return rayCasting(p,arr);
	}
	/**
	   * @description 射线法判断点是否在多边形内部
	   * @param {Object} p 待判断的点，格式：{ x: X坐标, y: Y坐标 }
	   * @param {Array} poly 多边形顶点，数组成员的格式同 p
	   * @return {boolean} 点 p 和多边形 poly 的几何关系，true为in，false为out
	   */
	public static final boolean rayCasting(Point p, Point[] poly) {
		double px = p.getLat();
		double py = p.getLng();
		boolean flag = false;
		for (int i = 0, l = poly.length, j = l - 1; i < l; j = i, i++) {
			double sx = poly[i].getLat(), sy = poly[i].getLng(), tx = poly[j].getLat(), ty = poly[j].getLng();

			// 点与多边形顶点重合
			if ((sx == px && sy == py) || (tx == px && ty == py)) {
				return true;
			}

			// 判断线段两端点是否在射线两侧
			if ((sy < py && ty >= py) || (sy >= py && ty < py)) {
				// 线段上与射线 Y 坐标相同的点的 X 坐标
				double x = sx + (py - sy) * (tx - sx) / (ty - sy);

				// 点在多边形的边上
				if (x == px) {
					return true;
				}

				// 射线穿过多边形的边界
				if (x > px) {
					flag = !flag;
				}
			}
		}
		return flag;
	}

	public static double getDistanceP2L(
            double pointLat, double pointLon, double lat1, double lon1, double lat2, double lon2) {
        double d0 = distance(pointLat, pointLon, lat1, lon1);
        double d1 = distance(lat1, lon1, lat2, lon2);
        double d2 = distance(lat2, lon2, pointLat, pointLon);
        if (Math.pow(d0, 2) > Math.pow(d1, 2) + Math.pow(d2, 2)) {
            return d2;
        }
        if (Math.pow(d2, 2) > Math.pow(d1, 2) + Math.pow(d0, 2)) {
            return d0;
        }
        double halfP = (d0 + d1 + d2) * 0.5;
        double area = Math.sqrt(halfP * (halfP - d0) * (halfP - d1) * (halfP - d2));
        return 2 * area / d1;
    }

    /**
     * 点到线的最短距离
     * @param lat1 点x0
     * @param lng1 点y0
     * @param latStart 线x1
     * @param lngStart 线y1
     * @param latEnd   线x2
     * @param lngEnd   线y2
     * @return
     */
	public static double getDistanceP2L_bak(double x0,double y0,double x1,double y1,double x2,double y2){
		 //求出三边长度
        double disLine=distance(x1,y1,x2,y2) ;
        double disOne=distance(x1,y1,x0,y0);
        double disTwo=distance(x2,y2,x0,y0);
        //判断是否在同一条线上，误差计算可能出现两条直线距离小于直线长度的情况
        if((disOne+disTwo)<=disLine) return 0;
        /*
        double a=disLine,b=disOne,c=disTwo;
        double cosa=(b*b+c*c-a*a)/2*b*c;
        double cosb=(a*a+c*c-b*b)/2*a*c;
        double cosc=(a*a+b*b-c*c)/2*a*b;
        System.out.println(cosa);
        System.out.println(cosb);
        System.out.println(cosc);*/
        //求出三角形面积
        double p=(disLine+disOne+disTwo)/2;
        //System.out.println(disLine);
       // System.out.println(disOne);
        //System.out.println(disTwo);
        //System.out.println(p);
        double s=Math.sqrt(p*(p-disLine)*(p-disOne)*(p-disTwo));
        //返回Line的高即为点到线距离
        double temp=2*s/disLine;
        if(temp>disLine)return temp;
        if(disLine>disOne&&disLine>disTwo) {
        	return temp;
        }else {
        	return disOne+disTwo;
        }
        //return 2*s/disLine;
		 
	}
	public static double getDistanceP2L(Point p, List<Point> polyline){
		double jl=0,temp;Point p1,p2;
		for(int i=0;i<polyline.size()-1;i++) {
			 p1=polyline.get(i);p2=polyline.get(i+1);
			 temp=getDistanceP2L(p.getLat(),p.getLng(),p1.getLat(),p1.getLng(),p2.getLat(),p2.getLng());
		if(jl==0||temp<jl) {
			jl=temp;
		}
		}
		return jl;
	}
    
	
	public static List<Point> getPoints(String latlngs){
		List<Point> list=new ArrayList<>();
		try {
			List<List<BigDecimal>> listLatlng=(List<List<BigDecimal>>)JSON.parse(latlngs);
			for(List<BigDecimal> list1:listLatlng) {
				list.add(new Point(list1.get(0).doubleValue(),list1.get(1).doubleValue()));
			}
		} catch (Exception e) {
			System.out.println(latlngs);
			e.printStackTrace();
		}
		return list;
	}
	public static Point getPoint(String latlng) {
		List<BigDecimal> list=(List<BigDecimal>)JSON.parse(latlng);
		return new Point(list.get(0).doubleValue(),list.get(1).doubleValue());
	}

	public static Point getPoint(String lat,String lng) {
		return new Point(Double.parseDouble(lat),Double.parseDouble(lng));
	}
	public static Point getPoint(double lat,double lng) {
		return new Point(lat,lng);
	}
	/**
	 * chatGPT
	 * @param point
	 * @param points
	 * @return
	 */
	public static double getShortestDistance(Point point, List<Point> points) {
	    double minDistance = Double.MAX_VALUE;
	    for (int i = 0; i < points.size() - 1; i++) {
	        Point p1 = points.get(i);
	        Point p2 = points.get(i + 1);
	        double distance = getDistance(point, p1, p2);
	        if (distance < minDistance) {
	            minDistance = distance;
	        }
	    }
	    return minDistance;
	}
	private static double getDistance(Point point, Point p1, Point p2) {
	    double a = point.getLat() - p1.getLat();
	    double b = point.getLng() - p1.getLng();
	    double c = p2.getLat() - p1.getLat();
	    double d = p2.getLng() - p1.getLng();
	    double dot = a * c + b * d;
	    double len_sq = c * c + d * d;
	    double param = -1;
	    if (len_sq != 0) //in case of 0 length line
	        param = dot / len_sq;
	    double xx, yy;
	    if (param < 0) {
	        xx = p1.getLat();
	        yy = p1.getLng();
	    }
	    else if (param > 1) {
	        xx = p2.getLat();
	        yy = p2.getLng();
	    }
	    else {
	        xx = p1.getLat() + param * c;
	        yy = p1.getLng() + param * d;
	    }

		double jl_jd = 102834.74258026089786013677476285;
		double jl_wd = 111712.69150641055729984301412873;
	    double dx = (point.getLat() - xx)* jl_jd;
	    double dy = (point.getLng() - yy)*jl_wd;

	    return Math.sqrt(dx * dx + dy * dy);
	}
	public static double distance(Point p1, Point p2) {
		return distance(p1.getLat(),p1.getLng(),p2.getLat(),p2.getLng());
	}
    public static double distance(double n1, double e1, double n2, double e2) {
		double jl_jd = 102834.74258026089786013677476285;
		double jl_wd = 111712.69150641055729984301412873;
		double b = Math.abs((e1 - e2) * jl_jd);
		double a = Math.abs((n1 - n2) * jl_wd);
		return Math.sqrt((a * a + b * b));

	}
}
