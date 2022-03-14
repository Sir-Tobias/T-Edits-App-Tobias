package com.example.t_edits_app_tobias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageViewHolder>{

    private Context mContext;
    private List<TContent> tContent;

    public ImageAdapter(Context context, List<TContent> content) {
        mContext = context;
        tContent = content;
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_view, parent, false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        //TContent currentTedits = tContent.get(position);
        holder.viewOne.setText(tContent.get(position).getContentCreatedOne());
        holder.viewTwo.setText(tContent.get(position).getContentCreatedTwo());
        holder.viewThree.setText(tContent.get(position).getContentCreatedThree());
        holder.viewFour.setText(tContent.get(position).getContentCreatedFour());
        Picasso.get().load(tContent.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return tContent.size();
    }

//    public void startListening() {
//    }

    public class imageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView viewOne;
        public TextView viewTwo;
        public TextView viewThree;
        public TextView viewFour;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_single_view);
            viewOne = itemView.findViewById(R.id.contentOne);
            viewTwo = itemView.findViewById(R.id.contentTwo);
            viewThree = itemView.findViewById(R.id.contentThree);
            viewFour = itemView.findViewById(R.id.contentFour);
        }
    }
}
