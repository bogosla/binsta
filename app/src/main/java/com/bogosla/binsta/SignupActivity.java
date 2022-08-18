package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bogosla.binsta.databinding.ActivitySignupBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding biding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);
        EditText username = biding.edUsername;
        EditText password = biding.edPassword;
        EditText fullname = biding.edFullname;

        Button btnSignup = biding.btnSignup;

        btnSignup.setOnClickListener(view -> {
            if (username.getText().toString().trim().isEmpty() ||
                    password.getText().toString().trim().isEmpty() ||
                        fullname.getText().toString().trim().isEmpty())
            {
                return;
            }
            signup(password.getText().toString(), username.getText().toString(), fullname.getText().toString());

        });

        biding.goLogin.setOnClickListener(view -> {
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }

    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) return;
            Intent goMain = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(goMain);
            finish();
        });
    }


    private void signup(String password, String username, String fullname) {
        ParseUser user = new ParseUser();
        user.setPassword(password);
        user.setUsername(username);
        user.put("fullname", fullname);

        user.signUpInBackground(e -> {
            if (e != null) return;
            Toast.makeText(SignupActivity.this, "Successfully signedUp !", Toast.LENGTH_LONG).show();
            login(user.getUsername(), password);
        });
    }
}