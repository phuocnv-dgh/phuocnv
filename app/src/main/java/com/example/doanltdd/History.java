package com.example.doanltdd;

public class History {
    private String en_word;
    private String en_def;
    public History(String en_word, String en_def)
    {
        this.en_word = en_word;
        this.en_def = en_def;
    }
    public String get_En_word(){
        return en_word;
    }
    public String get_En_def()
    {
        return en_def;
    }
}
