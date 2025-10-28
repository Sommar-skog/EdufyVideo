package com.example.EdufyVideo.enteties;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "video_clip")
public class VideoClip {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "video_clip_id")
    private Long id;

    @Column(name = "video_clip_title", nullable = false, length = 100)
    private String title;

    @Column(name = "video_clip_url", nullable = false)
    private String url;

    @Column(name = "video_clip_description", nullable = false)
    private String description;

    @Column(name = "video_clip_length", nullable = false)
    private LocalTime length;

    @Column(name = "video_clip_release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "video_clip_times_played")
    private Long timesPlayed;

/*    @ManyToMany
    @JoinTable(
            name = "video_clip_creator",
            joinColumns = @JoinColumn(name = "video_clip_id"),
            inverseJoinColumns = @JoinColumn(name = "creator_id")
    )
    private List<Creator> creators;

    @ManyToMany
    @JoinTable(
            name = "video_clip_genre",
            joinColumns = @JoinColumn(name = "video_clip_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;*/

    @OneToMany(mappedBy = "videoClip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistEntry> playlistEntries = new ArrayList<>();

    @Column(name = "video_clip_active")
    private Boolean active;

    public VideoClip() {}

    public VideoClip(Long id, String title, String url, String description, LocalTime length, LocalDate releaseDate, Long timesPlayed, List<Creator> creators, List<Genre> genres, List<PlaylistEntry> entries, Boolean active) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.length = length;
        this.releaseDate = releaseDate;
        this.timesPlayed = timesPlayed;
        //this.creators = creators;
        //this.genres = genres;
        this.playlistEntries = entries;
        this.active = active;
    }

    public VideoClip (VideoClip videoClip) {
        this.id = videoClip.id;
        this.title = videoClip.title;
        this.url = videoClip.url;
        this.description = videoClip.description;
        this.length = videoClip.length;
        this.releaseDate = videoClip.releaseDate;
        this.timesPlayed = videoClip.timesPlayed;
       /* this.creators = videoClip.creators;
        this.genres = videoClip.genres;*/
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

    public Long getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(Long timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

/*    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }*/

    public List<PlaylistEntry> getPlaylistEntries() {
        return playlistEntries;
    }

    public void setPlaylistEntries(List<PlaylistEntry> playlistEntries) {
        this.playlistEntries = playlistEntries;
    }

    public Boolean getActive() {
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
                ", timesPlayed=" + timesPlayed +
              /*  ", creators=" + creators +
                ", genres=" + genres +*/
                ", playlistEntries=" + playlistEntries +
                ", active=" + active +
                '}';
    }
}
