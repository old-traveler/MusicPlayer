package com.hyc.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.hyc.musicplayer.adapter.MusicAdapter;
import com.hyc.musicplayer.service.MusicService;
import com.hyc.musicplayer.utils.MusicUtils;
import com.hyc.musicplayer.view.DividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_play)
    Button btn_play;
    @Bind(R.id.btn_previous)
    Button btn_previous;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.pb_music)
    ProgressBar pb_music;
    @Bind(R.id.rl_player)
    RelativeLayout rl_player;
    @Bind(R.id.rv_main)
    RecyclerView rv_main;
    private Thread main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        MusicUtils.getIntance().setOnPlayListener(new MusicUtils.OnPlayListener() {
            @Override
            public void OnPlaying(int duration) {
                beginProgress(duration);
            }
        });
    }

    private void initView() {
        rv_main.setItemAnimator(new DefaultItemAnimator());
        rv_main.setLayoutManager(new LinearLayoutManager(this));
        rv_main.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_main.setAdapter(new MusicAdapter(MusicUtils.getIntance().getMusicData(this),MainActivity.this));
    }

    @OnClick({R.id.btn_play, R.id.btn_previous, R.id.btn_next})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_play:
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                intent.putExtra("style",MusicService.PLAYORPAUSE);
                startService(intent);
                if(btn_play.getText().toString().equals("播放")){
                    btn_play.setText("暂停");
                }else {
                    btn_play.setText("播放");
                }

                break;
            case R.id.btn_previous:
                Intent intent1 = new Intent(MainActivity.this, MusicService.class);
                intent1.putExtra("style",MusicService.PREVIOUSMUSIC);
                startService(intent1);
                break;
            case R.id.btn_next:
                Intent intent2 = new Intent(MainActivity.this, MusicService.class);
                intent2.putExtra("style",MusicService.PREVIOUSMUSIC);
                startService(intent2);

                break;

        }
    }

    public void toast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    public void beginProgress(final int progress) {
        Log.d("TAG","progress is "+progress);
        if (main!=null){
            if (main.isAlive()){
                main.interrupt();
            }
        }
        main = new Thread(new Runnable() {
            @Override
            public void run() {
                float pro=0;
                try {
                    while(pro<100){
                        Thread.sleep(1000);
                        pro += 100.00f/(progress/1000.00f);
                        final float finalPro = pro;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb_music.setProgress((int) finalPro);
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        main.start();
    }


}
