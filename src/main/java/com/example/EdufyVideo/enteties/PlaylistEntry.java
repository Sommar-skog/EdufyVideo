package com.example.EdufyVideo.enteties;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist_entry",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"playlist_id", "video_clip_id"}),
                @UniqueConstraint(columnNames = {"playlist_id", "playlist_entry_position"})
        })
public class PlaylistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_entry_id")
    private Long id;

  /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_clip_id", nullable = false)
    private VideoClip videoClip;

    @Column(name = "playlist_entry_position", nullable = false)
    private Integer position;

    public PlaylistEntry() {}

    public PlaylistEntry(Playlist playlist, VideoClip videoClip, Integer position) {
        //this.playlist = playlist;
        this.videoClip = videoClip;
        this.position = position;
    }

    public PlaylistEntry(Long id, Playlist playlist, VideoClip videoClip, Integer position) {
        this.id = id;
        //this.playlist = playlist;
        this.videoClip = videoClip;
        this.position = position;
    }

    public PlaylistEntry(PlaylistEntry playlistEntry) {
        this.id = playlistEntry.id;
        //this.playlist = playlistEntry.playlist;
        this.videoClip = playlistEntry.videoClip;
        this.position = playlistEntry.position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

/*    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }*/

    public VideoClip getVideoClip() {
        return videoClip;
    }

    public void setVideoClip(VideoClip videoClip) {
        this.videoClip = videoClip;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PlaylistEntry{" +
                "id=" + id +
               // ", playlist=" + playlist +
                ", videoClip=" + videoClip +
                ", position=" + position +
                '}';
    }
}
