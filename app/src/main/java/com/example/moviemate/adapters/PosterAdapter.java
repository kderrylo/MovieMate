package com.example.moviemate.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviemate.R;
import com.example.moviemate.models.TVShow;

import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private List<TVShow> tvShows;

    public PosterAdapter(List<TVShow> tvShows) {
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        return new PosterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        TVShow tvShow = tvShows.get(position);
        String imageUrl = tvShow.getImage_thumbnail_path();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .fitCenter()
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
        }
    }
}
