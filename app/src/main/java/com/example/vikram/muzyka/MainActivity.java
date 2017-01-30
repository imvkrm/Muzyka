package com.example.vikram.muzyka;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<File> mysongs;
    private ListView listView;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        listView=(ListView)findViewById(R.id.list);



         mysongs= findsongs(Environment.getExternalStorageDirectory());
        items =new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        listView.setAdapter(adp);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent plyintent=new Intent(getApplicationContext(),Player.class);
                plyintent.putExtra("pos",position);
                plyintent.putExtra("songlist",mysongs);
                startActivity(plyintent);
            }
        });


    }

    private ArrayList<File> findsongs(File root) {
        ArrayList<File> al=new ArrayList<>();
        File[] files=root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory()&& !singleFile.isHidden()){
                al.addAll(findsongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }

        return al;
    }
}
