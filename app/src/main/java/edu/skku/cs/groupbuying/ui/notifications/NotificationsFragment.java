package edu.skku.cs.groupbuying.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.databinding.FragmentNotificationsBinding;
import edu.skku.cs.groupbuying.ui.home.ListModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.util.Log;
import android.widget.EditText;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;

    private edu.skku.cs.groupbuying.ui.notifications.RecycleViewAdapter adapter;
    private ArrayList<ItemData> mData;
    private ArrayList<ItemData> searchData = new ArrayList<>();
    private int token;
    private int count=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        final EditText nicknameText = binding.textView;
        final String nickName;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);





        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/user/getinfo").newBuilder();
        urlBuilder.addQueryParameter("email", GlobalObject.getEmail());
        Log.d("emailcheck",GlobalObject.getEmail());
        String url = urlBuilder.build().toString();


        ArrayList<String> contentid = new ArrayList<>();

        Request req = new Request.Builder()
                .url(url)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final int code = response.code();

                final String myResponse = response.body().string();
                Log.d("checkpoint",myResponse);
                Gson gson = new GsonBuilder().create();
                final NotificationsModel data = gson.fromJson(myResponse, NotificationsModel.class);
                ArrayList<ItemData> mData = new ArrayList<>();
                for(int i=0; i<data.getUser_info().getJoin_content().size(); i++){
                    Log.d("hi",data.getUser_info().getJoin_content().get(i));
                    contentid.add(data.getUser_info().getJoin_content().get(i));
                }
                textView.setText(data.getUser_info().getNickname());


                for(int i=0; i<contentid.size();i++){


                    OkHttpClient client2 = new OkHttpClient();

                    HttpUrl.Builder urlBuilder2 = HttpUrl.parse("http://52.78.137.254:8080/content/get").newBuilder();
                    urlBuilder2.addQueryParameter("token",Integer.toString(GlobalObject.getToken()));
                    urlBuilder2.addQueryParameter("content-id", contentid.get(i));
                    String url2 = urlBuilder2.build().toString();
                    Request req2 = new Request.Builder()
                            .url(url2)
                            .build();

                    client2.newCall(req2).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final int code = response.code();
                            final String myResponse = response.body().string();
                            Log.d("ha", String.valueOf(myResponse.charAt(2)));
                            if (myResponse.charAt(2)=='c'){
                                Gson gson = new GsonBuilder().create();
                                final ListModel2 data2 = gson.fromJson(myResponse, ListModel2.class);

                                Log.d("finish", Integer.toString(data2.getContent().getContent_id()));
                                mData.add(new ItemData(data2.getContent().getContent_id(), data2.getContent().getImage_url(), data2.getContent().getTitle(),
                                        data2.getContent().getOwner(), data2.getContent().getDueDate(), (data2.getContent().getTargetMember() - data2.getContent().getCurrentMember()),
                                        data2.getContent().getCurrentMember(), data2.getContent().getTargetMember()));
                                MainActivity activity = (MainActivity) getActivity();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//
                                        recyclerView = binding.recyclerView;
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        adapter = new edu.skku.cs.groupbuying.ui.notifications.RecycleViewAdapter(mData, (MainActivity) getActivity(), token);
                                        recyclerView.setAdapter(adapter);
                                        DividerItemDecoration dividerDecoration =
                                                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
                                        recyclerView.addItemDecoration(dividerDecoration);
                                    }
                                });
                            }








                        }

                    });
                }
                if(count==contentid.size()) {
                    Log.d("abcd", Integer.toString(mData.size()));

                }



            }
        });











        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
