package edu.skku.cs.groupbuying.ui.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import edu.skku.cs.groupbuying.ChatData;
import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ChatData> mData;
    private Activity mActivity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mTitle;
        public Button mJoin;

        ViewHolder(View itemView) {
            super(itemView) ;
            mImage = (ImageView) itemView.findViewById(R.id.chatroom_image);
            mTitle = (TextView) itemView.findViewById(R.id.chatroom_title);
            mJoin = (Button) itemView.findViewById(R.id.chatroom_join_button);
        }
    }

    RecyclerViewAdapter(ArrayList<ChatData> list, Activity activity) {
        mData = list ;
        mActivity = activity;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatroom, parent, false) ;
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Glide.with(mActivity).load("https://" + mData.get(position).item_img).error(R.drawable.ic_baseline_image_24).into(holder.mImage);
        //holder.mImage.setImageResource(R.drawable.ic_baseline_image_24);
        holder.mTitle.setText(mData.get(position).title);
        int chatid = mData.get(position).chatid;
        int contentid = mData.get(position).contentid;
        holder.mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("chat-id", chatid);
                bundle.putInt("content-id", contentid);
                GlobalObject.setReview_host_email(mData.get(position).host_email);
                NavController navController = Navigation.findNavController(mActivity, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_dashboard_to_navigation_chat, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void setItems(ArrayList<ChatData> list){
        mData = list;
        notifyDataSetChanged();
    }
}