package edu.skku.cs.groupbuying.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<ItemData> mData;
    private Activity mActivity;
    private int mToken;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImage;
        public TextView mTitle;
        public TextView mDate;
        public ProgressBar mProgress;
        public TextView mLeft;
        public Button mJoin;

        ViewHolder(View itemView) {
            super(itemView) ;
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mDate = (TextView) itemView.findViewById(R.id.item_date);
            mProgress = (ProgressBar) itemView.findViewById(R.id.item_progressBar);
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
        holder.mImage.setClipToOutline(true);
        Glide.with(mActivity).load("https://"+mData.get(position).item_image).error(R.drawable.ic_baseline_image_24).into(holder.mImage);
        holder.mTitle.setText(mData.get(position).item_title);
        holder.mDate.setText("????????? "+mData.get(position).item_date);
        holder.mProgress.setMax(mData.get(position).item_total);
        holder.mProgress.setProgress(mData.get(position).item_current);
        holder.mLeft.setText(mData.get(position).item_left+"??? ????????????!");
        final int id = mData.get(position).item_id;

        holder.mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle result = new Bundle();
                //result.putInt("token", mToken);
                //result.putInt("content-id", id);
                GlobalObject.setContentid(id);
                NavController navController = Navigation.findNavController(mActivity, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_home_to_navigation_detail);//, result);
            }
        });
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
