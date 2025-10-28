package com.example.EdufyVideo.enteties;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @ManyToMany
    @JoinColumn(name = "video_clip_creators", nullable = false)
    private List<Creator> creators;

    @ManyToMany
    @JoinColumn(name = "video_clip_genres", nullable = false)
    private List<Genre> genres;

    @ManyToMany
    @JoinColumn(name = "video_clip_nr_in_playlist")
    private Map<Playlist, >


    @Column(name = "video_clip_active")
    private Boolean active;
}
