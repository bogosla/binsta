package com.bogosla.binsta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bogosla.binsta.databinding.PostItemBinding;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<String> posts;

    public PostAdapter(Context ctx, List<String> posts) {
        this.context = ctx;
        this.posts = posts;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.post_item, parent, false);
        return new PostHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PostHolder)holder).onBind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private static class PostHolder extends RecyclerView.ViewHolder {
        PostItemBinding biding;

        public PostHolder(@NonNull PostItemBinding biding) {
            super(biding.getRoot());
            this.biding = biding;
        }
        public void onBind(String item) {
            biding.tvUsername.setText(item);
        }
    }
}
