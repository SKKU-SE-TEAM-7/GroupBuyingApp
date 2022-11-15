package edu.skku.cs.groupbuying.ui.register;

import android.os.Bundle;
import android.util.Log;
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

import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentRegisterBinding;
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

            }
        });

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_password(password, password_verification)){
                    register_to_server(email, password, nickname);

                    MainActivity activity = (MainActivity) getActivity();
                    activity.RegisterToLogin();
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

    public void register_to_server(EditText email, EditText password, EditText nickname){
        /*
        OkHttpClient client = new OkHttpClient();

        RegisterDataModel data = new RegisterDataModel();
        data.setUser_email(email.getText().toString());
        data.setUser_password(password.getText().toString());
        data.setNickname(nickname.getText().toString());

        Gson gson = new Gson();
        String json = gson.toJson(data, RegisterDataModel.class);

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/user/register").newBuilder();
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),json))
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                Log.e("error", myResponse);
                //Toast toast=Toast.makeText(getContext(),""+myResponse,Toast.LENGTH_SHORT);
                //toast.show();
            }
        });*/
    }

}
