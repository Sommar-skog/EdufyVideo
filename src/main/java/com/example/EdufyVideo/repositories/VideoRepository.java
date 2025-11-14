package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.enteties.VideoClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository  extends JpaRepository<VideoClip, Long> {

    //ED-252-AA
    Optional<VideoClip> findVideoClipByIdAndActiveTrue(Long id);

    //ED-57-AA
    List<VideoClip> findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(String title);

    //ED-84-AA
    List<VideoClip> findAllByActiveTrue();

    //ED-282-AA
    // Returns all video IDs watched by the specified user
    @Query("SELECT v.id FROM VideoClip v JOIN v.userHistory h WHERE KEY(h) = :userId")
    List<Long> findVideoIdsByUserIdInHistory(@Param("userId") Long userId);

}
