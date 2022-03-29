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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageViewHolder> implements SearchView.OnQueryTextListener {
    //public class ImageAdapter extends RecyclerView.Adapter<com.example.t_edits_app_tobias.ImageAdapter.imageViewHolder> implements Filterable, SearchView.OnQueryTextListener

    //Creating Variables for the arraylist and context
    private Context mContext;
    private List<TContent> tContent;
    private ArrayList<TContent> uContent;
    private ArrayList<TContent> newContent;
    //private ArrayList<TContent> tContent;


    //Creating a constructor for my variables
    // first change Public ImageAdapter(Context context, List<TContent> content){
    public ImageAdapter(Context context, ArrayList<TContent> uContent) {
        //tContent = content;
        mContext = context;
        this.uContent = uContent;
        //tContent = content;
    }
    //second change Method for filtering recycler view items
    public void filterList(ArrayList<TContent> oContent) {
        // below line is to add our filtered
        // list in our course array list.
        uContent = oContent;
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

    //    public ImageAdapter(Context context, List<TContent> content) {
//        mContext = context;
//        tContent = content;
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
    public ImageAdapter(ArrayList<TContent> uContent) {
        this.uContent = uContent;
        newContent = new ArrayList<>(uContent);
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

        holder.viewOne.setText(uContent.get(position).getContentCreatedOne());
        holder.viewTwo.setText(uContent.get(position).getContentCreatedTwo());
        holder.viewThree.setText(uContent.get(position).getContentCreatedThree());
        holder.viewFour.setText(uContent.get(position).getContentCreatedFour());
        Picasso.get().load(uContent.get(position).getImageUri()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uContent.size();
    }

//    //Method for filtering the arraylist
//    private Filter examplefilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            List<TContent> tContents = new ArrayList<>();
//            if(charSequence==null|| charSequence.length()==0) {
//                tContents.addAll(uContent);
//            }
//            else {
//                String pattern = charSequence.toString().toLowerCase().trim();
//                for (TContent item : uContent) {
//                    if (item.getContentCreatedOne().toLowerCase().contains(pattern)) {
//                        tContents.add(item);
//
//                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
//                        tContents.add(item);
//
//                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
//                        tContents.add(item);
//
//                    } else if (item.getContentCreatedTwo().toLowerCase().contains(pattern)) {
//                        tContents.add(item);
//                    }
//                }
//            }
//            FilterResults filterResults = new FilterResults();
//            filterResults.values=tContents;
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults results) {
//            tContent.clear();
//            if(results.values instanceof List){
//                for(TContent item : (List<TContent>)results.values){
//                    if(item instanceof TContent) {
//                        tContent.add((TContent) item);
//                    }
//                }
//            }
//            //tContent.addAll((List)results.values);
//            notifyDataSetChanged();
//
//        }
//    };

}
