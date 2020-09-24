package com.example.doanltdd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.doanltdd.R;
import com.example.doanltdd.WordMeaningActivity;

public class FragmentAntonyms extends Fragment {
    public FragmentAntonyms(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_definition, container, false);
        Context context = getActivity();
        TextView text = view.findViewById(R.id.textViewD);
        String antony =  ((WordMeaningActivity)context).antonyms;
        if(antony != null){
            antony = antony.replaceAll(",", ",\n");
            text.setText(antony);
        }
        if(antony == null) {
            text.setText("No Antonyms found");
        }
        return view;
    }
}
