package com.example.EdufyVideo.repositories;

import com.example.EdufyVideo.models.enteties.VideoClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository  extends JpaRepository<VideoClip, Long> {
}
