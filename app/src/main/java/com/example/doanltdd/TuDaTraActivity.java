package com.example.doanltdd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class TuDaTraActivity extends AppCompatActivity {
    ImageView imageView;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;
    ArrayList<History> historyList;
    RecyclerView recyclerView;
    RecyclerView.Adapter hisAdapter;
    Cursor cursorHistory;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout emptyHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_da_tra);
        imageView = findViewById(R.id.backHistory);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myDbHelper = new DatabaseHelper(this);
        if(myDbHelper.checkDataBase()){
            openDatabase();
        }
        else
        {
            LoadDatabaseAsync task = new LoadDatabaseAsync(TuDaTraActivity.this);
            task.execute();
        }
        emptyHistory = findViewById(R.id.empty_history);

        recyclerView = findViewById(R.id.recyclerview_history);
        layoutManager = new LinearLayoutManager(TuDaTraActivity.this);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void fetch_history() {
        historyList = new ArrayList<>();
        if (!databaseOpened)
            myDbHelper.openDataBase();
        cursorHistory = myDbHelper.getHistory();
        if (cursorHistory.moveToFirst()){
            History h;
            do{
                h = new History(cursorHistory.getString(0), cursorHistory.getString(1));
                historyList.add(h);
            }while (cursorHistory.moveToNext());
        }

        hisAdapter = new RecyclerViewHistory(this, historyList);
        recyclerView.setAdapter(hisAdapter);
    }

    protected static void openDatabase(){
        try {
            myDbHelper.openDataBase();
            databaseOpened=true;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        fetch_history();
    }

}