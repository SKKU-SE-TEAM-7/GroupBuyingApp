package edu.skku.cs.groupbuying.ui.chat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
    private int contentid;
    private String chattitle;
    private Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        /*
        if (GlobalObject.getReviewed()) {
            GlobalObject.setReviewed(false);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_chat_to_navigation_dashboard);
        }*/

        init();

        ChatViewModel homeViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView chat_title = binding.chatTitle;
        final String server_adrs = "http://52.78.137.254:8080";
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/content/get").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
        urlBuilder.addQueryParameter("content-id", Integer.toString(contentid));
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder().url(url).build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { chattitle = ""; countDownLatch.countDown(); }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                String responseStr = resp.body().string();

                chattitle = JsonParser.parseString(responseStr).getAsJsonObject().get("content").getAsJsonObject().get("title").getAsString();
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        chat_title.setText(chattitle);

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

        ExtendedFloatingActionButton chat_recv = binding.moveReview;
        chat_recv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/chat/receive").newBuilder();
                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/canceljoin").newBuilder();
                urlBuilder.addQueryParameter("token", Integer.toString(GlobalObject.getToken()));
                urlBuilder.addQueryParameter("content-id", Integer.toString(contentid));
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
                        Log.d("ahoy", "receive: " + responseStr);

                        countDownLatch.countDown();
                    }
                });

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                NavController navController = Navigation.findNavController(mActivity, R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_navigation_chat_to_navigation_review);
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
        Bundle bundle = getArguments();
        chatid = bundle.getInt("chat-id");
        contentid = bundle.getInt("content-id");

        update_chat();
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