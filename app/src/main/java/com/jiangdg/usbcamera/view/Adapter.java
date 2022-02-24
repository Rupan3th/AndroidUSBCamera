package com.jiangdg.usbcamera.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiangdg.usbcamera.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<CourseModel> courseModels;
    Context context;
    private ItemClickListener clickListener;

    // Constructor for initialization
    public Adapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModels = courseModelArrayList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.images.setImageBitmap(courseModels.get(position).getImgid());

        if(courseModels.get(position).getFileType().equals("video"))
            holder.playBtn.setVisibility(View.VISIBLE);
        if(courseModels.get(position).getFileType().equals("image"))
            holder.playBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        // Returns number of items currently available in Adapter
        return courseModels.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        ImageView images;
        ImageView playBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.imageView);
            playBtn = (ImageView) itemView.findViewById(R.id.playBtn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getPosition()); // call the onClick in the OnItemClickListener
        }



    }


}
