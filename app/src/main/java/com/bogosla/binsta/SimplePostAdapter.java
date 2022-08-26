package com.bogosla.binsta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bogosla.binsta.databinding.PostItemBinding;
import com.bogosla.binsta.databinding.SimplePostItemBinding;
import com.bogosla.binsta.models.ParsePost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;


public class SimplePostAdapter extends RecyclerView.Adapter<SimplePostAdapter.SimplePostHolder> {
    private Context context;
    private List<ParsePost> posts;

    public SimplePostAdapter(Context ctx, List<ParsePost> posts) {
        this.context = ctx;
        this.posts = posts;
    }

    @NonNull
    @Override
    public SimplePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimplePostItemBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.simple_post_item, parent, false);
        return new SimplePostHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull SimplePostHolder holder, int position) {
        holder.bind(posts.get(position));
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected class SimplePostHolder extends RecyclerView.ViewHolder {
        SimplePostItemBinding biding;

        public SimplePostHolder(@NonNull SimplePostItemBinding biding) {
            super(biding.getRoot());
            this.biding = biding;
        }

        public void bind(ParsePost item) {
            if(item.getImage() != null)
                Glide.with(context)
                        .load(item.getImage().getUrl())
                        .transform(new RoundedCorners(12))
                        .into(biding.simpleImgPost2);
            else
                biding.simpleImgPost2.setImageResource(R.drawable.binsta);
        }
    }
}
