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

public class ImagePalletteAdapter extends RecyclerView.Adapter<ImagePalletteAdapter.imageViewHolder> {

    private Context mContext;
    private ArrayList<TPallette> pElement;

    private ImageView checkoutButton;

    //private ArrayList<TContent> tContent;

    public ImagePalletteAdapter(Context context, ArrayList<TPallette> element) {
        pElement = element;
        mContext = context;

    }

    public void filterList(ArrayList<TPallette> oContent) {
        // below line is to add our filtered
        // list in our course array list.
        pElement = oContent;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
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

        //holder.viewOne.setText(pElement.get(position).getPalletteImage());
        holder.viewOne.setText("T-Edits Element");
        Picasso.get().load(pElement.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pElement.size();
    }

    };
