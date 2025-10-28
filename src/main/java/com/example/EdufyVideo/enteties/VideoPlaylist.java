package com.example.EdufyVideo.enteties;


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

    @Column(name = "video_playlist_url", nullable = false)
    private String url;

    @Column(name = "video_playlist_description", nullable = false)
    private String description;

    @Column(name = "video_playlist_creation_date", nullable = false)
    private LocalDate creationDate;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PlaylistEntry> entryList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "video_playlist_creator",
            joinColumns = @JoinColumn(name = "video_playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "creator_id")
    )
    private List<Creator> creators = new ArrayList<>();

    @Column(name = "video_playlist_active")
    private boolean active;

    public VideoPlaylist() {

    }

    public VideoPlaylist(String title, String url, String description, LocalDate creationDate, List<PlaylistEntry> entryList, List<Creator> creators, boolean active) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.creationDate = creationDate;
        this.entryList = entryList;
        this.creators = creators;
        this.active = active;
    }

    public VideoPlaylist(VideoPlaylist videoPlaylist) {
        this.title = videoPlaylist.title;
        this.url = videoPlaylist.url;
        this.description = videoPlaylist.description;
        this.creationDate = videoPlaylist.creationDate;
        this.entryList = videoPlaylist.entryList;
        this.creators = videoPlaylist.creators;
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

    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
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
                ", creators=" + creators +
                ", active=" + active +
                '}';
    }
}
