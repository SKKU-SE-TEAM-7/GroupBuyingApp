package edu.skku.cs.groupbuying.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentHomeBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private FragmentHomeBinding binding;
    private ArrayList<ItemData> mData;
    private ArrayList<ItemData> searchData = new ArrayList<>();
    private int token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initDataset();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FloatingActionButton add = binding.addNew;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.HomeToCreate(token);
            }
        });

        final Button search = binding.search;
        final EditText search_space = binding.searchSpace;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_item(search_space);
            }
        });

        hideBottomNavigation(false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.nav_view);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void initDataset() {
        Bundle bundle = getArguments();
        token = bundle.getInt("token");


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/getRecent").newBuilder();
        String url = urlBuilder.build().toString();

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

                Gson gson = new GsonBuilder().create();
                final ListModel data = gson.fromJson(myResponse, ListModel.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mData = new ArrayList<>();
                        for(int i=0; i<data.getList().length; i++){
                            mData.add(new ItemData(data.getList()[i].getContent_id(), R.drawable.ic_baseline_image_24, data.getList()[i].getTitle(),
                                    data.getList()[i].getOwner(), data.getList()[i].getDueDate(), (data.getList()[i].getTargetMember()-data.getList()[i].getCurrentMember())));
                        }
                        recyclerView = binding.recyclerView;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new RecycleViewAdapter(mData, (MainActivity) getActivity(), token);
                        recyclerView.setAdapter(adapter);
                        DividerItemDecoration dividerDecoration =
                                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        recyclerView.addItemDecoration(dividerDecoration);

                    }
                });


            }
        });

    }

    public void search_item(EditText search){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/search").newBuilder();
        urlBuilder.addQueryParameter("keyword", search.getText().toString());
        String url = urlBuilder.build().toString();

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

                Gson gson = new GsonBuilder().create();
                final ListModel data = gson.fromJson(myResponse, ListModel.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mData = new ArrayList<>();
                        for(int i=0; i<data.getList().length; i++){
                            mData.add(new ItemData(data.getList()[i].getContent_id(), R.drawable.ic_baseline_image_24, data.getList()[i].getTitle(),
                                    data.getList()[i].getOwner(), data.getList()[i].getDueDate(), (data.getList()[i].getTargetMember()-data.getList()[i].getCurrentMember())));
                        }
                        recyclerView = binding.recyclerView;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new RecycleViewAdapter(mData, (MainActivity) getActivity(), token);
                        recyclerView.setAdapter(adapter);
                        DividerItemDecoration dividerDecoration =
                                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
                        recyclerView.addItemDecoration(dividerDecoration);

                    }
                });


            }
        });




    }


}