package com.example.supplycrate1;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalPhotosAdapter extends RecyclerView.Adapter<HorizontalPhotosAdapter.MyViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Bitmap> bitmapList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView riv;

        public MyViewHolder(View view) {
            super(view);

            riv = (ImageView) view.findViewById(R.id.horizontal_item_view_image);

        }
    }


    public HorizontalPhotosAdapter(Context context, ArrayList<Bitmap> bitmapList, String[] imageUrls) {
        this.context = context;
        this.bitmapList = bitmapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear, parent, false);

        if (itemView.getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT)
            itemView.getLayoutParams ().width = RecyclerView.LayoutParams.WRAP_CONTENT;

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.riv.setImageBitmap(bitmapList.get(position));

    }


    @Override
    public int getItemCount() {
        return bitmapList.size();
    }
}

