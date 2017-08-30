package com.hyc.music.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyc.music.R;
import com.hyc.music.bean.Song;
import com.hyc.music.utils.MusicUtils;

import java.util.List;

/**
 * Created by 1 on 2017/2/24.
 */

public class MusicAdapter extends BaseQuickAdapter<Song> {

    Context context;

    public MusicAdapter(List<Song> data, Context context) {
        super(R.layout.item_music, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Song song) {
        baseViewHolder.setText(R.id.tv_song_name, song.getSong());
        baseViewHolder.setText(R.id.tv_singer_name, song.getSinger());
        baseViewHolder.setImageResource(R.id.iv_song, R.mipmap.ic_launcher);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "播放音乐" + song.getPath());
                MusicUtils.getIntance().playMusic(song);
            }
        });
    }

}
