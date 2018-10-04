package com.example.miche.represent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewDetailed extends RecyclerView.Adapter<RecyclerViewDetailed.viewHolder> {
    private ArrayList<String> mImageNames;
    private ArrayList<String> mImages;
    private Context mContext;

    public RecyclerViewDetailed(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_detailedlist, parent, false);
        viewHolder viewholder = new viewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(viewHolder.image);
        viewHolder.imageName.setText(mImageNames.get(position));
        Typeface light = Typeface.createFromAsset(mContext.getAssets(), "fonts/Montserrat-Light.ttf");
        viewHolder.imageName.setTypeface(light);
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        RelativeLayout detailedLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.circleImage);
            imageName = itemView.findViewById(R.id.type);
            detailedLayout = itemView.findViewById(R.id.detailed_layout);
        }
    }
}
