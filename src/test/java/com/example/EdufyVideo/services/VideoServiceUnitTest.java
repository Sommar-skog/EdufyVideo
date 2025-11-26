package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.*;
import com.example.EdufyVideo.models.dtos.AddPlaylistDTO;
import com.example.EdufyVideo.models.dtos.AddVideoClipDTO;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

//ED-316-AA Unit tests
@ExtendWith(MockitoExtension.class)
class VideoServiceUnitTest {

    @Mock
    private VideoRepository mockVideoRepository;
    @Mock
    private PlaylistService mockPlaylistService;

    @Mock
    private CreatorClient mockCreatorClient;
    @Mock
    private GenreClient mockGenreClient;
    @Mock
    private UserClient mockUserClient;
    @Mock
    private ThumbClient mockThumbClient;

    @InjectMocks
    private VideoServiceImpl videoService;

    private AddVideoClipDTO addVideoClipDTO;

    @BeforeEach
    void setUp() {
        addVideoClipDTO = new AddVideoClipDTO(
                "Test title",
                "Test Description",
                List.of(1L,2L),
                "http://test.com",
                LocalTime.of(0,10,32),
                List.of(1L,2L),
                List.of(1L)
        );
    }


    @Test
    void getVideoClipById() {
    }

    @Test
    void getVideoClipsByTitle() {
    }

    @Test
    void getAllVideoClips() {
    }

    @Test
    void playVideoClip() {
    }

    @Test
    void getUserHistory() {
    }

    @Test
    void addVideoClip() {
    }
}