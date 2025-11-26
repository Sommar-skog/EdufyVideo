package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.*;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.AddVideoClipDTO;
import com.example.EdufyVideo.models.dtos.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.repositories.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    private VideoClip video;
    private VideoClipResponseDTO videoResponseDTO;

    @BeforeEach
    void setUp() {
        addVideoClipDTO = new AddVideoClipDTO(
                "Test title",
                "Test Description",
                List.of(1L,2L),
                "https://test.com",
                LocalTime.of(0,10,32),
                List.of(1L,2L),
                List.of(1L)
        );

        video = new VideoClip(
                1L,
                "Test title",
                "https://test.com",
                "Test Description",
                LocalTime.of(0, 10, 32),
                LocalDate.now(),
                new HashMap<>(),
                List.of(new PlaylistEntry()),
                true
        );

        videoResponseDTO = new VideoClipResponseDTO();

    }

    @Test
    void getVideoClipByIdShouldReturnAdminDtoWhenRoleIsAdmin(){

        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_admin");

        when(mockVideoRepository.findById(1L)).thenReturn(Optional.of(video));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(VideoClipResponseMapper.class)) {
            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOAdmin(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            VideoClipResponseDTO result = videoService.getVideoClipById(1L, roles);

            assertEquals(videoResponseDTO, result);
            verify(mockVideoRepository).findById(1L);
        }

    }

    @Test
    void getVideoClipByIdShouldThrowWhenAdminVideoNotFound(){
        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_admin");

        when(mockVideoRepository.findById(-1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoService.getVideoClipById(-1L, roles));

        assertEquals("VideoClip not found with id: -1", exception.getMessage());

        verify(mockVideoRepository).findById(-1L);
    }

    @Test
    void getVideoClipByIdShouldReturnUserDtoWhenRoleIsUser(){
        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_user");

        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper =
                     mockStatic(VideoClipResponseMapper.class)) {

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOUser(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            VideoClipResponseDTO result = videoService.getVideoClipById(1L, roles);

            assertEquals(videoResponseDTO, result);
            verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
        }
    }

    @Test
    void getVideoClipByIdShouldThrowWhenUserVideoNotFound(){
        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_user");

        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(-1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoService.getVideoClipById(-1L, roles));

        assertEquals("VideoClip not found with id: -1", exception.getMessage());

        verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(-1L);
    }

    @Test
    void getVideoClipByIdShouldUseFindByIdForAdminRole() {
        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_admin");

        when(mockVideoRepository.findById(1L)).thenReturn(Optional.of(video));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper =
                     mockStatic(VideoClipResponseMapper.class)) {

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOAdmin(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            videoService.getVideoClipById(1L, roles);

            verify(mockVideoRepository).findById(1L);
            verify(mockVideoRepository, never()).findVideoClipByIdAndActiveTrue(any());
        }
    }

    @Test
    void getVideoClipByIdShouldUseFindActiveForUserRole() {
        Collection<GrantedAuthority> roles = List.of(() -> "ROLE_video_user");

        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(video));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper =
                     mockStatic(VideoClipResponseMapper.class)) {

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOUser(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            videoService.getVideoClipById(1L, roles);

            verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
            verify(mockVideoRepository, never()).findById(any());
        }
    }

    @Test
    void getVideoClipsByTitleShouldReturnListOfDtos() {
        String title = "Test";

        when(mockVideoRepository.findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(title)).thenReturn(List.of(video));

        try(MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(
                VideoClipResponseMapper.class)){

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOUser(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            List<VideoClipResponseDTO> result = videoService.getVideoClipsByTitle(title);

            assertEquals(1, result.size());
            assertEquals(videoResponseDTO, result.get(0));

            verify(mockVideoRepository).findVideoClipByTitleContainingIgnoreCaseAndActiveTrue(title);
        }
    }

    @Test
    void getVideoClipsByTitleShouldThrowWhenNoVideosFound(){

        when(mockVideoRepository.findVideoClipByTitleContainingIgnoreCaseAndActiveTrue("test")).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoService.getVideoClipsByTitle("test"));

        assertEquals("VideoClip not found with title containing: test", exception.getMessage());

        verifyNoInteractions(mockCreatorClient, mockGenreClient);
        verify(mockVideoRepository).findVideoClipByTitleContainingIgnoreCaseAndActiveTrue("test");
    }

    @Test
    void getAllVideoClipsShouldReturnAdminDtosWhenRoleIsAdmin() {
        Authentication auth = mock(Authentication.class);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_video_admin"))).when(auth).getAuthorities();

        when(mockVideoRepository.findAll()).thenReturn(List.of(video));

        try(MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(
                VideoClipResponseMapper.class)) {

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOAdmin(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            List<VideoClipResponseDTO> result = videoService.getAllVideoClips(auth);

            assertEquals(1, result.size());
            assertEquals(videoResponseDTO, result.getFirst());

            verify(mockVideoRepository).findAll();
            verify(mockVideoRepository, never()).findAllByActiveTrue();
        }

    }

    @Test
    void getAllVideoClipsShouldReturnEmptyListWhenRepositoryReturnsEmptyListForAdmin(){
        Authentication auth = mock(Authentication.class);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_video_admin"))).when(auth).getAuthorities();

        when(mockVideoRepository.findAll()).thenReturn(List.of());

        List<VideoClipResponseDTO> result = videoService.getAllVideoClips(auth);

        assertEquals(0, result.size());
        verifyNoInteractions(mockCreatorClient, mockGenreClient);
        verify(mockVideoRepository).findAll();
        verify(mockVideoRepository, never()).findAllByActiveTrue();
    }

    @Test
    void getAllVideoClipsShouldReturnUserDtosWhenRoleIsUser(){
        Authentication auth = mock(Authentication.class);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_video_user"))).when(auth).getAuthorities();

        when(mockVideoRepository.findAllByActiveTrue()).thenReturn(List.of(video));

        try(MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(
                VideoClipResponseMapper.class)) {

            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOUser(video, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            List<VideoClipResponseDTO> result = videoService.getAllVideoClips(auth);

            assertEquals(1, result.size());
            assertEquals(videoResponseDTO, result.getFirst());

            verify(mockVideoRepository).findAllByActiveTrue();
            verify(mockVideoRepository, never()).findAll();
        }
    }

    @Test
    void getAllVideoClipsShouldReturnEmptyListWhenRepositoryReturnsEmptyListForUser(){
        Authentication auth = mock(Authentication.class);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_video_user"))).when(auth).getAuthorities();

        when(mockVideoRepository.findAllByActiveTrue()).thenReturn(List.of());

        List<VideoClipResponseDTO> result = videoService.getAllVideoClips(auth);

        assertEquals(0, result.size());
        verifyNoInteractions(mockCreatorClient, mockGenreClient);
        verify(mockVideoRepository).findAllByActiveTrue();
        verify(mockVideoRepository, never()).findAll();

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