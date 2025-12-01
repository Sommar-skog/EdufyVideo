package com.example.EdufyVideo.models.enteties;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//ED-123-AA
@Entity
@Table(name = "video_playlist")
public class VideoPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_playlist_id")
    private Long id;

    @Column(name = "video_playlist_title", nullable = false, length = 100)
    private String title;

    @Column(name = "video_playlist_url", nullable = false, unique = true)
    private String url;

    @Column(name = "video_playlist_description", nullable = false)
    private String description;

    @Column(name = "video_playlist_creation_date", nullable = false)
    private LocalDate creationDate;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PlaylistEntry> entryList = new ArrayList<>();

    @Column(name = "video_playlist_active")
    private boolean active = true;

    public VideoPlaylist() {

    }

    public VideoPlaylist(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.creationDate = LocalDate.now();
    }

    public VideoPlaylist(String title, String url, String description, LocalDate creationDate) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.creationDate = creationDate;
    }

    public VideoPlaylist(VideoPlaylist videoPlaylist) {
        this.id = videoPlaylist.id;
        this.title = videoPlaylist.title;
        this.url = videoPlaylist.url;
        this.description = videoPlaylist.description;
        this.creationDate = videoPlaylist.creationDate;
        this.entryList = videoPlaylist.entryList;
        this.active = videoPlaylist.active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<PlaylistEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<PlaylistEntry> entryList) {
        this.entryList = entryList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VideoPlaylist{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", entryList=" + entryList +
                ", active=" + active +
                '}';
    }
}
