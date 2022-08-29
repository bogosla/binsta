package com.bogosla.binsta.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bogosla.binsta.EndlessRecyclerViewScrollListener;
import com.bogosla.binsta.R;
import com.bogosla.binsta.models.ParsePost;

import java.util.ArrayList;
import java.util.List;

public class MyBaseFragment extends Fragment {
    protected LinearLayoutManager layoutManager;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView rcPosts;
    protected final List<ParsePost> posts = new ArrayList<>();
    protected SwipeRefreshLayout sRefresh;

    protected EndlessRecyclerViewScrollListener scrollListener;

    public MyBaseFragment() {
        // Required empty public constructor
    }

    public static MyBaseFragment newInstance() {
        MyBaseFragment fragment = new MyBaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMore(page, totalItemsCount, view);
            }
        };
        rcPosts.addOnScrollListener(scrollListener);
        rcPosts.setAdapter(adapter);
        rcPosts.setHasFixedSize(true);
        rcPosts.setLayoutManager(layoutManager);
        // get posts
        getPosts();
    }

    protected void loadMore(int page, int totalItemsCount, RecyclerView view) {
    }

    protected void getPosts() {
    }
}
