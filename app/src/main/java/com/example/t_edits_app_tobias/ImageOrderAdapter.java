package com.example.t_edits_app_tobias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageOrderAdapter extends RecyclerView.Adapter<ImageOrderAdapter.imageViewHolder> implements SearchView.OnQueryTextListener {
    //public class ImageAdapter extends RecyclerView.Adapter<com.example.t_edits_app_tobias.ImageAdapter.imageViewHolder> implements Filterable, SearchView.OnQueryTextListener

    //Creating Variables for the arraylist and context
    private Context mContext;
    private List<TPost> tPost;
    private ArrayList<TPost> uPost;
    private ArrayList<TPost> newPost;
    //private ArrayList<TContent> tContent;


    //Creating a constructor for my variables
    // first change Public ImageAdapter(Context context, List<TContent> content){
    public ImageOrderAdapter(Context context, ArrayList<TPost> uPost) {
        //tContent = content;
        mContext = context;
        this.uPost = uPost;
        //tContent = content;
    }
    //second change Method for filtering recycler view items
    public void filterList(ArrayList<TPost> oPost) {
        // below line is to add our filtered
        // list in our course array list.
        uPost = oPost;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class imageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView postname;
        public TextView postcaption;
        public TextView desName;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_post_view);
            postname = itemView.findViewById(R.id.nameText);
            postcaption = itemView.findViewById(R.id.DescriptionText);
            desName = itemView.findViewById(R.id.designerText);
        }
    }
    public ImageOrderAdapter(ArrayList<TPost> uPost) {
        this.uPost = uPost;
        newPost = new ArrayList<>(uPost);
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_view, parent, false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        //TContent currentTedits = tContent.get(position);

        holder.desName.setText("Designer ~ "+uPost.get(position).getNameOfDesigner());
        holder.postname.setText(uPost.get(position).getNameOfPost());
        holder.postcaption.setText(uPost.get(position).getCaption());
        Picasso.get().load(uPost.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uPost.size();
    }

}
