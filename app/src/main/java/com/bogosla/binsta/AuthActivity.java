package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

public class AuthActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignupFragment.SignListener {
    public static final String TAG = "AuthActivity";
    int cur = 0; // 0 Login, 1 SignUp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) goMainActivity(); // Already signed In, go to mainActivity.
        else
            Log.i(TAG, "user null!!");
        switchTo(cur);
    }

    private void goMainActivity() {
        Intent goMain = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(goMain);
        finish();
    }

    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) return; // todo helpful error
            goMainActivity();
        });
    }

    public void signup(String password, String username, String email) {
        ParseUser user = new ParseUser();
        user.setPassword(password);
        user.setUsername(username);
        user.setEmail(email);

        user.signUpInBackground(e -> {
            Log.i(TAG, "Signup"+e.toString());
            if (e != null) return; // todo helpful error handling
            Toast.makeText(AuthActivity.this, "Successfully !", Toast.LENGTH_LONG).show();
            login(user.getUsername(), password);
        });
    }

    public void switchTo(int w) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f;
        if (w == 0)
            f = LoginFragment.newInstance();
        else
            f = SignupFragment.newInstance();
        ft.replace(R.id.flForm, f);
        ft.commit();

    }

    @Override
    public void onLoginClick(View v, String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty())
            return;
        login(username, password);
    }

    @Override
    public void onGoSignup() {
        cur = 1;
        switchTo(cur);
    }

    @Override
    public void onSignupClick(View v, String username, String password, String email) {
        if (username.trim().isEmpty() ||
                password.trim().isEmpty() ||
                email.trim().isEmpty())
        {
            return;
        }
        signup(password, username, email);
    }

    @Override
    public void onGoLogin() {
        cur = 0;
        switchTo(cur);
    }
}