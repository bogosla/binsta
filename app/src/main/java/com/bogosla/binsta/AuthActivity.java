package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bogosla.binsta.fragments.LoginFragment;
import com.bogosla.binsta.fragments.SignupFragment;
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

    public void login(String username, String password, IndeterminateDialog dialog) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Toast.makeText(this, "Error while logging In!", Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
                return;
            }
            if (dialog != null)
                dialog.dismiss();
            goMainActivity();
        });
    }

    public void signup(String password, String username, String email, IndeterminateDialog dialog) {
        ParseUser user = new ParseUser();
        user.setPassword(password);
        user.setUsername(username);
        user.setEmail(email);

        user.signUpInBackground(e -> {
            if (e != null) {
                Toast.makeText(this, "Error while signing up!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
            Toast.makeText(AuthActivity.this, "Successfully !", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            login(user.getUsername(), password, null);
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
    public void onLoginClick(View v, String username, String password, IndeterminateDialog dialog) {
        login(username, password, dialog);
    }

    @Override
    public void onGoSignup() {
        cur = 1;
        switchTo(cur);
    }

    @Override
    public void onSignupClick(View v, String username, String password, String email, IndeterminateDialog dialog) {
        signup(password, username, email, dialog);
    }

    @Override
    public void onGoLogin() {
        cur = 0;
        switchTo(cur);
    }
}