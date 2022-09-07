package kz.smrtx.techmerch.api;

public class StringQuery {
    private StringQuery stringQuery;

    public StringQuery getStringQuery() {
        return stringQuery;
    }

    public static String getUser(String login, String password) {
        return "select u.USE_ID, u.USE_LOGIN, u.USE_PASSWORD, u.USE_NAME, u.USE_USR_CODE," +
                " r.USR_NAME, u.USE_LOC_CODE, u.USE_VERSION_CODE, u.USE_LAST_SYNC_DATE, u.USE_LANG " +
                "from dbo.ST_USER u, dbo.ST_USER_ROLE r " +
                "where u.USE_USR_CODE = r.USR_CODE " +
                "and USE_LOGIN='" + login + "' and USE_PASSWORD='" + password + "'";
    }

    public static String changePassword(String user, String old, String newP) {
        return "update dbo.ST_USER" +
                " set USE_PASSWORD='" + newP +
                "' where USE_ID=" + user +
                " and USE_PASSWORD='" + old + "'" +
                " select @@ROWCOUNT as 'rows'";
    }
}
