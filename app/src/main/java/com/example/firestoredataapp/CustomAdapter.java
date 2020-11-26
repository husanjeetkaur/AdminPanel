package com.example.firestoredataapp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    ListActivity listActivity;
    List<Model> modelList;
    public CustomAdapter(ListActivity listActivity, List<Model> modelList){
        this.listActivity = listActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String Price = "Price:"+modelList.get(position).getPrice();
                String Description = "Description:"+modelList.get(position).getDescription();
                String Category = "Category"+modelList.get(position).getCategory();
                String SubCategory = "SubCategory"+modelList.get(position).getSubCategory();
                String Id = "Id"+modelList.get(position).getId();
                String Image = modelList.get(position).getImage();
                Toast.makeText(listActivity,Price+"\n"+Description+"\n"+Category+"\n"+SubCategory+"\n"+Image+"\n"+Id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.showPrice.setText(" Price = "+ modelList.get(position).getPrice());
        holder.showDesc.setText(" Description = "+modelList.get(position).getDescription());
        holder.showCategory.setText(" Category = "+modelList.get(position).getCategory());
        holder.showSubcategory.setText(" SubCategory = "+modelList.get(position).getSubCategory());
        holder.Id.setText(" Id = "+modelList.get(position).getId());
        Picasso.get().load(modelList.get(position).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
