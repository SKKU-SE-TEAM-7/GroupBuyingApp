package edu.skku.cs.groupbuying.ui.create;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.ContentResolver;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentCreateBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CreateFragment extends Fragment {

    private int token;
    private FragmentCreateBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        token = bundle.getInt("token");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateViewModel createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);

        Bundle bundle = getArguments();
        token = bundle.getInt("token");


        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DatePickerDialog.OnDateSetListener callbackMethod;
        final Uri[] uri = new Uri[1];
        final Button selectImageBtn = binding.selectImageBtn;
        final ImageView imageView = binding.imageView2;

        final EditText title = binding.title;
        final EditText maxPeople = binding.textView5;
        final EditText setCalender = binding.textView9;
        final EditText detailExplanation = binding.textView7;
        final Button finishButton = binding.create;

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enroll(title, maxPeople, setCalender, detailExplanation);
            }
        });






        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){
                    @Override
                    public void onActivityResult(ActivityResult result){
                        if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                            uri[0] = result.getData().getData();
                            try{

                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri[0]);
                                imageView.setImageBitmap(bitmap);
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                    }

                }
        );

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void enroll(TextView title, TextView member, TextView date, TextView detail){
        OkHttpClient client = new OkHttpClient();

        List<Pair> params = new ArrayList<Pair>();
        params.add(new Pair("title", title.getText().toString()));
        params.add(new Pair("detail", detail.getText().toString()));
        params.add(new Pair("targetMember", member.getText().toString()));
        params.add(new Pair("dueDate", date.getText().toString()));
        byte[] postData = CreateQuery(params, "UTF-8");

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://52.78.137.254:8080/content/new").newBuilder();
        urlBuilder.addQueryParameter("token", Integer.toString(token));
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),postData))
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
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
                            activity.CreateToHome(token);
                            Toast toast=Toast.makeText(getContext(),"성공적으로 생성했습니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            Toast toast=Toast.makeText(getContext(),"생성에 실패했습니다",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        });
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
