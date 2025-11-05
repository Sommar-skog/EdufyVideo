package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.enteties.VideoClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository  extends JpaRepository<VideoClip, Long> {

    //ED-252-AA
    Optional<VideoClip> findVideoClipByIdAndActiveTrue(Long id);

    //ED-57-AA
    List<VideoClip> findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(String title);
}
