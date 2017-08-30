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
 * 这个适配器，我是继承了一个开源库 不和之前的一样了 不过你需要了解的就这两个方法 Created by 1 on 2017/2/24.
 */

public class MusicAdapter extends BaseQuickAdapter<Song> {

    Context context;

    /**
     * 构造方法，初始化数据集合
     * @param data
     * @param context
     */
    public MusicAdapter(List<Song> data, Context context) {
        super(R.layout.item_music, data);
        this.context = context;
    }

    /**
     * 加载当前显示子项的数据，简而言之就是你现在看见的这个子项，把数据加载到对应的控件中
     * @param baseViewHolder
     * @param song
     */
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Song song) {
        baseViewHolder.setText(R.id.tv_song_name, song.getSong());
        baseViewHolder.setText(R.id.tv_singer_name, song.getSinger());
        baseViewHolder.setImageResource(R.id.iv_song, R.mipmap.ic_launcher);
        //设置点击事件，点击就播放当前子项所对应的歌曲
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "播放音乐" + song.getPath());
                MusicUtils.getIntance().playMusic(song);
            }
        });
    }

}
