package com.example.doanltdd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Vector;

public class YoutobeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<YoutobeVideos> youtobeVideos = new Vector<YoutobeVideos>();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtobe);
        imageView = findViewById(R.id.backHistory);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyvlerview_Youtobe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/W7QYqI2im-Y\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/njDKi7dDOq4\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/CM4u7Idq-uI\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/GBZ6AYbTKlQ\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/8h7g38WufJU\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Og3XK_yU1EY\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Ci7T1i-B8XY\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/9buWDyEZ5f8\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ES-jZlcvFLc\" frameborder=\"0\" allowfullscreen></iframe>") );
        youtobeVideos.add( new YoutobeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/lVFffZ8okpg\" frameborder=\"0\" allowfullscreen></iframe>") );
        VideoAdapter videoAdapter = new VideoAdapter(youtobeVideos);

        recyclerView.setAdapter(videoAdapter);
    }
}