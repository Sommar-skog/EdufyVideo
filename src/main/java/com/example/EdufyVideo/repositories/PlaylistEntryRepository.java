package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.entities.PlaylistEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//ED-243-AA
@Repository
public interface PlaylistEntryRepository extends CrudRepository<PlaylistEntry, Long> {

    int countByPlaylistId(Long playlistId);
}
