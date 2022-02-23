package com.example.t_edits_app_tobias;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView viewOne;
    TextView viewTwo;
    TextView viewThree;
    TextView viewFour;

    //CardView cardView;
    View view;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_single_view);
        viewOne = itemView.findViewById(R.id.contentOne);
        viewTwo = itemView.findViewById(R.id.categoryTwo);
        viewThree = itemView.findViewById(R.id.categoryThree);
        viewFour = itemView.findViewById(R.id.contentFour);

        //cardView=cardView.findViewById(R.id.menupopbutton);

        view = itemView;
    }
}
