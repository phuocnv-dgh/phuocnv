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

public class FragmentExample extends Fragment {
    public FragmentExample(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_definition, container, false);

        Context context = getActivity();
        TextView text = view.findViewById(R.id.textViewD);
        String example =  ((WordMeaningActivity)context).exam;
        text.setText(example);
        if(example==null)
        {
            text.setText("No Example found");
        }
        return view;
    }
}
