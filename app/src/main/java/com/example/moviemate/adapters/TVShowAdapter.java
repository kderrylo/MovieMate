package com.example.moviemate.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.R;
import com.example.moviemate.databinding.ItemContainerTvShowBinding;
import com.example.moviemate.models.TVShow;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewholder> {

    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;

    public TVShowAdapter(List<TVShow> tvShows){
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public TVShowAdapter.TVShowViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_container_tv_show, parent, false);
        return new TVShowViewholder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.TVShowViewholder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class TVShowViewholder extends RecyclerView.ViewHolder {
        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewholder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow){
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
        }
    }
}
