package edu.skku.cs.groupbuying.ui.dashboard;

import android.app.Activity;
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

import java.util.ArrayList;

import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ItemData> mData;
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

    RecyclerViewAdapter(ArrayList<ItemData> list, Activity activity) {
        mData = list ;
        mActivity = activity;

        Log.d("ahoy", mData.get(0).item_title);
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chatroom, parent, false) ;
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mImage.setImageResource(mData.get(position).item_img);
        holder.mTitle.setText(mData.get(position).item_title);
        holder.mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(mActivity, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_home_to_navigation_detail);
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
