package kz.smrtx.techmerch.api;

import android.content.Context;
import android.util.Log;

import kz.smrtx.techmerch.Ius;

public class StringQuery {

    public static String getUser(String login, String password) {
        return "select u.USE_ID, u.USE_CODE, u.USE_LOGIN, u.USE_PASSWORD, u.USE_NAME, u.USE_USR_CODE," +
                " r.USR_NAME as USE_USR_NAME, u.USE_LOC_CODE, u.USE_APP_VERSION, u.USE_LAST_SYNC_DATE, u.USE_LANG " +
                "from dbo.ST_USER u, dbo.ST_USER_ROLE r " +
                "where u.USE_USR_CODE = r.USR_CODE " +
                "and USE_LOGIN='" + login + "' and USE_PASSWORD='" + password + "'";
    }

    public static String getUserCity(int userCode) {
        return "select USC_LOC_CODE" +
                " from dbo.RS_USER_LOCATION" +
                " where USC_USE_CODE = " + userCode;
    }

    public static String changePassword(String user, String old, String newP) {
        return "update dbo.ST_USER" +
                " set USE_PASSWORD='" + newP +
                "' where USE_ID=" + user +
                " and USE_PASSWORD='" + old + "'" +
                " select @@ROWCOUNT as 'rows'";
    }

    public static String changeLanguage(String userCode, String language) {
        return "update dbo.ST_USER" +
                " set USE_LANG='" + language +
                "' where USE_CODE=" + userCode +
                " select @@ROWCOUNT as 'rows'";
    }

    public static String changeVersionCode(String userCode, int versionCode) {
        return "update dbo.ST_USER" +
                " set USE_VERSION_CODE=" + versionCode +
                " where USE_CODE=" + userCode +
                " select @@ROWCOUNT as 'rows'";
    }

    public static String getTablesUpdated() {
        return "select TAU_TABLE_NAME, TAU_LAST_UPDATE from WT_TABLES_UPDATED";
    }

    public static String getNewSupplies(String cities) {
        cities = cities.replace('-', ',');
        return "SELECT swj.WAJ_ID, sw2.WAR_NAME AS WAR_NAME_SOURCE, sw.WAR_NAME, CONCAT('x', swj.WAJ_CHANGE, ' ', swc.WAC_CONTENT_NAME) as WAC_CONTENT_NAME, swj.WAJ_DATE, swj.WAJ_CHANGE from ST_WAREHOUSE_JOURNAL swj\n" +

                "INNER JOIN ST_WAREHOUSE_CONTENT swc\n" +
                "ON swc.WAC_ID = swj.WAJ_WAC_ID\n" +
                "INNER JOIN ST_WAREHOUSE sw\n" +
                "ON sw.WAR_ID = swc.WAC_WAR_ID\n" +
                "INNER JOIN ST_WAREHOUSE_CONTENT swc2\n" +
                "ON swc2.WAC_ID = swj.WAJ_WAC_ID_SOURCE\n" +
                "inner JOIN ST_WAREHOUSE sw2\n" +
                "ON sw2.WAR_ID = swc2.WAC_WAR_ID\n" +

                "WHERE swj.WAJ_MANAGER_ACCEPTED=0 AND sw.WAR_LOC_CODE IN (" + cities + ")";
    }

    public static String acceptNewSupplies(Context context, String date) {
        String newDate = Ius.getDateByFormat(Ius.getDateFromString(date, "yyyy-MM-dd'T'HH:mm:ss"), "yyyy-MM-dd");
        String q = "UPDATE ST_WAREHOUSE_JOURNAL SET WAJ_MANAGER_ACCEPTED = 1, WAJ_DATE_ACCEPTED = getdate(), WAJ_USE_CODE = " +
        Ius.readSharedPreferences(context, Ius.USER_CODE) +
                " WHERE CAST (DATEDIFF (DAY, 0, WAJ_DATE) AS datetime) = '" + newDate + "'";
        Log.e("sss", q);
        return "UPDATE ST_WAREHOUSE_JOURNAL SET WAJ_MANAGER_ACCEPTED = 1, WAJ_DATE_ACCEPTED = getdate(), WAJ_USE_CODE = " +
                Ius.readSharedPreferences(context, Ius.USER_CODE) +
                " WHERE CAST (DATEDIFF (DAY, 0, WAJ_DATE) AS datetime) = '" + newDate + "'";
    }
}
