package edu.skku.cs.groupbuying.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.skku.cs.groupbuying.HttpRequestGet;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentRegisterBinding;
import edu.skku.cs.groupbuying.ui.login.LoginDataModel;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button enroll = binding.joinButton;
        final Button check_button = binding.checkButton;

        final EditText email = binding.joinEmail;
        final EditText password = binding.joinPassword;
        final EditText password_verification = binding.joinPwck;
        final EditText verification_code = binding.validationCode;
        final EditText nickname = binding.nickname;

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_email(email);
            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_password(password, password_verification)){
                    register_to_server(verification_code, email, password, nickname);
                }
            }
        });

        hideBottomNavigation(true);
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

    public boolean check_password(EditText pass, EditText re_pass){
        String password = pass.getText().toString();
        String re_password = re_pass.getText().toString();
        if(password.equals(re_password)){
            return true;
        }
        else{
            Toast toast=Toast.makeText(getContext(),"입력한 두 비밀번호가 다릅니다",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
    }

    public void verify_email(EditText email){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/user/authcode").newBuilder();
        urlBuilder.addQueryParameter("user_email", email.getText().toString());
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

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 200){
                            Toast toast=Toast.makeText(getContext(),"인증번호가 전송되었습니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else if(code == 201) {
                            Toast toast=Toast.makeText(getContext(),"이미 존재하는 이메일입니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast=Toast.makeText(getContext(),"성균관대 이메일 형식이 아닙니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
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

    public void register_to_server(EditText verify_code, EditText email, EditText password, EditText nickname){

        OkHttpClient client = new OkHttpClient();

        List<Pair> params = new ArrayList<Pair>();
        params.add(new Pair("user_email", email.getText().toString()));
        params.add(new Pair("user_password", password.getText().toString()));
        params.add(new Pair("nickname", nickname.getText().toString()));
        params.add(new Pair("authcode", verify_code.getText().toString()));
        byte[] postData = CreateQuery(params, "UTF-8");

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/user/register").newBuilder();
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),postData))
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

                MainActivity activity = (MainActivity) getActivity();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(code == 200){
                            MainActivity activity = (MainActivity) getActivity();
                            activity.RegisterToLogin();
                            Toast toast=Toast.makeText(getContext(),"회원가입이 완료되었습니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else if(code == 201) {
                            Toast toast=Toast.makeText(getContext(),"메일 인증번호가 틀립니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast=Toast.makeText(getContext(),"이미 존재하는 이메일입니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
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
