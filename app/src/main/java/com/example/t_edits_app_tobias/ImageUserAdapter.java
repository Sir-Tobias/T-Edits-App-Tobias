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

import java.util.ArrayList;
import java.util.List;

public class ImageUserAdapter extends RecyclerView.Adapter<ImageUserAdapter.imageViewHolder>{

    private Context uContext;
    private ArrayList<TContent> tContent;

    public ImageUserAdapter(Context context, ArrayList<TContent> content) {
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

    public void filterList(ArrayList<TContent> oContent) {
        // below line is to add our filtered
        // list in our course array list.
        tContent = oContent;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


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
