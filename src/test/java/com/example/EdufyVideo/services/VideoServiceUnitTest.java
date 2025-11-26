package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.CreatorClientImpl;
import com.example.EdufyVideo.clients.GenreClientImpl;
import com.example.EdufyVideo.clients.ThumbClientImpl;
import com.example.EdufyVideo.clients.UserClientImpl;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//ED-316-AA Unit tests
@ExtendWith(MockitoExtension.class)
class VideoServiceUnitTest {

    @Mock
    private VideoRepository mockVideoRepository;
    @Mock
    private PlaylistService mockPlaylistService;

    @Mock
    private CreatorClientImpl mockCreatorClient;
    @Mock
    private GenreClientImpl mockGenreClient;
    @Mock
    private UserClientImpl mockUserClient;
    @Mock
    private ThumbClientImpl mockThumbClient;


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