package com.bogosla.binsta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bogosla.binsta.databinding.CommentItemBinding;
import com.bogosla.binsta.databinding.SimplePostItemBinding;
import com.bogosla.binsta.models.ParseComment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    Context context;
    List<ParseComment> comments;

    public CommentAdapter(Context ctx, List<ParseComment> comments) {
        this.context = ctx;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentItemBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_item, parent, false);

        return new CommentHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.onBind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    protected class CommentHolder extends RecyclerView.ViewHolder {
        CommentItemBinding binding;

        public CommentHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void onBind(ParseComment item) {
            binding.xText.setText(item.getText());
            try {
                binding.byUser.setText(item.getUser().getUsername());
            } catch (Exception e) {}
        }
    }
}
