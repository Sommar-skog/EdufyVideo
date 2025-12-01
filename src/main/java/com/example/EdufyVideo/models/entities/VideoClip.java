package com.example.EdufyVideo.models.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//ED-122-AA
@Entity
@Table(name = "video_clip")
public class VideoClip {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "video_clip_id")
    private Long id;

    @Column(name = "video_clip_title", nullable = false, length = 100)
    private String title;

    @Column(name = "video_clip_url", nullable = false, unique = true)
    private String url;

    @Column(name = "video_clip_description", nullable = false)
    private String description;

    @Column(name = "video_clip_length", nullable = false)
    private LocalTime length;

    @Column(name = "video_clip_release_date", nullable = false)
    private LocalDate releaseDate;

    //ED-282-AA
    @ElementCollection
    @CollectionTable(name = "video_clip_user_history", joinColumns = @JoinColumn(name = "video_clip_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "times_played")
    private Map<Long, Long> userHistory = new HashMap<>();

    @OneToMany(mappedBy = "videoClip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistEntry> playlistEntries = new ArrayList<>();

    @Column(name = "video_clip_active")
    private Boolean active = true;

    public VideoClip() {}

    public VideoClip(Long id, String title, String url, String description, LocalTime length, LocalDate releaseDate,Map<Long,Long> userHistory, List<PlaylistEntry> entries, Boolean active) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.length = length;
        this.releaseDate = releaseDate;
        this.userHistory = userHistory;
        this.playlistEntries = entries;
        this.active = active;
    }

    public VideoClip(String title, String url, String description, LocalTime length) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.length = length;
        this.releaseDate = LocalDate.now();
        this.userHistory = new HashMap<>();
        this.active = true;
    }

    public VideoClip (VideoClip videoClip) {
        this.id = videoClip.id;
        this.title = videoClip.title;
        this.url = videoClip.url;
        this.description = videoClip.description;
        this.length = videoClip.length;
        this.releaseDate = videoClip.releaseDate;
        this.userHistory = videoClip.userHistory;
        this.playlistEntries = videoClip.playlistEntries;
        this.active = videoClip.active;
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

    public LocalTime getLength() {
        return length;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    //ED-282-AA
    // Tracks total play count per user for this video (userId → timesPlayed)
    public Long getTimesPlayed() {
        return userHistory.values().stream().mapToLong(Long::longValue).sum();
    }

    public Map<Long, Long> getUserHistory() {
        return userHistory;
    }

    public void setUserHistory(Map<Long, Long> userHistory) {
        this.userHistory = userHistory;
    }

    public List<PlaylistEntry> getPlaylistEntries() {
        return playlistEntries;
    }

    public void setPlaylistEntries(List<PlaylistEntry> playlistEntries) {
        this.playlistEntries = playlistEntries;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VideoClip{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", length=" + length +
                ", releaseDate=" + releaseDate +
                ", timesPlayed=" + getTimesPlayed() +
                ", playlistEntries=" + playlistEntries +
                ", active=" + active +
                '}';
    }
}
