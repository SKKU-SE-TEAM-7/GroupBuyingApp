package edu.skku.cs.groupbuying;

public class GlobalObject {
    private static int token;

    public static void setToken(int token) { GlobalObject.token = token; }
    public static int getToken() { return GlobalObject.token; }
}
