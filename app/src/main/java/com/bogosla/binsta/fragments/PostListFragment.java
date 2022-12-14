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

import com.bogosla.binsta.CommentAdapter;
import com.bogosla.binsta.EndlessRecyclerViewScrollListener;
import com.bogosla.binsta.PostAdapter;
import com.bogosla.binsta.R;
import com.bogosla.binsta.models.ParseComment;
import com.bogosla.binsta.models.ParsePost;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PostListFragment extends Fragment {
    private static final String TAG = "PostListFragment";
    private static final int LIMIT = 20;
    private RecyclerView rcPosts;
    private final List<ParsePost> posts = new ArrayList<>();
    private PostAdapter adapter;
    LinearLayoutManager linearLayout;
    private SwipeRefreshLayout sRefresh;
    private EndlessRecyclerViewScrollListener scrollListener;
    private PostListListener mCallback;

    ParseQuery<ParsePost> parseQuery;
    ParseLiveQueryClient parseLiveQueryClient = null;



    public interface PostListListener {
         void onItemClick(ParsePost p, String type);
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
                getNextPage(page);
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
        sRefresh.setOnRefreshListener(() -> {
            getPosts();

        }); // refresh data
        adapter.setAdapterListener((p, type) -> {
            mCallback.onItemClick(p,  type); // define mainActivity
        });

        // get posts
        getPosts();


        String websocketUrl = "wss://binsta.b4a.io";
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        parseQuery = ParseQuery.getQuery(ParsePost.class);
        parseQuery.include(ParsePost.USER_KEY);
        parseQuery.whereNotEqualTo(ParsePost.USER_KEY, ParseUser.getCurrentUser());

        // Connect to Parse server
        SubscriptionHandling<ParsePost> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            query.include(ParsePost.USER_KEY);
            query.getInBackground(object.getObjectId(), (object2, e) -> {
                if (e == null) {
                    posts.add(0, object2);
                }
            });


            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.setLimit(LIMIT);
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
            scrollListener.resetState();

        });
    }

    private void getNextPage(int page) {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        try {
            int count = query.count(); // Total items in db
            int skip = (LIMIT * page) + 1;
            query.setLimit(LIMIT);
            query.setSkip(skip);
            query.addDescendingOrder("createdAt");

            if (skip >= count) return; // if totalItems less than skip, no need to make call
            query.findInBackground((objects, e) -> {
                if (e != null) {
                    sRefresh.setRefreshing(false);
                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
                sRefresh.setRefreshing(false);
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void addToList(ParsePost p) {
        posts.add(0, p);
        adapter.notifyItemInserted(0);
    }
}

