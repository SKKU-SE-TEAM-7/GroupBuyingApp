package edu.skku.cs.groupbuying.ui.review;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.skku.cs.groupbuying.ChatData;
import edu.skku.cs.groupbuying.GlobalObject;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentReviewBinding;
import edu.skku.cs.groupbuying.networkobject.NetworkSnip;
import edu.skku.cs.groupbuying.networkobject.ResponseChatGetlist;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GlobalObject.setReviewed(true);
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView review_host = binding.reviewHost;
        RatingBar review_rating = binding.reviewRating;
        Button review_btn = binding.reviewButton;

        String host_email = GlobalObject.getReview_host_email();

        String host_nickname = NetworkSnip.getNicknamebyEmail(host_email);
        review_host.setText(host_nickname);

        review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String server_adrs = "http://52.78.137.254:8080";
                OkHttpClient client = new OkHttpClient();
                HttpUrl.Builder urlBuilder = HttpUrl.parse(server_adrs + "/user/givereview").newBuilder();
                urlBuilder.addQueryParameter("email", GlobalObject.getReview_host_email());
                urlBuilder.addQueryParameter("star", Integer.toString((int)review_rating.getRating()));

                String url = urlBuilder.build().toString();
                Request req = new Request.Builder().url(url).build();

                CountDownLatch countDownLatch = new CountDownLatch(1);

                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) { countDownLatch.countDown(); }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response resp) throws IOException {
                        String responseStr = resp.body().string();

                        Log.d("ahoy", "review: " + responseStr);

                        countDownLatch.countDown();
                    }
                });

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
