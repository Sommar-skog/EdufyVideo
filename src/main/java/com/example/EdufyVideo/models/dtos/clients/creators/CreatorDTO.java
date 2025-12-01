package com.example.EdufyVideo.models.dtos.clients.creators;

import java.util.List;

//ED-124-AA (updated by ED-61-AA)
public class CreatorDTO {

    private long id;
    private String sub;
    private String username;
    private List<Long> videoClips;
    private List<Long> videoPlaylists;
    private List<Long> songs;
    private List<Long> albums;
    private List<Long> podcastEpisodes;
    private List<Long> podcastSeasons;

    public CreatorDTO() {}

    public CreatorDTO(long id, String sub, String username, List<Long> videoClips, List<Long> videoPlaylists, List<Long> songs, List<Long> albums, List<Long> podcastEpisodes, List<Long> podcastSeasons) {
        this.id = id;
        this.sub = sub;
        this.username = username;
        this.videoClips = videoClips;
        this.videoPlaylists = videoPlaylists;
        this.songs = songs;
        this.albums = albums;
        this.podcastEpisodes = podcastEpisodes;
        this.podcastSeasons = podcastSeasons;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Long> getVideoClips() {
        return videoClips;
    }

    public void setVideoClips(List<Long> videoClips) {
        this.videoClips = videoClips;
    }

    public List<Long> getVideoPlaylists() {
        return videoPlaylists;
    }

    public void setVideoPlaylists(List<Long> videoPlaylists) {
        this.videoPlaylists = videoPlaylists;
    }

    public List<Long> getSongs() {
        return songs;
    }

    public void setSongs(List<Long> songs) {
        this.songs = songs;
    }

    public List<Long> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Long> albums) {
        this.albums = albums;
    }

    public List<Long> getPodcastEpisodes() {
        return podcastEpisodes;
    }

    public void setPodcastEpisodes(List<Long> podcastEpisodes) {
        this.podcastEpisodes = podcastEpisodes;
    }

    public List<Long> getPodcastSeasons() {
        return podcastSeasons;
    }

    public void setPodcastSeasons(List<Long> podcastSeasons) {
        this.podcastSeasons = podcastSeasons;
    }

    @Override
    public String toString() {
        return "CreatorDTO{" +
                "id=" + id +
                ", sub='" + sub + '\'' +
                ", username='" + username + '\'' +
                ", videoClips=" + videoClips +
                ", videoPlaylists=" + videoPlaylists +
                ", songs=" + songs +
                ", albums=" + albums +
                ", podcastEpisodes=" + podcastEpisodes +
                ", podcastSeasons=" + podcastSeasons +
                '}';
    }

    /*    private Long id;
    private String username;

    public CreatorDTO() {}

    public CreatorDTO(Long id, String username, List<VideoClip> videoClips, List<VideoPlaylist> videoPlaylists) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CreatorDTO{" +
                "id=" + id +
                ", username='" + username +
                '}';
    }*/
}
