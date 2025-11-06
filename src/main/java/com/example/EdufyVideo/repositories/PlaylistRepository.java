package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//ED-79-AA
@Repository
public interface PlaylistRepository extends JpaRepository<VideoPlaylist, Long> {

    //ED-252-AA
    Optional<VideoPlaylist> findByIdAndActiveTrue(Long id);

    List<VideoPlaylist> findVideoPlaylistByTitleContainingIgnoreCaseAndActiveTrue(String title);

}
