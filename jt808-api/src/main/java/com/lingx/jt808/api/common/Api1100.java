package com.lingx.jt808.api.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.service.JT808CommonService;

@Component
public class Api1100 extends AbstractAuthApi {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JT808CommonService commonService;
    @Autowired
    private LingxService lingxService;

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE8 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MMDD = DateTimeFormatter.ofPattern("MM-dd");

    @Override
    public boolean isLog() {
        return false;
    }

    @Override
    public Map<String, Object> api(Map<String, Object> params) {
        long ts111 = System.currentTimeMillis();
        String token = IApi.getParamString(params, "lingxtoken", "");
        String userid = TokenUtils.getTokenDataUserId(token);

        Map<String, Object> ret = IApi.getRetMap(1, "SUCCESS");
        Map<String, Object> data = new HashMap<>();

        String sqlAll = "SELECT COUNT(*) FROM tgps_car WHERE group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?)";
        String sqlOnline = "SELECT COUNT(*) FROM tgps_car WHERE online='1' AND group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?)";

        int co = queryForInt(sqlAll, userid);
        int ol = queryForInt(sqlOnline, userid);

        data.put("all", co);
        data.put("online", ol);
        ts111 = printTs(ts111, "总数与在线");

        LocalDate today = LocalDate.now();
        String today8 = DATE8.format(today);
        String stime = today8 + "000000";
        String etime = today8 + "235959";

        int bj = queryForInt(
                "SELECT COUNT(*) FROM tgps_car_alarm WHERE gpstime>=? AND gpstime<=? AND car_id IN (SELECT id FROM tgps_car WHERE group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?))",
                stime, etime, userid);
        int lc = queryForInt(
                "SELECT SUM(dtlc) FROM tgps_mileage WHERE create_time=? AND car_id IN (SELECT id FROM tgps_car WHERE group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?))",
                today8, userid);

        data.put("bj", bj);
        data.put("lc", lc);
        ts111 = printTs(ts111, "里程与时长");

        data.put("onlines", buildDayTrend(userid, true));
        ts111 = printTs(ts111, "上线情况");

        data.put("alarms", buildDayTrend(userid, false));
        ts111 = printTs(ts111, "报警情况");

        ret.put("data", data);
        return ret;
    }

    private List<Map<String, Object>> buildDayTrend(String userId, boolean online) {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            String date8 = DATE8.format(d);
            int count;
            if (d.equals(LocalDate.now())) {
                if (online) {
                    count = queryForInt(
                            "SELECT COUNT(*) FROM tgps_car WHERE gpstime>=? AND gpstime<=? AND group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?)",
                            date8 + "000000", date8 + "235959", userId);
                } else {
                    count = queryForInt(
                            "SELECT COUNT(*) FROM tgps_car_alarm WHERE gpstime>=? AND gpstime<=? AND car_id IN (SELECT id FROM tgps_car WHERE group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?))",
                            date8 + "000000", date8 + "235959", userId);
                }
            } else {
                String table = online ? "tgps_index_online" : "tgps_index_alarm";
                count = queryForInt("SELECT num FROM " + table + " WHERE user_id=? AND date8=?", userId, date8);
            }

            Map<String, Object> map1 = new HashMap<>();
            map1.put("date", MMDD.format(d));
            map1.put("count", count);
            result.add(map1);
        }
        return result;
    }

    private int queryForInt(String sql, Object... args) {
        try {
            Integer v = jdbcTemplate.queryForObject(sql, Integer.class, args);
            return v == null ? 0 : v;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getApiCode() {
        return 1100;
    }

    @Override
    public String getApiName() {
        return "首页统计信息";
    }

    @Override
    public String getGroupName() {
        return "车载监控";
    }

    @Scheduled(cron = "10 59 23 * * ?")
    public void autoTj() {
        LocalDate today = LocalDate.now();
        String date8 = DATE8.format(today);
        String stime = date8 + "000000";

        List<Map<String, Object>> listUser = jdbcTemplate.queryForList("SELECT id FROM tlingx_user WHERE status=1");

        for (Map<String, Object> map : listUser) {
            Object uid = map.get("id");
            int onlineCount = queryForInt(
                    "SELECT COUNT(*) FROM tgps_car WHERE gpstime>=? AND group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?)",stime, uid);
            jdbcTemplate.update("DELETE FROM tgps_index_online WHERE user_id=? AND date8=?", uid, date8);
            jdbcTemplate.update("INSERT INTO tgps_index_online(user_id,date8,num) VALUES(?,?,?)", uid, date8, onlineCount);
        }

        for (Map<String, Object> map : listUser) {
            Object uid = map.get("id");
            int alarmCount = queryForInt(
                    "SELECT COUNT(*) FROM tgps_car_alarm WHERE gpstime>=? AND car_id IN (SELECT id FROM tgps_car WHERE group_id IN (SELECT group_id FROM tgps_group_user WHERE user_id=?))",stime, uid);
            jdbcTemplate.update("DELETE FROM tgps_index_alarm WHERE user_id=? AND date8=?", uid, date8);
            jdbcTemplate.update("INSERT INTO tgps_index_alarm(user_id,date8,num) VALUES(?,?,?)", uid, date8, alarmCount);
        }
    }

    public static long printTs(long time, String text) {
        long ts = System.currentTimeMillis();
        // System.out.println(text + ":" + (ts - time) + "ms");
        return ts;
    }
}
