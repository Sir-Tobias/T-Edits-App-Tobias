package com.example.t_edits_app_tobias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageElementAdapter extends RecyclerView.Adapter<ImageElementAdapter.imageViewHolder> {

    private Context mContext;
    private List<TElement> tElement;

    //private ArrayList<TContent> tContent;

    public ImageElementAdapter(Context context, List<TElement> element) {
        tElement = element;
        mContext = context;

    }

    public class imageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView viewOne;


        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_element_view);
            viewOne = itemView.findViewById(R.id.elementDisplayText);
        }
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_view3, parent, false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        //TContent currentTedits = tContent.get(position);

        holder.viewOne.setText(tElement.get(position).getElementCreatedOne());
        Picasso.get().load(tElement.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return tElement.size();
    }

    };
