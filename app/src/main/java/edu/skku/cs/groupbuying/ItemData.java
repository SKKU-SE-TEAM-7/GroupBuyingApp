package edu.skku.cs.groupbuying;


public class ItemData {
    public int item_id;
    public String item_image;
    public String item_title;
    public String item_email;
    public String item_date;
    public int item_left;
    public int item_current;
    public int item_total;

    public ItemData(int item_id, String item_image, String item_title, String item_email, String item_date, int item_left, int item_current, int item_total) {
        this.item_id = item_id;
        this.item_image = item_image;
        this.item_title = item_title;
        this.item_email = item_email;
        this.item_date = item_date;
        this.item_left = item_left;
        this.item_current = item_current;
        this.item_total = item_total;
    }
}
