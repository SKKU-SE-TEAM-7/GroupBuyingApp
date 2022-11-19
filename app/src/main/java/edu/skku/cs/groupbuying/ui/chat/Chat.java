package edu.skku.cs.groupbuying.ui.chat;

import edu.skku.cs.groupbuying.networkobject.NetworkSnip;

public class Chat {
    //public int profile_img;
    public String sender;
    public String text;
    public String time;

    public Chat(String sender, String text, String time) {
        //this.profile_img = profile_img;
        this.sender = NetworkSnip.getNicknamebyEmail(sender);
        this.text = text;
        this.time = time;
    }
}
