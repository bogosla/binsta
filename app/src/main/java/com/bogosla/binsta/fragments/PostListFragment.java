package com.bogosla.binsta.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogosla.binsta.EndlessRecyclerViewScrollListener;
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
    LinearLayoutManager linearLayout;
    private SwipeRefreshLayout sRefresh;
    private EndlessRecyclerViewScrollListener scrollListener;
    private PostListListener mCallback;

    public interface PostListListener {
         void onItemClick(ParsePost p, char type);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            mCallback = (PostListListener) getActivity();
        } catch(ClassCastException e) {
            throw  new ClassCastException("Must be implements PostListListener");
        }
        super.onAttach(context);
    }

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
        linearLayout = new LinearLayoutManager(getContext());
        adapter = new PostAdapter(getContext(), posts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_list, container, false);

        rcPosts = root.findViewById(R.id.rcPosts);
        sRefresh = root.findViewById(R.id.sRefresh);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore OK");
            }
        };
        rcPosts.addOnScrollListener(scrollListener);
        rcPosts.setAdapter(adapter);
        rcPosts.setHasFixedSize(true);
        rcPosts.setLayoutManager(linearLayout);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sRefresh.setOnRefreshListener(() -> getPosts()); // refresh data
        adapter.setAdapterListener((p, type) -> {
            mCallback.onItemClick(p, type); // define mainActivity
        });

        // get posts
        getPosts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.setLimit(23);
        query.addDescendingOrder("createdAt");
        query.findInBackground((objects, e) -> {
            if (e != null) {
                sRefresh.setRefreshing(false);
                Toast.makeText(getContext(), "Maybe you have no internet!!", Toast.LENGTH_SHORT).show();
                return;
            }
            posts.clear();
            posts.addAll(objects);
            adapter.notifyDataSetChanged();
            sRefresh.setRefreshing(false);
        });
    }

    public void addToList(ParsePost p) {
        posts.add(0, p);
        adapter.notifyItemInserted(0);
    }
}

