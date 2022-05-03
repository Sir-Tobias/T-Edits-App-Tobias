package com.example.t_edits_app_tobias;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Context oContext;
    private List<TOrder> tOrder;
    private ArrayList<TOrder> uOrder;
    private ArrayList<TOrder> newOrder;
    //private ArrayList<TContent> tContent;

    private RadioGroup designerOption;



    //Creating a constructor for my variables
    // first change Public ImageAdapter(Context context, List<TContent> content){
    public ImageOrderAdapter(Context context, ArrayList<TOrder> uOrder) {
        //tContent = content;
        oContext = context;
        this.uOrder = uOrder;
        //tContent = content;
    }
    //second change Method for filtering recycler view items
    public void filterList(ArrayList<TOrder> oOrder) {
        // below line is to add our filtered
        // list in our course array list.
        uOrder = oOrder;
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
        public TextView OrderStatus;
        public TextView ClientName;
        public TextView LogoName;
        public TextView PalleteNumber;
        public TextView AssignedDesigner;
        public TextView PackageCode;
        View v;

        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderStatus = itemView.findViewById(R.id.Status);
            ClientName = itemView.findViewById(R.id.fieldOne);
            LogoName = itemView.findViewById(R.id.fieldTwo);
            PalleteNumber = itemView.findViewById(R.id.fieldThree);
            PackageCode = itemView.findViewById(R.id.PackageCode);
            AssignedDesigner = itemView.findViewById(R.id.Designer);

            v=itemView;
        }
    }
    public ImageOrderAdapter(ArrayList<TOrder> uOrder) {
        this.uOrder = uOrder;
        newOrder = new ArrayList<>(uOrder);
    }

    @NonNull
    @Override
    public ImageOrderAdapter.imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(oContext).inflate(R.layout.to_be_assigned, parent, false);
        return new ImageOrderAdapter.imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageOrderAdapter.imageViewHolder holder, int position) {
        //TContent currentTedits = tContent.get(position);
        final TOrder temp = uOrder.get(position);
        holder.OrderStatus.setText(uOrder.get(position).getOrderStatus());
        holder.ClientName.setText(uOrder.get(position).getClientName());
        holder.LogoName.setText(uOrder.get(position).getLogoName());
        holder.PalleteNumber.setText(uOrder.get(position).getPalleteNumber());
        holder.PackageCode.setText(uOrder.get(position).getPackageCode());
        holder.AssignedDesigner.setText(uOrder.get(position).getAssignedDesigner());

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(oContext,EditOrder.class);
                intent.putExtra("ClientName", temp.getClientName());
                intent.putExtra("LogoName", temp.getLogoName());
                intent.putExtra("PalleteNumber", temp.getPalleteNumber());
                intent.putExtra("PackageCode", temp.getPackageCode());
                intent.putExtra("PackageDesigner", temp.getAssignedDesigner());
                intent.putExtra("OrderStatus", temp.getOrderStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                oContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uOrder.size();
    }

}
