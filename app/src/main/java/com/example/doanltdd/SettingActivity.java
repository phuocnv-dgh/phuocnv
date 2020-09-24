package com.example.doanltdd;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        //xoa lich su
        TextView clearHis = findViewById(R.id.clear_history);
        clearHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB = new DatabaseHelper(SettingActivity.this);
                try{
                    myDB.openDataBase();
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
                showAlerDialog();
            }
        });

    }

    private void showAlerDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Bạn chắc chắn muốn xóa?");
        builder.setMessage("Tất cả lịch sử sẽ bị xóa sạch!!");
        String posTest = "Yes";
        builder.setPositiveButton(posTest, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteHistory();
            }
        });
        String negTest = "No";
        builder.setNegativeButton(negTest, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
