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
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    List<ParsePost> posts;
    private PostAdapterListener listener;

    public void setAdapterListener(PostAdapterListener listener) {
        this.listener = listener;
    }

    public interface PostAdapterListener {
        void onItemClick(ParsePost p, String type);
    }

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
            try {
                if (item.getUser().get("profile") != null )
                    Glide.with(context).load(((ParseFile)item.getUser().get("profile")).getUrl()).into(biding.imgProfile);
                else
                    biding.imgProfile.setImageResource(R.drawable.binsta);
            }catch (Exception e){
                biding.imgProfile.setImageResource(R.drawable.binsta);
            }

            biding.tvUsername.setText(item.getUser().getUsername());
            biding.tvCreation.setText(item.getRelDate());

            if(item.getImage() != null)
                Glide.with(context)
                        .load(item.getImage().getUrl())
                        .transform(new RoundedCorners(12))
                        .into(biding.imgPost2);
            else
                biding.imgPost2.setImageResource(R.drawable.binsta);

            // Listener on img, to go to detail activity
            biding.imgPost2.setOnClickListener(view -> listener.onItemClick(item, "D"));
            biding.tvUsername.setOnClickListener(view -> listener.onItemClick(item, "P"));
            biding.imgProfile.setOnClickListener(view -> listener.onItemClick(item, "P"));

            biding.tvCaption.setText(item.getDescription());
        }
    }
}
