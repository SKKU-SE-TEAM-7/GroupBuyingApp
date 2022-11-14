package edu.skku.cs.groupbuying.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.skku.cs.groupbuying.MainActivity;
import edu.skku.cs.groupbuying.R;
import edu.skku.cs.groupbuying.databinding.FragmentLoginBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText textView = binding.emailInput;


        final EditText textView2 = binding.passwordInput;


        final Button login = binding.loginButton;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.LoginToHome();
            }
        });

        final Button register = binding.registerButton;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.LoginToRegister();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
