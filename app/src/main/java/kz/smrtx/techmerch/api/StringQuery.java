package kz.smrtx.techmerch.api;

public class StringQuery {
    private StringQuery stringQuery;

    public StringQuery getStringQuery() {
        return stringQuery;
    }

    public static String getUser(String login, String password) {
        return "select u.USE_ID, u.USE_CODE, u.USE_LOGIN, u.USE_PASSWORD, u.USE_NAME, u.USE_USR_CODE," +
                " r.USR_NAME, u.USE_LOC_CODE, u.USE_VERSION_CODE, u.USE_LAST_SYNC_DATE, u.USE_LANG " +
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
}
