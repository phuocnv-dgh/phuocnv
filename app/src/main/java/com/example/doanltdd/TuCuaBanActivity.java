package com.example.doanltdd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.doanltdd.MainActivity.openDatabase;

public class TuCuaBanActivity extends AppCompatActivity {
    ImageView imageView;

    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;
    ArrayList<Words> wordList;
    ListView listWord;
    Adapter wordAdapter;
    Cursor cursorWord;
    RelativeLayout emptyWord;
    Button btnThem;
    Button btnHuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_cua_ban);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);
        imageView = findViewById(R.id.backWords);
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
            LoadDatabaseAsync task = new LoadDatabaseAsync(TuCuaBanActivity.this);
            task.execute();
        }
        emptyWord = findViewById(R.id.empty_word);
        listWord = findViewById(R.id.list_word);

        final EditText edtTen =  findViewById(R.id.tenTu);
        final EditText edtNgia = findViewById(R.id.nghiaTu);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTen.getText().clear();
                edtNgia.getText().clear();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTen.getText().toString();
                String nghia = edtNgia.getText().toString();
                if(ten.equals("") || nghia.equals(""))
                {
                    Toast.makeText(TuCuaBanActivity.this,"Vui lòng nhập tên từ muốn thêm!", Toast.LENGTH_SHORT).show();
                }else
                {
                    int kq = myDbHelper.inserWords(ten, nghia);
                    if(kq == 0)
                    {
                        DialogThemTuTrung(ten, nghia);
                    }
                    fetch_word();
                }
            }
        });
    }
    private void fetch_word() {
        wordList = new ArrayList<>();
        if (!databaseOpened)
            myDbHelper.openDataBase();
        cursorWord = myDbHelper.getWords();
        if (cursorWord.moveToFirst()){
            Words h;
            do{
                h = new Words(cursorWord.getString(0), cursorWord.getString(1));
                wordList.add(h);
            }while (cursorWord.moveToNext());
        }
        wordAdapter = new WordsAdapter(this,R.layout.word_item_layout, wordList);

        listWord.setAdapter((ListAdapter) wordAdapter);
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
        fetch_word();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_tuvung, menu);

        return super.onCreateOptionsMenu(menu);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
//    {
//        menuInflater.inflate(R.menu.add_tuvung, menu);
//        super.onCreateOptionsMenu(menu, menuInflater);
//    }
    public void DoalogSuaTuVung(final String ten, String nghia){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sua);
        final EditText editTen = dialog.findViewById(R.id.editTen_sua);
        final EditText editNghia = dialog.findViewById(R.id.editNgia_sua);
        Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy_sua);


        editTen.setText(ten);
        editNghia.setText(nghia);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMoi = editTen.getText().toString().trim();
                String nghiaMoi = editNghia.getText().toString().trim();
                myDbHelper.upDateWords(tenMoi,nghiaMoi);
                Toast.makeText(TuCuaBanActivity.this,"Đã sửa", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                fetch_word();

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void DialogXoaTuVung(final String ten){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa từ "+ ten+ " không??");
//        final EditText editTen = findViewById(R.id.editTen_sua);
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDbHelper.deleteWords(ten);
                Toast.makeText(TuCuaBanActivity.this,"Đã xóa " + ten , Toast.LENGTH_SHORT).show();
                fetch_word();
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }
    public void DialogThemTuTrung(final String ten, final String nghia){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Từ "+ ten+ " đã tồn tại, bạn có muốn sửa không??");
//        final EditText editTen = findViewById(R.id.editTen_sua);
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDbHelper.upDateWords(ten, nghia);
                Toast.makeText(TuCuaBanActivity.this,"Đã sửa " + ten , Toast.LENGTH_SHORT).show();
                fetch_word();
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }
}