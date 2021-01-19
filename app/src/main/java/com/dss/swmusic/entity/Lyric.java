package com.dss.swmusic.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Lyric extends LitePalSupport {

    /**
     * 歌曲的id
     */
    @Column(unique = true)
    private long id;

    /**
     * 歌词
     */
    private String lyric;

    public Lyric(){}

    public Lyric(long id, String lyric) {
        this.id = id;
        this.lyric = lyric;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
