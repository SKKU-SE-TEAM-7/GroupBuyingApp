package edu.skku.cs.groupbuying.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;

import edu.skku.cs.groupbuying.ChatData;
import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.HttpRequestGet;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.networkobject.ResponseChatGetlist;
import edu.skku.cs.groupbuying.databinding.FragmentDashboardBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FragmentDashboardBinding binding;
    private ArrayList<ChatData> mData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("ahoy", "start of oncreate");
        super.onCreate(savedInstanceState);
        initDataset();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("ahoy", "start of oncreateview");
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        //initDataset();

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.chatroomList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(mData, (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerDecoration);

        Log.d("ahoy", "end of oncreateview");

        hideBottomNavigation(false);
        return root;
    }

    /*
    @Override
    public void onResume() {
        Log.d("ahoy", "onresume");
        super.onResume();
        //initDataset();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
        Log.d("ahoy", "ondestroyview");
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void initDataset() {
        Log.d("ahoy", "start of initdataset");
        mData = new ArrayList<ChatData>();

        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/chat/getlist").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();
                Log.d("ahoy", "onresp: " + responseStr);

                ResponseChatGetlist response = new ResponseChatGetlist(responseStr);

                for (int i = 0; i < response.getChatlist().size(); i++) {
                    mData.add(new ChatData(R.drawable.ic_baseline_image_24, "chat id: " + Integer.toString(response.getChatlist().get(i).getChatid())));
                }
            }
        });

        mData.add(new ChatData(R.drawable.ic_baseline_image_24, "chat id: test"));
        Log.d("ahoy", "end of initdataset");
    }
}