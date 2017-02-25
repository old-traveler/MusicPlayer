package com.hyc.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyc.musicplayer.utils.MusicUtils;

/**
 * Created by 1 on 2017/2/25.
 */

public class MusicService extends Service {

    /**
     * 播放音乐
     */
    public static final String PLAYMUSIC="4kf";

    /**
     * 暂停或者是播放音乐
     */
    public static final String PLAYORPAUSE="2k5o";

    /**
     * 上一首音乐
     */
    public static final String PREVIOUSMUSIC="4si3";

    /**
     * 下一首音乐
     */
    public static final String NEXTMUSIC="2hd3";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","服务开启");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG","style is "+intent.getStringExtra("style"));
        switch (intent.getStringExtra("style")){
            case PLAYORPAUSE:
                MusicUtils.getIntance().playOrPause();
                break;
            case PREVIOUSMUSIC:
                MusicUtils.getIntance().playPrevious();
                break;
            case NEXTMUSIC:
                MusicUtils.getIntance().playNext();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicUtils.getIntance().clean();
    }
}
