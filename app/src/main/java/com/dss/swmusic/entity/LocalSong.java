package com.dss.swmusic.entity;

/**
 * 一首曲子
 */
public class LocalSong {
    /**歌曲名*/
    private String name;
    /**路径*/
    private String path;
    /**所属专辑*/
    private String album;
    /**艺术家(作者)*/
    private String artist;
    /**文件大小*/
    private long size;
    /**时长*/
    private int duration;
    /**歌曲名的拼音，用于字母排序*/
    private String pinyin;

    /**
     *
     * @param name 歌曲名
     * @param path 路径
     * @param album 专辑
     * @param artist 艺术家
     * @param size 文件大小
     * @param duration 时长
     */
    public LocalSong(String name, String path, String album, String artist, long size, int duration) {
        this.name = name;
        this.path = path;
        this.album = album;
        this.artist = artist;
        this.size = size;
        this.duration = duration;
        //TODO
        this.pinyin = "";
    }

    public LocalSong() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
