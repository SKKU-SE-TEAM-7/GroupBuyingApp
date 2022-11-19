package edu.skku.cs.groupbuying.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.net.URLEncoder;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;

import edu.skku.cs.groupbuying.databinding.FragmentChatBinding;
import edu.skku.cs.groupbuying.networkobject.ResponseChatGetchat;
import edu.skku.cs.groupbuying.networkobject.chat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FragmentChatBinding binding;
    private ArrayList<Chat> mData = new ArrayList<>();
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
        chat_title.setText(Integer.toString(chatid)+"번 채팅방");

        EditText chat_type = binding.chatType;

        ImageButton chat_sendbtn = binding.chatSendbtn;

        recyclerView = binding.chatList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(mData, (MainActivity) getActivity());
        recyclerView.setAdapter(adapter);
        //DividerItemDecoration dividerDecoration =
        //        new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        //recyclerView.addItemDecoration(dividerDecoration);

        chat_sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = chat_type.getText().toString();
                chat_type.setText("");

                OkHttpClient client = new OkHttpClient();
                List<Pair> params = new ArrayList<Pair>();
                params.add(new Pair("text", text));
                byte[] postData = CreateQuery(params, "UTF-8");

                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/chat/sendchat").newBuilder();
                urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
                urlBuilder.addQueryParameter("chat-id", Integer.toString(chatid));
                String url = urlBuilder.build().toString();
                Request req = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),postData)).build();

                CountDownLatch countDownLatch = new CountDownLatch(1);
                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                        String responseStr = resp.toString();
                        countDownLatch.countDown();
                    }
                });

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                update_chat();
                adapter.setItems(mData);
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

    private void init() {
        Log.d("ahoy", "chatfrag init");
        Bundle bundle = getArguments();
        chatid = bundle.getInt("chat-id");
        Log.d("ahoy", "chatfrag init chatid: " + Integer.toString((chatid)));

        update_chat();

        Log.d("ahoy", "end of chatfrag init: size: " + mData.size());
    }

    private void update_chat() {
        mData = new ArrayList<>();

        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/chat/getchat").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
        urlBuilder.addQueryParameter("chat-id", Integer.toString(chatid));
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { countDownLatch.countDown(); }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();

                ResponseChatGetchat response = new ResponseChatGetchat(responseStr);

                for (int i = 0; i < response.getChatinfo().getChats().size(); i++) {
                    chat ch = response.getChatinfo().getChats().get(i);

                    mData.add(new Chat(ch.getSender(), ch.getText(), ch.getTime()));
                }

                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static byte[] CreateQuery(List<Pair> pairs, String charset) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Pair pair : pairs) {

                if (first)
                    first = false;
                else
                    result.append('&');

                result.append(URLEncoder.encode((String) pair.first, charset));
                result.append('=');
                result.append(URLEncoder.encode((String) pair.second, charset));
            }
        }
        catch( Exception e ) {

        }
        return result.toString().getBytes();
    }
}