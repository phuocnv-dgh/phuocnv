package com.example.doanltdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewHistory extends RecyclerView.Adapter<RecyclerViewHistory.HistoryViewHolder> {
    private ArrayList<History> histories;
    private Context context;

    public RecyclerViewHistory(Context context, ArrayList<History> histories){
        this.histories = histories;
        this.context = context;
    }
    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView enWord;
        TextView enDef;
        public HistoryViewHolder(View view){
            super(view);
            enWord = view.findViewById(R.id.en_word);
            enDef = view.findViewById(R.id.en_def);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String text = histories.get(position).get_En_word();
                    Intent intent = new Intent(context, WordMeaningActivity.class);
                    intent.putExtra("en_word", text);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new HistoryViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.enWord.setText(histories.get(position).get_En_word());
        holder.enDef.setText(histories.get(position).get_En_def());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }


}
