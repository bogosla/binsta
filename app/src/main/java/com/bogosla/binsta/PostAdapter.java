package com.bogosla.binsta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bogosla.binsta.databinding.PostItemBinding;
import com.bogosla.binsta.models.ParsePost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<ParsePost> posts;

    public PostAdapter(Context ctx, List<ParsePost> posts) {
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

    private class PostHolder extends RecyclerView.ViewHolder {
        PostItemBinding biding;

        public PostHolder(@NonNull PostItemBinding biding) {
            super(biding.getRoot());
            this.biding = biding;
        }
        public void onBind(ParsePost item) {
            biding.imgProfile.setImageResource(R.drawable.binsta);
            biding.tvUsername.setText(item.getUser().getUsername());
            biding.tvCreation.setText(TimeFormatter.getTimeDifference(item.getCreatedAt().toString()));

            if(item.getImage() != null)
                Glide.with(context)
                        .load(item.getImage().getUrl())
                        .transform(new RoundedCorners(12))
                        .into(biding.imgPost2);
            else
                biding.imgPost2.setImageResource(R.drawable.binsta);


            biding.tvCaption.setText(item.getDescription());
        }
    }
}
