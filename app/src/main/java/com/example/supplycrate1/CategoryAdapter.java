package com.example.supplycrate1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter {

    List<String> CategoryList;
    Context _context;

    public CategoryAdapter(@NonNull Context context, List<String> categoryList) {
        super(context, R.layout.ctgryview,categoryList);

        this.CategoryList = categoryList;
        this._context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(_context).inflate(R.layout.ctgryview,parent,false);
        TextView  ctgrytitle = view.findViewById(R.id.categorynameview);

        ctgrytitle.setText(CategoryList.get(position));
        return view;
    }
}
