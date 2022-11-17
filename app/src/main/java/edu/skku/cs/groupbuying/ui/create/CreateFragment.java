package edu.skku.cs.groupbuying.ui.create;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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

import java.io.ByteArrayOutputStream;
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
    public Bitmap bitmap;
    public String format;

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
                                String mimeType = getContext().getContentResolver().getType(uri[0]);
                                format = mimeType.substring(6);

                                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri[0]);
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
        String encode = "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";

        List<Pair> params = new ArrayList<Pair>();
        params.add(new Pair("title", title.getText().toString()));
        params.add(new Pair("detail", detail.getText().toString()));
        params.add(new Pair("targetMember", member.getText().toString()));
        params.add(new Pair("duedate", date.getText().toString()));
        params.add(new Pair("image", bitmapToString(bitmap)));
        params.add(new Pair("imageformat", format));
        Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!", format);

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
                final String myResponse = response.body().string();
                Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!", ""+code);
                Log.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!", myResponse);
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

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

}
