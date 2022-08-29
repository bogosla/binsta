package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.bogosla.binsta.databinding.ActivityDetailBinding;
import com.bogosla.binsta.fragments.DetailFragment;
import com.bogosla.binsta.fragments.ProfileUserFragment;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding biding;
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.activity_detail);
        setSupportActionBar(biding.activityDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent fromMain = getIntent();

        if (fromMain != null && fromMain.getStringExtra("type") != null) {
            String type = fromMain.getStringExtra("type");

            if ("D".equals(type))
                current = DetailFragment.newInstance(fromMain.getParcelableExtra("post"));
            else if("P".equals(type))
                current = ProfileUserFragment.newInstance(fromMain.getParcelableExtra("post"));

            getSupportFragmentManager().beginTransaction().replace(R.id.activity_detail_fl, current).commit();
        }
    }
}