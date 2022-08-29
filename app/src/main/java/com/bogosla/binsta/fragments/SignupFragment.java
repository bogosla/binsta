package com.bogosla.binsta.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bogosla.binsta.IndeterminateDialog;
import com.bogosla.binsta.R;


public class SignupFragment extends Fragment {
    public static final String TAG = "SignupFragment";
    private EditText edUsername;
    private EditText edPassword;
    private EditText edEmail;
    private Button btnSignup;
    private Button gotoLogin;
    private SignListener listener;


    public interface SignListener {
        void onSignupClick(View v, String username, String password, String email, IndeterminateDialog dialog);
        void onGoLogin();
    }


    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SignListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Must implements LoginListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        edUsername = root.findViewById(R.id.edUsername2);
        edPassword = root.findViewById(R.id.edPassword2);
        edEmail = root.findViewById(R.id.edEmail);
        btnSignup = root.findViewById(R.id.btnSignup);
        gotoLogin = root.findViewById(R.id.goLogin);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignup.setOnClickListener(v -> {
            if (edUsername.getText().toString().trim().isEmpty() ||
                    edPassword.getText().toString().trim().isEmpty() ||
                    edEmail.getText().toString().trim().isEmpty())
            {
                Toast.makeText(getContext(), "Please, fill all fields!!", Toast.LENGTH_SHORT).show();
                return;
            }
            IndeterminateDialog dl = IndeterminateDialog.newInstance("Signing Up", "Whip pip pip!! Hey!! Hey!!");
            dl.setCancelable(false);
            dl.show(getActivity().getSupportFragmentManager(), "signup");
            listener.onSignupClick(btnSignup, edUsername.getText().toString(), edPassword.getText().toString(), edEmail.getText().toString(), dl);
        });

        gotoLogin.setOnClickListener(v -> listener.onGoLogin());
    }
}