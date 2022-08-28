package com.bogosla.binsta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.bogosla.binsta.databinding.ActivityMainBinding;
import com.bogosla.binsta.fragments.PostFragment;
import com.bogosla.binsta.fragments.PostListFragment;
import com.bogosla.binsta.fragments.ProfileFragment;
import com.bogosla.binsta.models.ParsePost;
import com.parse.ParseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ProfileFragment.ProfileListener, PostFragment.PostListener, PostListFragment.PostListListener {
    static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    Fragment current;

    PostFragment fragmentPost;
    ProfileFragment fragmentUser;
    PostListFragment fragmentList;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // Create post fragment
        fragmentPost = PostFragment.newInstance();
        // Profile fragment
        fragmentUser = (ProfileFragment) new ProfileFragment();
        // List of post fragment
        fragmentList = PostListFragment.newInstance();

        current = fragmentPost; // Default post creation
        fragmentManager.beginTransaction().add(R.id.flMain, fragmentList).hide(fragmentList).commit();
        fragmentManager.beginTransaction().add(R.id.flMain, fragmentUser).hide(fragmentUser).commit();
        fragmentManager.beginTransaction().add(R.id.flMain, fragmentPost).commit();

        binding.bnbMain.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    fragmentManager.beginTransaction().hide(current).show(fragmentList).commit();
                    current = fragmentList;
                    break;
                case R.id.post:
                    fragmentManager.beginTransaction().hide(current).show(fragmentPost).commit();
                    current = fragmentPost;
                    break;
                default:
                    fragmentManager.beginTransaction().hide(current).show(fragmentUser).commit();
                    current = fragmentUser;
                    break;
            }
            return true;
        });

        // By default display home
        binding.bnbMain.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoggedOut() {
        gotoLogin();
    }

    private void gotoLogin() {
        Intent i = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemAdded(ParsePost p) {
        fragmentList.addToList(p);
    }

    public void goToDetail(ParsePost p, String type) {
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra("post", p);
        i.putExtra("type", type);
        startActivity(i);
    }

    @Override
    public void onItemClick(ParsePost p, String type) {
        Toast.makeText(this, ""+type, Toast.LENGTH_SHORT).show();
        if ("P".equals(type) && p.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            binding.bnbMain.setSelectedItemId(R.id.profile);

            return;
        }
        goToDetail(p, type);
    }
}