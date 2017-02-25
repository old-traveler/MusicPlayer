package com.hyc.musicplayer.bean;

/**
 * Created by 1 on 2017/2/24.
 */

public class Song {
    /**
     * 歌手
     */
    private String singer;
    /**
     * 歌曲名
     */
    private String song;
    /**
     * 歌曲的地址
     */
    private String path;
    /**
     * 歌曲长度
     */
    private int duration;
    /**
     * 歌曲的大小
     */
    private long size;
    /**
     * 歌曲在集合中的position
     */
    private int position;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
