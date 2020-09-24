package com.example.doanltdd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaCodec;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    SearchView search;
    ImageButton mVoice;
    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;
    SimpleCursorAdapter suggestionAdapter;
    ListView listView;

    String[] items = {"Dịch văn bản","Từ của bạn",
            "Từ đã tra", "Kênh học Tiếng Anh"};

    int[] icons = {R.drawable.icon_sach,R.drawable.icon_star,
            R.drawable.icon_time, R.drawable.icon_youtobe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVoice = findViewById(R.id.voice_speak);
        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = findViewById(R.id.search_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconifiedByDefault(false);
                search.requestFocus();
                showKeyboard();
            }
        });
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Oke", Toast.LENGTH_SHORT).show();
//                search.setIconified(false);
//                Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
//                startActivity(intent);
            }
        });

        myDbHelper = new DatabaseHelper(this);
        if(myDbHelper.checkDataBase()){
            openDatabase();
        }
        else
        {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }
        final String[] from = new String[]{"en_word"};
        final int[] to = new int[]{R.id.suggestion_text};
        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.suggestion_row, null,from,to,0){
            @Override
            public void changeCursor(Cursor cursor)
            {
                super.swapCursor(cursor);
            }
        };
        search.setSuggestionsAdapter(suggestionAdapter);
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word,false);
                search.clearFocus();
                search.setFocusable(false);
                Intent intent = new Intent(MainActivity.this,WordMeaningActivity.class);
                intent.putExtra("en_word", clicked_word);
                startActivity(intent);
                return true;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = search.getQuery().toString();
                // ko cho nguoi dung nhap ky tu dat biet
                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(text);

                if(m.matches())
                {
                    Cursor c = myDbHelper.getMeaning(text);

                    if(c.getCount() == 0)
                    {
                        showAlerDiaglog();
                    }
                    else {
                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                 else{
                    showAlerDiaglog();
                }
                 return  false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                search.setIconifiedByDefault(false);
                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(s);
                if(m.matches()){
                    Cursor cursorSuggestion=myDbHelper.getSuggestions(s);
                    suggestionAdapter.changeCursor(cursorSuggestion);
                }

                return false;

            }
        });

        //fetch_history();
        listView = findViewById(R.id.listMain);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đi nào :))");

        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null!=data){
                    //lay
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //gan
                    search.setQuery(result.get(0),false);
                }
                break;
            }
        }
    }

    private void showAlerDiaglog()
    {
        search.setQuery("",false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("word not Found");
        builder.setMessage("Please search again");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                search.clearFocus();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_exit){
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.layout_listdm,null);
            ImageView iv = view1.findViewById(R.id.ivHinh);
            TextView tv = view1.findViewById(R.id.tvText);
            LinearLayout ln = view1.findViewById(R.id.linearButton);
            tv.setText(items[i]);
            iv.setImageResource(icons[i]);
            final int stt = i;
            ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (stt){
                        case 0:
                        {
                            Intent intent1 = new Intent(MainActivity.this, DichVanBanActivity.class);
                            startActivity(intent1);
                        break;
                        }
                        case 1:
                        {
                            Intent intent = new Intent(MainActivity.this, TuCuaBanActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case 2:
                        {
                            Intent intent = new Intent(MainActivity.this, TuDaTraActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case 3:
                        {
                            Intent intent2 = new Intent(MainActivity.this, YoutobeActivity.class);
                            startActivity(intent2);
                            break;
                        }
                        default:
                            break;
                    }
                }
            });
            return view1;
        }

    }
    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}