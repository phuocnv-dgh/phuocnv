package com.example.doanltdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WordsAdapter extends BaseAdapter {
    private TuCuaBanActivity context;
    private int layout;
    private List<Words> wordsList;

    public WordsAdapter(TuCuaBanActivity context, int layout, List<Words> wordsList) {
        this.context = context;
        this.layout = layout;
        this.wordsList = wordsList;
    }



    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtTen, txtNghia;
        ImageView imgDelete, imgEdit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTen = (TextView) convertView.findViewById(R.id.tenTu);
            holder.txtNghia = convertView.findViewById(R.id.nghiaTu);
            holder.imgDelete = convertView.findViewById(R.id.imageViewDelete);
            holder.imgEdit = convertView.findViewById(R.id.imageViewEdit);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final Words words = wordsList.get(position);
        holder.txtTen.setText(words.getTenTu());
        holder.txtNghia.setText(words.getNghiaTu());
        //bat su kien sua va xoa
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DoalogSuaTuVung(words.getTenTu(), words.getNghiaTu());
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogXoaTuVung(words.getTenTu());
            }
        });
        return convertView;
    }
}
