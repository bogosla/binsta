package com.bogosla.binsta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bogosla.binsta.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener {
    public static final String TAG = "MainAcitvity";
    ActivityMainBinding biding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setSupportActionBar(biding.toolbar);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        PostFragment fragmentPost = PostFragment.newInstance();
        LoginFragment fragmentHome = LoginFragment.newInstance();

//        ParseQuery<ParsePost> query = ParseQuery.getQuery(ParsePost.class);
//        query.findInBackground((posts, e) -> {
//            if (e != null) return;
//            for (ParsePost post : posts) {
//                Log.i(TAG, post.getDescription());
//            }
//        });
        biding.bnbMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Log.i(TAG, "Item "+ String.valueOf(item.getItemId()));
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = fragmentPost;
                        break;
                    case R.id.post:
                        fragment = fragmentHome;
                        break;
                    case R.id.profile:
                        fragment = PostFragment.newInstance();
                        break;
                    default:
                        fragment = fragmentPost;
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flMain, fragment).commit();
                return true;
            }
        });

        // By default display home
        biding.bnbMain.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onLoginClick(View v, String username, String password) {

    }

    @Override
    public void onGoSignup() {

    }
}