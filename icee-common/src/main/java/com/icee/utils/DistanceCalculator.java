package com.icee.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DistanceCalculator {

    // 地球半径（公里）
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * 计算两个坐标点之间的距离（公里）
     * @param lat1 地点1纬度
     * @param lng1 地点1经度
     * @param lat2 地点2纬度
     * @param lng2 地点2经度
     * @return 距离（公里），精确到两位小数
     */
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // 将角度转换为弧度
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = Math.toRadians(lat1 - lat2);
        double b = Math.toRadians(lng1 - lng2);

        // Haversine公式
        double haversine = Math.sin(a/2) * Math.sin(a/2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(b/2) * Math.sin(b/2);

        double distance = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        // 计算距离（公里）
        double result = EARTH_RADIUS_KM * distance;

        // 四舍五入保留两位小数
        return roundToTwoDecimals(result);
    }

    /**
     * 四舍五入保留两位小数
     */
    private static double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
