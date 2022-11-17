package edu.skku.cs.groupbuying;

public class ChatData {
    public int profile_img;
    public String title;
    public int chatid;

    public ChatData(int item_img, String item_title, int chatid) {
        this.profile_img = item_img;
        this.title = item_title;
        this.chatid = chatid;
    }
}
