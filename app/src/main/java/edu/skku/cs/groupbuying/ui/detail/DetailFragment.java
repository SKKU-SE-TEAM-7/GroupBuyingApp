package edu.skku.cs.groupbuying.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentDetailBinding;
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
    public String img_url;
    public int chat_id;
    public boolean user_join = false;
    public boolean open = false;
    public String owner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bundle bundle = getArguments();

//        init();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        init();

        DetailViewModel detailViewModel =
                new ViewModelProvider(this).get(DetailViewModel.class);

        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final FloatingActionButton show = binding.show;
        final ExtendedFloatingActionButton detail_join = binding.detailJoin;
        final ExtendedFloatingActionButton detail_chat = binding.detailChat;
        final ExtendedFloatingActionButton detail_exit = binding.detailExit;

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!open){
                    detail_join.setVisibility(View.VISIBLE);
                    detail_chat.setVisibility(View.VISIBLE);
                    detail_exit.setVisibility(View.VISIBLE);
                    show.setImageResource(R.drawable.ic_baseline_close_24);
                    open = true;
                }
                else{
                    detail_join.setVisibility(View.GONE);
                    detail_chat.setVisibility(View.GONE);
                    detail_exit.setVisibility(View.GONE);
                    show.setImageResource(R.drawable.ic_baseline_add_24);
                    open = false;
                }

            }
        });

        detail_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_join();
            }
        });

        detail_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/user/joinlist").newBuilder();
                urlBuilder.addQueryParameter("token", Integer.toString(token));
                String url = urlBuilder.build().toString();

                Request req = new Request.Builder()
                        .url(url)
                        .build();

                CountDownLatch countDownLatch = new CountDownLatch(1);

                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        final int code = response.code();
                        final String myResponse = response.body().string();
                        JsonArray joinlist = JsonParser.parseString(myResponse).getAsJsonObject().get("joinlist").getAsJsonArray();
                        for (int i = 0; i < joinlist.size(); i++) {
                            if (joinlist.get(i).getAsInt() == id) {
                                user_join = true;
                                break;
                            }
                        }
                        countDownLatch.countDown();
                    }
                });

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(user_join){
                    GlobalObject.setReview_host_email(owner);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.DetailToChat(chat_id, id);
                }
                else{
                    Toast toast=Toast.makeText(getContext(),"아직 공동구매에 참가하지 않았거나 확인할 수가 없습니다",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        detail_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_exit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    public void init() {
        token = GlobalObject.getToken();
        id = GlobalObject.getContentid();

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/get").newBuilder();
        urlBuilder.addQueryParameter("content-id", Integer.toString(id))
                .addQueryParameter("token", Integer.toString(token));
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final int code = response.code();
                final String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                Log.d("ahoy", url);
                Log.d("ahoy", myResponse);
                final ContentModel data = gson.fromJson(myResponse, ContentModel.class);
                img_url = "https://"+data.getContent().getImage_url();
                chat_id = data.getContent().getChat_id();
                owner = data.getContent().getOwner();

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ImageView detail_image = binding.detailImage;
                        final TextView detail_title = binding.detailTitle;
                        final ProgressBar progressBar = binding.progressBar;
                        final TextView detail_member = binding.detailMember;
                        final TextView detail_date = binding.detailDate;
                        final TextView detail_detail = binding.detailDetail;

                        Glide.with(activity).load(img_url).error(R.drawable.ic_baseline_image_24).into(detail_image);
                        detail_title.setText(data.getContent().getTitle());
                        progressBar.setMax(data.getContent().getTargetMember());
                        progressBar.setProgress(data.getContent().getCurrentMember());
                        detail_member.setText(data.getContent().getCurrentMember()+"명 참여 / "+data.getContent().getTargetMember()+"명 목표");
                        detail_date.setText(data.getContent().getDueDate()+" 종료");
                        detail_detail.setText("상품 설명 / 공동구매 진행 안내\n\n"+data.getContent().getDetail());

                        getHost(data.getContent().getOwner());
                    }
                });

                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getHost(String email){
        OkHttpClient client1 = new OkHttpClient();

        HttpUrl.Builder urlBuilder1 = HttpUrl.parse("http://52.78.137.254:8080/user/getinfo").newBuilder();
        urlBuilder1.addQueryParameter("email", email);
        String url1 = urlBuilder1.build().toString();

        Request req1 = new Request.Builder()
                .url(url1)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client1.newCall(req1).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final int code = response.code();
                final String myResponse1 = response.body().string();

                Gson gson1 = new GsonBuilder().create();
                final UserModel data = gson1.fromJson(myResponse1, UserModel.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final TextView detail_nickname = binding.detailNickname;
                        final TextView detail_star = binding.detailStar;
                        final RatingBar detail_rate = binding.ratingBar;

                        detail_nickname.setText(data.getUser_info().getNickname());
                        detail_rate.setRating(data.getUser_info().getStar());
                        detail_star.setText("평가 "+data.getUser_info().getStar()+" / 5");
                    }
                });

                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void user_join(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/join").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(token))
                .addQueryParameter("content-id", Integer.toString(id));
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final int code = response.code();
                final String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final DetailMessage data = gson.fromJson(myResponse, DetailMessage.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 200){
                            Toast toast=Toast.makeText(getContext(),"공동구매 참가 완료",Toast.LENGTH_SHORT);
                            toast.show();
                            user_join = true;
                        }
                        else{
                            if(data.getMessage().equals("wrong content id")){
                                Toast toast=Toast.makeText(getContext(),"공동구매 컨텐트 ID가 틀립니다",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else if(data.getMessage().equals("Exceed Member")){
                                Toast toast=Toast.makeText(getContext(),"이미 인원이 가득 찼습니다.",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                Toast toast=Toast.makeText(getContext(),"이미 참가한 공동구매 입니다",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                    }
                });

                init();
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void user_exit(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/canceljoin").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(token))
                .addQueryParameter("content-id", Integer.toString(id));
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final int code = response.code();
                final String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final DetailMessage data = gson.fromJson(myResponse, DetailMessage.class);

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 200){
                            Toast toast=Toast.makeText(getContext(),"공동구매 탈퇴 완료",Toast.LENGTH_SHORT);
                            toast.show();
                            user_join = false;
                        }
                        else{
                            if(data.getMessage().equals("wrong content id")){
                                Toast toast=Toast.makeText(getContext(),"공동구매 컨텐트 ID가 틀립니다",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                Toast toast=Toast.makeText(getContext(),"아직 참여하지 않은 공동구매 입니다",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                    }
                });
                init();
                countDownLatch.countDown();

            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}