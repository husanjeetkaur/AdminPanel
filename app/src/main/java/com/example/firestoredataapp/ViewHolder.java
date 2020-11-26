package com.example.firestoredataapp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView showPrice,showDesc,showCategory,showSubcategory,Id;
    ImageView imageView;
    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        view =itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
                return true;
            }
        });

        showPrice = itemView.findViewById(R.id.showPrice);
        showDesc = itemView.findViewById(R.id.showDesc);
        showCategory = itemView.findViewById(R.id.showCategory);
        showSubcategory = itemView.findViewById(R.id.showSubcategory);
        imageView = itemView.findViewById(R.id.showImage);
        Id = itemView.findViewById(R.id.id);



    }

    private  ViewHolder.ClickListener mClickListener;


    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener =clickListener;
    }
}
