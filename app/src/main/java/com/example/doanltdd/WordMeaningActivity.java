package com.example.doanltdd;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanltdd.fragments.FragmentAntonyms;
import com.example.doanltdd.fragments.FragmentDefinition;
import com.example.doanltdd.fragments.FragmentExample;
import com.example.doanltdd.fragments.FragmentSynonyms;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaningActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ViewPager viewPager;
    String enWord;
    DatabaseHelper myDbHelper;
    Cursor c = null;

    public String enDefinition;
    public String exam;
    public String synonyms;
    public String antonyms;
    TextToSpeech tts;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            enWord = bundle.getString("en_word");
            myDbHelper = new DatabaseHelper(this);
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            c = myDbHelper.getMeaning(enWord);
            if (c.moveToFirst()) {
                enDefinition = c.getString(c.getColumnIndex("en_definition"));
                exam = c.getString(c.getColumnIndex("example"));
                synonyms = c.getString(c.getColumnIndex("synonyms"));
                antonyms = c.getString(c.getColumnIndex("antonyms"));
//            enDefinition = c.getString(2);
//            exam = c.getString(3);
//            synonyms = c.getString(4);
//            antonyms = c.getString(5);
            }
            //history
            myDbHelper.insertHistory(enWord);
            //speak
            ImageButton btnSpeak = findViewById(R.id.btnSpeak);
            btnSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tts = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                int result = tts.setLanguage(Locale.ENGLISH);
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("error", "Ngôn ngữ không được hổ trợ ");
                                } else {
                                    tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null);
                                }

                            } else
                                Log.e("error", "Initialization Failed!");

                        }
                    });
                }
            });


            toolbar = findViewById(R.id.mToolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(enWord);
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_white);

            viewPager = findViewById(R.id.tab_viewPaper);
            if (viewPager != null) {
                setupViewPager(viewPager);
            }

            //TapLayout
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }

            });
        }
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private  final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList =  new ArrayList<>();
        ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }
        void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount(){
            return  mFragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDefinition(), "Definition");
        adapter.addFrag(new FragmentSynonyms(), "Synonyms");
        adapter.addFrag(new FragmentAntonyms(), "Antonyms");
        adapter.addFrag(new FragmentExample(), "Example");
        viewPager.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
