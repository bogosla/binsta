package com.bogosla.binsta.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogosla.binsta.CommentAdapter;
import com.bogosla.binsta.CreateComment;
import com.bogosla.binsta.databinding.FragmentDetailBinding;
import com.bogosla.binsta.models.ParseComment;
import com.bogosla.binsta.models.ParseLike;
import com.bogosla.binsta.models.ParsePost;
import com.bumptech.glide.Glide;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {
    FragmentDetailBinding binding;
    ParsePost post;
    CommentAdapter adapter;
    List<ParseComment> comments = new ArrayList<>();
    ParseLiveQueryClient parseLiveQueryClient = null;
    ParseQuery<ParseComment> parseQuery;

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
        // Enter the websocket URL of your Parse server

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

        binding.fragmentDetailNewComment.setOnClickListener(view1 -> showCreationForm());

        Glide.with(getContext()).load(post.getImage().getUrl()).into(binding.fragmentDetailImagePost);

        String websocketUrl = "wss://binsta.b4a.io";
        RecyclerView rcComents = binding.fragmentDetailRcComments;

        adapter = new CommentAdapter(getContext(), comments);
        rcComents.setAdapter(adapter);
        rcComents.setHasFixedSize(true);
        rcComents.setLayoutManager(new LinearLayoutManager(getContext()));

        getComments();

        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        parseQuery = ParseQuery.getQuery(ParseComment.class);
        parseQuery.include(ParseComment.POST_KEY);
        parseQuery.include(ParseComment.USER_KEY);
        parseQuery.whereEqualTo(ParseComment.POST_KEY, post);

        // Connect to Parse server
        SubscriptionHandling<ParseComment> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            try {
                binding.fragmentDetailComments.setText(String.format("Got %d comments", query.count()));
            }catch (Exception e){}
            comments.add(0, object);

            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        count_like();
        super.onResume();
    }

    private void showCreationForm() {
        CreateComment fr = new CreateComment();
        fr.setListener(text -> createComment(text, ParseUser.getCurrentUser(), post, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) Toast.makeText(getContext(), "Successful!", Toast.LENGTH_SHORT).show();
                fr.dismiss();
            }
        }));
        fr.show(getActivity().getSupportFragmentManager(), "cr_comment");

    }

    private void createComment(String text, ParseUser curUser, ParsePost post, SaveCallback callback) {
        ParseComment comment = new ParseComment();
        comment.setText(text);
        comment.setUser(curUser);
        comment.setPost(post);
        comment.saveInBackground(callback);
    }

    private void getComments() {
        ParseQuery<ParseComment> query = new ParseQuery<>(ParseComment.class);
        query.include(ParseComment.USER_KEY);
        query.whereEqualTo(ParseComment.POST_KEY, post);
        query.addDescendingOrder("createdAt");
        try {
            binding.fragmentDetailComments.setText(String.format("Got %d comments", query.count()));
        }catch (Exception e){}

        query.findInBackground((objects, e) -> {
            if (e == null) {
                comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void count_like() {
        ParseQuery<ParseLike> query = new ParseQuery<>(ParseLike.class);
        query.countInBackground((count, e) -> {
            if (e == null)
                binding.fragmentDetailLikes.setText(String.format("Got %d likes", count));
            else
                binding.fragmentDetailLikes.setText(String.format("You don't have likes", count));
        });
    }
}