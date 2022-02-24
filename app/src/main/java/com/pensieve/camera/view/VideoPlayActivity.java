package com.pensieve.camera.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.pensieve.camera.R;

import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayActivity extends AppCompatActivity {

    private String videoName="";
    private VideoView mVideoView;
    private ImageView pauseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        videoName = intent.getStringExtra("videoName");

        pauseBtn = findViewById(R.id.pauseBtn);

        String mp4Name = videoName.substring(0, videoName.length()-3) + "mp4";

        mVideoView = findViewById(R.id.screenVideoView);
        Uri uri = Uri.parse(mp4Name);
        mVideoView.setVideoURI(uri);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                testStart();

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                if(dpWidth/dpHeight > mp.getVideoWidth()/mp.getVideoHeight()){
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                    lp.leftToLeft = R.id.main_constraint_layout;
                    lp.rightToRight =R.id.main_constraint_layout;
                    lp.topToTop = R.id.main_constraint_layout;
                    lp.bottomToBottom = R.id.main_constraint_layout;
                    mVideoView.setLayoutParams(lp);
                }
                else{
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    lp.leftToLeft = R.id.main_constraint_layout;
                    lp.rightToRight =R.id.main_constraint_layout;
                    lp.topToTop = R.id.main_constraint_layout;
                    lp.bottomToBottom = R.id.main_constraint_layout;
                    mVideoView.setLayoutParams(lp);
                }

                mVideoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying())  {
                            pauseBtn.setVisibility(View.VISIBLE);
                            pauseBtn.setImageResource(R.drawable.pause_button);
                            mp.pause();
                        }
                        else {
                            mp.start();
                            pauseBtn.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        pauseBtn.setVisibility(View.VISIBLE);
                        pauseBtn.setImageResource(R.drawable.play_btn);
                        timer_sec = 0;
                    }

                });

            }
        });

//        final MediaController mediaController = new MediaController(this);
//        mVideoView.setMediaController(mediaController);

    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private TimerTask second;
    private TextView timer_text;
    private final Handler handler = new Handler();
    int timer_sec;
    public void testStart() {
        timer_sec = 0;
        timer_text = (TextView) findViewById(R.id.timer);
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }
    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
//                timer_text.setText(milliSecondsToTimer(timer_sec*1000));
                timer_text.setText(milliSecondsToTimer(mVideoView.getCurrentPosition()));
            }
        };

        if(mVideoView.getCurrentPosition() >= mVideoView.getDuration()) handler.removeCallbacks(updater);
        else handler.post(updater);
    }

}