package com.basudev.onlinewallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context context;
    private List<ImageModel> wallpaperModelList;

    public ImageAdapter(Context context, List<ImageModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(context).load(wallpaperModelList.get(position).getMediumUrl()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,FullscreenImage.class)
                        .putExtra("originalUrl",wallpaperModelList.get(position).getOriginalUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }
}
class ImageViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageViewItem);
    }
}
