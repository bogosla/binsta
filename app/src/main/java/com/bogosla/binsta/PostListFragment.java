package com.bogosla.binsta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends Fragment {
    private static final String TAG = "PostListFragment";
    private RecyclerView rcPosts;
    private List<String> posts = new ArrayList<>();
    private PostAdapter adapter;
    private LinearLayoutManager linearLayout;

    public PostListFragment() {
        // Required empty public constructor
    }

    public static PostListFragment newInstance() {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_post_list, container, false);
        rcPosts = root.findViewById(R.id.rcPosts);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PostAdapter(getContext(), posts);
        linearLayout = new LinearLayoutManager(getContext());

        rcPosts.setAdapter(adapter);
        rcPosts.setHasFixedSize(true);
        rcPosts.setLayoutManager(linearLayout);

        rcPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePosition = lm.findFirstVisibleItemPosition();
                int firstCompletelyVisible = lm.findFirstCompletelyVisibleItemPosition();
                Log.i(TAG, "State: "+String.valueOf(newState) + " - " + String.valueOf(RecyclerView.SCROLL_STATE_IDLE));
                Log.i(TAG, String.valueOf(firstVisiblePosition) + " - " + String.valueOf(firstCompletelyVisible));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        setPosts();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "OnDestroy");
        posts.clear();
        super.onDestroy();
    }

    private void setPosts() {
        posts.add("Hello world");
        posts.add("James DESTINE");
        posts.add("Dave VICTOR");
        posts.add("GANG gang");
        adapter.notifyDataSetChanged();
    }
}