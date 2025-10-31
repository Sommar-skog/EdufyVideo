package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.enteties.VideoPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//ED-79-AA
@Repository
public interface PlaylistRepository extends JpaRepository<VideoPlaylist, Long> {

}
