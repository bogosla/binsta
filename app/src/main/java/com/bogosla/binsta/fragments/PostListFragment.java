package com.bogosla.binsta.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogosla.binsta.PostAdapter;
import com.bogosla.binsta.R;
import com.bogosla.binsta.models.ParsePost;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends Fragment {
    private static final String TAG = "PostListFragment";
    private RecyclerView rcPosts;
    private final List<ParsePost> posts = new ArrayList<>();
    private PostAdapter adapter;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_list, container, false);
        rcPosts = root.findViewById(R.id.rcPosts);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PostAdapter(getContext(), posts);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        rcPosts.setAdapter(adapter);
        rcPosts.setHasFixedSize(true);
        rcPosts.setLayoutManager(linearLayout);

        // get posts
        getPosts();
    }

    @Override
    public void onDestroy() {
        // posts.clear();
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.setLimit(33);
        query.addDescendingOrder("createdAt");
        query.findInBackground((objects, e) -> {
            if (e != null) {
                return;
            }
            posts.addAll(objects);
            adapter.notifyDataSetChanged();
        });
    }
}