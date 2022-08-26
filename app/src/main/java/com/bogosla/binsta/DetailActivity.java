package com.bogosla.binsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.bogosla.binsta.databinding.ActivityDetailBinding;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding biding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        biding = DataBindingUtil.setContentView(DetailActivity.this, R.layout.activity_detail);
        setSupportActionBar(biding.dtToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}