package com.cosc3p97.newshub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosc3p97.newshub.dao.AppDatabase;
import com.cosc3p97.newshub.models.Model;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;
    private List<Model> modelList;
    private int lastPosition = -1;
    private AppDatabase database;  // Reference to the Room database

    public Adapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
        this.database = AppDatabase.getDatabase(context);  // Initialize the database
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model model = modelList.get(position);

        holder.headlines.setText(model.getTitle() != null ? model.getTitle() : "Headline unavailable");
        holder.mainNews.setText(model.getDescription() != null ? model.getDescription() : "Description unavailable");
        holder.author.setText(model.getAuthor() != null ? model.getAuthor() : "Unknown author");
        holder.publishedAt.setText(model.getPublishedAt() != null ? model.getPublishedAt() : "Date unknown");

        // Load the image
        Glide.with(context)
                .load(model.getUrlToImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_shape)
                .into(holder.imageView);

        // Set bookmark icon based on database status
        new Thread(() -> {
            Model bookmarkedModel = database.bookmarkDao().getBookmark(model.getUrl());
            holder.bookmarkIcon.post(() -> holder.bookmarkIcon.setImageResource(
                    bookmarkedModel != null ? R.drawable.ic_bookmarked : R.drawable.ic_add_bookmark
            ));
        }).start();

        // Bookmark icon click listener
        holder.bookmarkIcon.setOnClickListener(view -> {
            new Thread(() -> {
                Model bookmarkedModel = database.bookmarkDao().getBookmark(model.getUrl());
                if (bookmarkedModel != null) {
                    // If the article is already bookmarked, remove it
                    database.bookmarkDao().deleteBookmark(model);
                    holder.bookmarkIcon.post(() -> {
                        holder.bookmarkIcon.setImageResource(R.drawable.ic_add_bookmark);
                        Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Otherwise, bookmark it
                    database.bookmarkDao().insertBookmark(model);
                    holder.bookmarkIcon.post(() -> {
                        holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmarked);
                        Toast.makeText(context, "Bookmarked", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

        // Handle the news item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadNewsActivity.class);
            intent.putExtra("URL", model.getUrl());
            context.startActivity(intent);
        });

        // Set animation
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return modelList != null ? modelList.size() : 0;
    }

    /**
     * Animates item if it's displayed for the first time.
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * Updates the adapter's data with DiffUtil for better performance.
     */
    public void updateList(List<Model> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return modelList != null ? modelList.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Assuming each article has a unique URL
                return modelList.get(oldItemPosition).getUrl().equals(newList.get(newItemPosition).getUrl());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return modelList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        });

        modelList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * ViewHolder: Holds references to the views for each item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headlines, mainNews, author, publishedAt;
        ImageView imageView, bookmarkIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headlines = itemView.findViewById(R.id.headline);
            mainNews = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.newsImageView);
            author = itemView.findViewById(R.id.author);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
        }
    }
}
