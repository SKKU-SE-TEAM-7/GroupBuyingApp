package edu.skku.cs.groupbuying.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import edu.skku.cs.groupbuying.ItemData;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentDetailBinding;
import edu.skku.cs.groupbuying.ui.home.ListModel;
import edu.skku.cs.groupbuying.ui.home.RecycleViewAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailFragment extends Fragment {

    private int token;
    private int id;
    private FragmentDetailBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        init();

        DetailViewModel detailViewModel =
                new ViewModelProvider(this).get(DetailViewModel.class);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void init(){
        Bundle bundle = getArguments();
        token = bundle.getInt("token");
        id = bundle.getInt("content-id");


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/get").newBuilder();
        urlBuilder.addQueryParameter("content-id", Integer.toString(id))
                .addQueryParameter("token", Integer.toString(token));
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
                final ContentModel data = gson.fromJson(myResponse, ContentModel.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });


            }
        });



    }

}