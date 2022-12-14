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

public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    private EditText edUsername;
    private EditText edPassword;
    private Button btnLogin;
    private Button gotoSignup;

    private LoginListener listener;

    public interface LoginListener {
        void onLoginClick(View v, String username, String password, IndeterminateDialog dialog);
        void onGoSignup();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (LoginListener) getActivity();
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
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        edUsername = root.findViewById(R.id.edUsername2);
        edPassword = root.findViewById(R.id.edPassword2);
        btnLogin = root.findViewById(R.id.btnLogin);
        gotoSignup = root.findViewById(R.id.goSignup);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogin.setOnClickListener(v -> {
            if (edUsername.getText().toString().trim().isEmpty() || edPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Empty field !", Toast.LENGTH_SHORT).show();
                return;
            }
            IndeterminateDialog dl = IndeterminateDialog.newInstance("Logging In", "Whip pip pip!!");
            dl.setCancelable(false);
            dl.show(getActivity().getSupportFragmentManager(), "login");

            listener.onLoginClick(btnLogin, edUsername.getText().toString(), edPassword.getText().toString(), dl);
        });

        gotoSignup.setOnClickListener(v -> listener.onGoSignup());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}