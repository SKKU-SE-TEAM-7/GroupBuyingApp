package edu.skku.cs.groupbuying;

public class GlobalObject {
    private static int token;
    private static String email;
    private static int contentid;

    public static void setToken(int token) { GlobalObject.token = token; }
    public static int getToken() { return GlobalObject.token; }

    public static void setEmail(String email){ GlobalObject.email=email;}
    public static String getEmail() { return GlobalObject.email;}

    public static void setContentid(int contentid) { GlobalObject.contentid = contentid; }
    public static int getContentid() { return GlobalObject.contentid; }
}
