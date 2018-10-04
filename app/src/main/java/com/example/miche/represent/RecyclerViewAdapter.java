package com.example.miche.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder>{

    private ArrayList<String> Type;
    private ArrayList<String> mImages;
    private ArrayList<String> Websites;
    private ArrayList<String> Emails;
    private ArrayList<String> Names;
    private ArrayList<String> Part;
    private ArrayList<String> Ids;

    private Context mContext;

    public static final String ID = "this is id";
    public static final String TYPE = "rep or sen";
    public static final String NAME = "name";
    public static final String PARTY = "party";
    public static final String IMAGEURL = "url";

    public RecyclerViewAdapter(ArrayList<String> Types, ArrayList<String> mImages,
                               ArrayList<String> Name, ArrayList<String> Web, ArrayList<String> Em,
                               ArrayList<String> Party, ArrayList<String> Id, Context mContext) {
        this.Type = Types;
        this.mImages = mImages;
        this.Websites = Web;
        this.Names = Name;
        this.Emails = Em;
        this.Part = Party;
        this.mContext = mContext;
        this.Ids = Id;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        Typeface reg = Typeface.createFromAsset(mContext.getAssets(), "fonts/Montserrat-Regular.ttf");

        holder.repType.setTypeface(reg);
        if (Type.get(position).equals("representative")) {
            holder.repType.setText("Representative");
        } else {
            holder.repType.setText("Senator");
        }

        if (Part.get(position).equals("Democrat")) {
            holder.repType.setTextColor(Color.BLUE);
        } else {
            holder.repType.setTextColor(Color.RED);
        }

        holder.web.setTypeface(reg);
        holder.web.setText(Html.fromHtml(Websites.get(position)));
        holder.web.setMovementMethod(LinkMovementMethod.getInstance());

        holder.names.setTypeface(reg);
        holder.names.setText(Names.get(position));

        holder.contact.setTypeface(reg);
        if (Emails.get(position).equals("<a href=\"null\">Email</a>")) {
            holder.contact.setText("No Email");
        } else {
            holder.contact.setText(Html.fromHtml(Emails.get(position)));
            holder.contact.setMovementMethod(LinkMovementMethod.getInstance());
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent det = new Intent(mContext, DetailedView.class);
                det.putExtra(ID, Ids.get(position));
                if (Type.get(position).equals("representative")) {
                    det.putExtra(TYPE, "Representative");
                } else {
                    det.putExtra(TYPE, "Senator");
                }
                det.putExtra(NAME, Names.get(position));
                det.putExtra(PARTY, Part.get(position));
                det.putExtra(IMAGEURL, mImages.get(position));
                mContext.startActivity(det);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Type.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView repType;
        TextView web;
        TextView contact;
        TextView names;
        RelativeLayout parentLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            repType = itemView.findViewById(R.id.type);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            web = itemView.findViewById(R.id.website);
            contact = itemView.findViewById(R.id.email);
            names = itemView.findViewById(R.id.repName);
        }
    }
}
