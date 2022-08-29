package com.bogosla.binsta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogosla.binsta.SimplePostAdapter;
import com.bogosla.binsta.databinding.FragmentProfileUserBinding;
import com.bogosla.binsta.models.ParsePost;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileUserFragment extends MyBaseFragment {
    public static final String POST_KEY = "post";
    FragmentProfileUserBinding binding;
    ParsePost post;


    public ProfileUserFragment() {
        // Required empty public constructor
    }

    public static ProfileUserFragment newInstance(ParsePost post) {
        ProfileUserFragment fragment = new ProfileUserFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST_KEY, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new SimplePostAdapter(getContext(), posts);

        if (getArguments() != null) {
            post = getArguments().getParcelable(POST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileUserBinding.inflate(inflater, container, false);
        this.rcPosts = binding.fragmentProfileUserRcPosts;
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentProfileUserUsername.setText(post.getUser().getUsername());
        if (post.getUser().get("profile") != null)
            Glide.with(getContext()).load(((ParseFile)post.getUser().get("profile")).getUrl()).into(binding.fragmentProfileUserImgProfile);
    }

    @Override
    protected void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.whereEqualTo(ParsePost.USER_KEY, post.getUser());

        query.addDescendingOrder("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}