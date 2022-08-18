package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bogosla.binsta.databinding.ActivityLoginBinding;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding biding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) goMainActivity(); // Already signed In, go to mainActivity.


        EditText username = biding.edUsername;
        EditText password = biding.edPassword;
        Button btnLogin = biding.btnLogin;

        password.setOnClickListener(view -> {
            if (username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())
                return;
            login(username.getText().toString(), password.getText().toString());
        });

        biding.goSignup.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        });
    }
    private void goMainActivity() {
        Intent goMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(goMain);
        finish();
    }

    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) return;
            goMainActivity();
        });
    }
}