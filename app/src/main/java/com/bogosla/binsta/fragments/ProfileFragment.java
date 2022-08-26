package com.bogosla.binsta.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bogosla.binsta.PostAdapter;
import com.bogosla.binsta.R;
import com.bogosla.binsta.SimplePostAdapter;
import com.bogosla.binsta.models.ParsePost;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ProfileFragment extends MyBaseFragment {
    private static final String TAG = "ProfileFragment";
    private Button btnLogout;
    private ProfileListener listener;

    public interface ProfileListener {
        void onLoggedOut();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ProfileListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "Must implements ProfileListener");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new SimplePostAdapter(getContext(), posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        rcPosts = root.findViewById(R.id.profileRcPosts);
        btnLogout = root.findViewById(R.id.btnLogout);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) return;
                        listener.onLoggedOut();
                    }
                });
            }
        });
    }

    @Override
    protected void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.whereEqualTo(ParsePost.USER_KEY, ParseUser.getCurrentUser());
        query.setLimit(21);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParsePost>() {
            @Override
            public void done(List<ParsePost> objects, ParseException e) {
                if (e == null) {
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}