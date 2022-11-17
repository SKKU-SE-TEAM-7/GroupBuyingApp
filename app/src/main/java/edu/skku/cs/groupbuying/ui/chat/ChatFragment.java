package edu.skku.cs.groupbuying.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Locale;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentChatBinding;
import edu.skku.cs.groupbuying.databinding.FragmentHomeBinding;
import edu.skku.cs.groupbuying.networkobject.ResponseChatGetchat;
import edu.skku.cs.groupbuying.networkobject.ResponseChatGetlist;
import edu.skku.cs.groupbuying.networkobject.chat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FragmentChatBinding binding;
    private ArrayList<Chat> mData;
    private int chatid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel homeViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView chat_title = binding.chatTitle;
        chat_title.setText(Integer.toString(chatid));

        /*
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecycleViewAdapter(mData, (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerDecoration =
                new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerDecoration);*/


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

    private void init() {
        Log.d("ahoy", "chatfrag init");
        Bundle bundle = getArguments();
        chatid = bundle.getInt("chat-id");
        Log.d("ahoy", "chatfrag init chatid: " + Integer.toString((chatid)));

        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/chat/getchat").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
        urlBuilder.addQueryParameter("chat-id", Integer.toString(chatid));
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();
                Log.d("ahoy", "onresp: " + responseStr);

                ResponseChatGetchat response = new ResponseChatGetchat(responseStr);

                for (int i = 0; i < response.getChatinfo().getChats().size(); i++) {
                    chat ch = response.getChatinfo().getChats().get(i);

                    mData.add(new Chat(ch.getSender(), ch.getText(), ch.getTime()));
                }
            }
        });
    }
}