package org.audit4j.handler.db;

public class Utils {
    /** The Constant EMBEDED_DB_NAME. */
    static final String EMBEDED_DB_NAME = "audit4j";
    
    public static String getDBName(String userName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(EMBEDED_DB_NAME);
        buffer.append("@");
        buffer.append(userName);
        return buffer.toString();
    }
   
}
