package edu.skku.cs.groupbuying.ui.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<ItemData> mData;
    private Activity mActivity;
    private int mToken;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mTitle;
        public TextView mEmail;
        public TextView mDate;
        public TextView mLeft;
        public Button mJoin;

        ViewHolder(View itemView) {
            super(itemView) ;
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mEmail = (TextView) itemView.findViewById(R.id.item_email);
            mDate = (TextView) itemView.findViewById(R.id.item_date);
            mLeft = (TextView) itemView.findViewById(R.id.item_left);
            mJoin = (Button) itemView.findViewById(R.id.join_button);
        }
    }

    RecycleViewAdapter(ArrayList<ItemData> list, Activity activity, int token) {
        mData = list ;
        mActivity = activity;
        mToken = token;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false) ;
        RecycleViewAdapter.ViewHolder vh = new RecycleViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {
        Glide.with(mActivity).load("https://"+mData.get(position).item_image).error(R.drawable.ic_baseline_image_24).into(holder.mImage);
        holder.mTitle.setText(mData.get(position).item_title);
        holder.mEmail.setText("작성자 "+mData.get(position).item_email);
        holder.mDate.setText("마감일 "+mData.get(position).item_date);
        holder.mLeft.setText(mData.get(position).item_left+"명 남았어요!");
        final int id = mData.get(position).item_id;


    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void setItems(ArrayList<ItemData> list){
        mData = list;
        notifyDataSetChanged();
    }

}
