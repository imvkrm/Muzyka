package com.example.vikram.muzyka;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    private static MediaPlayer mediaPlayer;
    private  int currentposition=0;
    private Uri uri;
    private int position;
    private ArrayList<File> mySongs;
    private Button prvbtn,bckbtn,frdbtn,nxtbtn;
    private FloatingActionButton plybtn;
    private SeekBar seekBar;
    Thread updateseekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
         mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(String.valueOf(Environment.getExternalStorageDirectory()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        prvbtn=(Button)findViewById(R.id.prebtn);
        bckbtn=(Button)findViewById(R.id.bckbtn);
        frdbtn=(Button)findViewById(R.id.frdbtn);
        nxtbtn=(Button)findViewById(R.id.nxtbtn);
        plybtn=(FloatingActionButton)findViewById(R.id.plybtn);
        seekBar=(SeekBar)findViewById(R.id.seekBar);

        prvbtn.setOnClickListener(this);
        bckbtn.setOnClickListener(this);
        plybtn.setOnClickListener(this);
        frdbtn.setOnClickListener(this);
        nxtbtn.setOnClickListener(this);

        updateseekbar=new Thread(){
            @Override
            public void run() {
                int totalDuration=mediaPlayer.getDuration();


                while (currentposition<totalDuration){
                    try {
                        sleep(500);
                        currentposition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };



        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mySongs=(ArrayList)bundle.getParcelableArrayList("songlist");
        position=bundle.getInt("pos",0);

        uri=Uri.parse(mySongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            }
        });



    }

    @Override
    public void onClick(View v) {
        int U_id=v.getId();

        switch (U_id){
            case R.id.prebtn:
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                uri=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
            case R.id.bckbtn:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;
            case R.id.plybtn:
                if(mediaPlayer.isPlaying())
                {
                    plybtn.setImageDrawable(getResources().getDrawable(R.drawable.play_24));
                    mediaPlayer.pause();
                }
                else {
                    plybtn.setImageDrawable(getResources().getDrawable(R.drawable.pause_24));

                    mediaPlayer.start();
                }
                break;
            case R.id.frdbtn:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;
            case R.id.nxtbtn:
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.release();
                position=(position+1)%mySongs.size();
                uri=Uri.parse(mySongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
        }
    }
}
