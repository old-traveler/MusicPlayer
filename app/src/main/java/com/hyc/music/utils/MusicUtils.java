package com.hyc.music.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hyc.music.bean.Song;

/**
 * 音乐工具类,
 */
public class MusicUtils {
    /**
     * 播放模式
     */
    private int pattern;
    /**
     * 顺序播放
     */
    public static final int TYPE_ORDER = 4212;
    /**
     * 随机播放
     */
    public static final int TYPE_RANDOM = 4313;
    /**
     * 单曲循环
     */
    public static final int TYPE_SINGLE = 4414;

    private MediaPlayer mediaPlayer;

    private static MusicUtils musicUtils;

    private List<Song> songs;

    private List<Song> initialSongs;

    private int currentSongPosition;

    private OnPlayListener onPlayListener;

    private boolean isPrepare = false;

    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    public void setPattern(int pattern) {
        if (this.pattern == pattern && pattern == TYPE_ORDER) {
            return;
        }
        this.pattern = pattern;
        changeSongsByPattern();
    }

    public interface OnPlayListener {
        void OnPlaying(int duration);
    }

    public static synchronized MusicUtils getIntance() {
        if (musicUtils == null) {
            musicUtils = new MusicUtils();
        }
        return musicUtils;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        songs = getMusicData(context);
    }

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public List<Song> getMusicData(Context context) {
        List<Song> list = new ArrayList<Song>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        int position = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.getSong().contains("-")) {
                        String[] str = song.getSong().split("-");
                        song.setSinger(str[0]);
                        String name = str[1].toString().split(".mp3")[0].indexOf("[mqms2]") > 0
                                ? str[1].toString().split(".mp3")[0].substring(0, str[1].toString().split(".mp3")[0].indexOf("[mqms2]")) : str[1].toString().split(".mp3")[0];
                        song.setSong(name);

                    }

                    song.setPosition(position);
                    position++;
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        if (initialSongs == null) {
            initialSongs = list;
        }
        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    /**
     * 播放音乐
     *
     * @param song
     */
    public void playMusic(Song song) {
        if (isPrepare) {
            return;
        }
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        setPlayCompletionListener();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            isPrepare = true;
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isPrepare = false;
        onPlayListener.OnPlaying(song.getDuration());
        currentSongPosition = song.getPosition();
    }

    /**
     * 暂停和播放音乐
     */
    public void playOrPause() {
        if (mediaPlayer == null) {
            playMusic(songs.get(0));
            currentSongPosition = 0;
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    /**
     * 播放上一曲
     */
    public void playPrevious() {
        if (pattern == TYPE_SINGLE) {
            playMusic(songs.get(currentSongPosition));
            return;
        }
        Log.d("TAG", "currentPosition" + currentSongPosition);
        if (mediaPlayer == null) {
            playMusic(songs.get(0));
            currentSongPosition = 0;
            return;
        }
        if (currentSongPosition == 0) {
            currentSongPosition = songs.size() - 1;
            Log.d("TAG", "1111currentPosition" + currentSongPosition);
            playMusic(songs.get(currentSongPosition));
            return;
        }
        currentSongPosition--;
        playMusic(songs.get(currentSongPosition));

    }

    /**
     * 播放下一曲
     */
    public void playNext() {
        if (pattern == TYPE_SINGLE) {
            playMusic(songs.get(currentSongPosition));
            return;
        }
        Log.d("TAG", "currentPosition" + currentSongPosition);
        if (mediaPlayer == null) {
            playMusic(songs.get(0));
            currentSongPosition = 0;
            return;
        }
        if (currentSongPosition == songs.size() - 1) {
            currentSongPosition = 0;
            playMusic(songs.get(currentSongPosition));
            return;
        }
        currentSongPosition++;
        playMusic(songs.get(currentSongPosition));

    }

    public void setPlayCompletionListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playNext();
            }
        });

    }

    /**
     * 清理资源
     */
    public void clean() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**
     * 根据播放模式改变songs
     */
    public void changeSongsByPattern() {
        switch (pattern) {
            case TYPE_ORDER:
                songs = initialSongs;
                break;
            case TYPE_RANDOM:
                Collections.shuffle(songs);
                break;
        }
        for (Song s : songs) {
            Log.d("TAG", "song.position is " + s.getPosition());
        }
    }

}
