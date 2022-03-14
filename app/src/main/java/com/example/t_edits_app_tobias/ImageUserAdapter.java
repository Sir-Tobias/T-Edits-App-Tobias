package com.example.t_edits_app_tobias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageUserAdapter extends RecyclerView.Adapter<ImageUserAdapter.imageViewHolder>{

    private Context uContext;
    private List<TContent> tContent;

    public ImageUserAdapter(Context context, List<TContent> content) {
        uContext = context;
        tContent = content;
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(uContext).inflate(R.layout.single_view2, parent, false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        //TContent currentTedits = tContent.get(position);
        Picasso.get().load(tContent.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return tContent.size();
    }


    public class imageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;


        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_user_view);
        }
    }
}
