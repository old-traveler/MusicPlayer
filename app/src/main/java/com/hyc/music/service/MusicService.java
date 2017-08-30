package com.hyc.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyc.music.utils.MusicUtils;

/**
 * Created by 1 on 2017/2/25.
 */

public class MusicService extends Service {

    /**
     * 播放音乐
     */
    public static final String PLAYMUSIC = "4kf";

    /**
     * 暂停或者是播放音乐
     */
    public static final String PLAYORPAUSE = "2k5o";

    /**
     * 上一首音乐
     */
    public static final String PREVIOUSMUSIC = "4si3";

    /**
     * 下一首音乐
     */
    public static final String NEXTMUSIC = "2hd3";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "服务开启");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();//获取设备电源锁
        switch (intent.getStringExtra("style")) {
            case PLAYORPAUSE:
                MusicUtils.getIntance().playOrPause();
                break;
            case PREVIOUSMUSIC:
                MusicUtils.getIntance().playPrevious();
                break;
            case NEXTMUSIC:
                Log.d("TAG", "播放下一首歌曲");
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
        releaseWakeLock();
    }


    private PowerManager.WakeLock mWakeLock = null;

    private void acquireWakeLock() {
        Log.d("TAG", "申请电源锁成功!");
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this
                    .getClass().getCanonicalName());
            if (null != mWakeLock) {
                mWakeLock.acquire();
                if (mWakeLock.isHeld()) {
                    Log.d("TAG", "申请电源锁成功!");
                } else {
                    Log.d("TAG", "申请电源锁失败!");
                }
            }
        }
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
