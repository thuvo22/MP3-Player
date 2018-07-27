package com.example.thuvo.mediaappmusic;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView discPic;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    ArrayList<Song> arraySong;
    int pos = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapped();
        addSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);
        reStart();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos--;
                if(pos < 0){
                    pos = arraySong.size()-1;
                }

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                reStart();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                if(pos > arraySong.size()-1){
                    pos = 0;
                }

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                reStart();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play);
                reStart();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }else{
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                updateTimeSong();
                discPic.startAnimation(animation);
            }
        });
    skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(skSong.getProgress());
        }
    });
    }



    private void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());

                // check the song time --> if finish --> start next song
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        pos++;
                        if(pos > arraySong.size()-1){
                            pos = 0;
                        }

                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        reStart();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                        setTimeTotal();
                        updateTimeSong();
                    }
                });


                handler.postDelayed(this, 500);

            }
        },100);
    }

    private void setTimeTotal(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(timeFormat.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void reStart(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(pos).getFile());
        txtTitle.setText(arraySong.get(pos).getTitle());
    }

    private void addSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("A Million Dreams", R.raw.a));
        arraySong.add(new Song("The Other Side", R.raw.t));
        arraySong.add(new Song("The Greatest Show", R.raw.g));

    }

    public void mapped(){
        txtTimeSong     = (TextView) findViewById(R.id.textViewTimeSong);
        txtTimeTotal    = (TextView) findViewById(R.id.textViewTimeTotal);
        txtTitle        = (TextView) findViewById(R.id.textViewTitle);
        skSong          = (SeekBar) findViewById(R.id.seekBarSong);
        btnNext         = (ImageButton) findViewById(R.id.imageButtonNext);
        btnPlay         = (ImageButton) findViewById(R.id.imageButtonPlay);
        btnPrev         = (ImageButton) findViewById(R.id.imageButtonPre);
        btnStop         = (ImageButton) findViewById(R.id.imageButtonStop);
        discPic         = (ImageView)   findViewById(R.id.imageViewDisc);
    }
}
