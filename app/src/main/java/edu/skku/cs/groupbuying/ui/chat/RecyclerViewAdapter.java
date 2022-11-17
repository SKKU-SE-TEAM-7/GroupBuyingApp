package edu.skku.cs.groupbuying.ui.chat;

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
    private ArrayList<Chat> mData;
    private Activity mActivity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mProfile;
        public TextView mText;
        public TextView mTime;

        ViewHolder(View itemView) {
            super(itemView) ;
            mProfile = (ImageView) itemView.findViewById(R.id.chat_profile);
            mText = (TextView) itemView.findViewById(R.id.chat_text);
            mTime = (TextView) itemView.findViewById(R.id.chat_time);
        }
    }

    RecyclerViewAdapter(ArrayList<Chat> list, Activity activity) {
        Log.d("ahoy", "adapter: " + Integer.toString(list.size()));
        mData = list ;
        mActivity = activity;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_chat, parent, false) ;
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mProfile.setImageResource(mData.get(position).profile_img);
        holder.mText.setText(mData.get(position).text);
        holder.mTime.setText(mData.get(position).time);
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void setItems(ArrayList<Chat> list){
        mData = list;
        notifyDataSetChanged();
    }
}
