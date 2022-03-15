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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageViewHolder> implements Filterable {

    private Context mContext;
    private List<TContent> tContent;
    private List<TContent> uContent;
    //private ArrayList<TContent> tContent;

    public ImageAdapter(Context context, ArrayList<TContent> content) {
        mContext = context;
        tContent = content;
    }

//    public ImageAdapter(Context context, List<TContent> content) {
//        mContext = context;
//        tContent = content;
//    }
    @Override
    public Filter getFilter() {
        return examplefilter;
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

    //Method for filtering the arraylist
    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TContent> tContents = new ArrayList<>();
            if(charSequence==null|| charSequence.length()==0) {
                tContents.addAll(uContent);
            }
            else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (TContent item : uContent) {
                    if (item.getContentCreatedOne().toLowerCase().contains(pattern)) {
                        tContents.add(item);

                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
                        tContents.add(item);

                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
                        tContents.add(item);

                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
                        tContents.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=tContents;
            return filterResults;
        };


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            tContent.clear();
            tContent.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

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
    ImageAdapter(List<TContent> tContent) {
        this.tContent = tContent;
        uContent = new ArrayList<>(tContent);
    }
}
