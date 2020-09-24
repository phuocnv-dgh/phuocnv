package com.example.doanltdd;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private String DB_PATH = null;
    private static String DB_NAME = "eng_dictionary.db";
    private SQLiteDatabase myDatabase;
    private  final Context myContext;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, 1);
        this.myContext = context;
//        this.DB_PATH = "/data/data/" + context.getPackageName() + "/database/eng_dictionary.db";
        this.DB_PATH = context.getDatabasePath(DB_NAME+DB_PATH).toString();
        Log.e("Path 1", DB_PATH);
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(!dbExist){
            this.getReadableDatabase();
            try{
                copyDatabase();
            }catch (IOException e){
                throw new Error("Error copying database");
            }
        }
    }

    public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try {
//            File dbFile = myContext.getDatabasePath(DB_NAME);
//            return dbFile.exists();
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLException e)
        {
            //
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDatabase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutPut = new FileOutputStream(outFileName);
        byte[] buffer = new byte[64];
        int length;
        while ((length = myInput.read(buffer)) > 0){
            myOutPut.write(buffer, 0, length);
        }
        myOutPut.flush();
        myOutPut.close();
        myInput.close();
        Log.i("copyDatabase","Database copied");
    }
    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    @Override
    public synchronized void close(){
        if(myDatabase != null)
        {
            myDatabase.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        try{
            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAME);
            copyDatabase();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //lấy các định nghĩa, ví dụ, đồng nghĩa, trái nghĩa
    public Cursor getMeaning(String text)
    {
        String query = String.format("SELECT en_definition, example, synonyms, antonyms FROM words WHERE UPPER(en_word) = UPPER('%s')", text);
        Cursor c = myDatabase.rawQuery(query, null);
        return  c;
    }
    public Cursor getSuggestions(String text)
    {
        Cursor c = myDatabase.rawQuery("SELECT _id, en_word FROM words WHERE en_word LIKE '"+text+"%' LIMIT 40",null);
        return c;
    }
    public void insertHistory(String text)
    {
        myDatabase.execSQL("INSERT INTO history(word) VALUES(UPPER('"+text+"'))");
    }
    public int inserWords(String text, String text1)
    {
        if(checkWordExist(text)) {
            String query = String.format("INSERT INTO TuVung VALUES('" + text + "','" + text1 + "')");
            myDatabase.execSQL(query);
            return 1;
        }
        return 0;
    }
    public boolean checkWordExist(String text)
    {
        String query = "Select * from TuVung where TenTu='" + text + "'";
        Cursor c = myDatabase.rawQuery(query, null);
        boolean b = c.moveToFirst();
        return !b;
    }
    public Cursor getWords()
    {
        String query = "SELECT * FROM TuVung";
//                OLD = "SELECT DISTINCT word, en_definition FROM history h JOIN words w ON UPPER(h.word) = UPPER(w.en_word) ORDER BY h._id DESC";
        Cursor c = myDatabase.rawQuery(query,null);
        return c;
    }
    public void upDateWords(String ten, String nghia)
    {
        if(!checkWordExist(ten)) {
            String query = String.format("UPDATE TuVung SET TenTu = '" + ten + "', NghiaTu = '" + nghia + "' WHERE TenTu = '" + ten + "'");
            myDatabase.execSQL(query);
        }
    }
    public void deleteWords(String ten)
    {
        String query = String.format("DELETE FROM TuVung WHERE TenTu = '"+ten+"'");
        myDatabase.execSQL(query);
    }
    public Cursor getHistory()
    {
        String query = "SELECT * FROM HISTORY",
                OLD = "SELECT DISTINCT word, en_definition FROM history h JOIN words w ON UPPER(h.word) = UPPER(w.en_word) ORDER BY h._id DESC";
        Cursor c = myDatabase.rawQuery(OLD,null);
        return c;
    }
    public void deleteHistory(){
        myDatabase.execSQL("DELETE FROM history");
    }
}
