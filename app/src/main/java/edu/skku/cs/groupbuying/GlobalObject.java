package edu.skku.cs.groupbuying;

import android.util.Log;

public class GlobalObject {
    private static int token;
    private static String email;
    private static int contentid;
    private static String review_host_email;
    private static boolean reviewed;

    public static void setToken(int token) { Log.d("ahoy", "token: " + token); GlobalObject.token = token; }
    public static int getToken() { return GlobalObject.token; }

    public static void setEmail(String email){ GlobalObject.email=email;}
    public static String getEmail() { return GlobalObject.email;}

    public static void setContentid(int contentid) { GlobalObject.contentid = contentid; }
    public static int getContentid() { return GlobalObject.contentid; }

    public static void setReview_host_email(String review_host_email) { GlobalObject.review_host_email = review_host_email; }
    public static String getReview_host_email() { return GlobalObject.review_host_email; }

    public static void setReviewed(boolean reviewed) { GlobalObject.reviewed = reviewed; }
    public static boolean getReviewed() { return GlobalObject.reviewed; }

}
