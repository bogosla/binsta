package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.bogosla.binsta.databinding.ActivityMainBinding;
import com.bogosla.binsta.models.ParsePost;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainAcitvity";
    ActivityMainBinding biding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        setSupportActionBar(biding.toolbar);

//        ParseQuery<ParsePost> query = ParseQuery.getQuery(ParsePost.class);
//        query.findInBackground((posts, e) -> {
//            if (e != null) return;
//            for (ParsePost post : posts) {
//                Log.i(TAG, post.getDescription());
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}