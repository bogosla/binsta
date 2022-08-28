package com.bogosla.binsta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogosla.binsta.databinding.FragmentDetailBinding;
import com.bogosla.binsta.models.ParsePost;
import com.bumptech.glide.Glide;


public class DetailFragment extends Fragment {
    FragmentDetailBinding binding;
    ParsePost post;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(ParsePost post) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            post = getArguments().getParcelable("post");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentDetailCreation.setText(String.format("Created by %s, %s", post.getUser().getUsername(), post.getRelDate()));
        binding.fragmentDetailCaption.setText(post.getDescription());
        Glide.with(getContext()).load(post.getImage().getUrl()).into(binding.fragmentDetailImagePost);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}