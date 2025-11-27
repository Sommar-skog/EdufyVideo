package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.*;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.models.dtos.AddVideoClipDTO;
import com.example.EdufyVideo.models.dtos.PlayedDTO;
import com.example.EdufyVideo.models.dtos.UserDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    Authentication auth;

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
        auth = mock(Authentication.class);
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
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_video_user"))).when(auth).getAuthorities();

        when(mockVideoRepository.findAllByActiveTrue()).thenReturn(List.of());

        List<VideoClipResponseDTO> result = videoService.getAllVideoClips(auth);

        assertEquals(0, result.size());
        verifyNoInteractions(mockCreatorClient, mockGenreClient);
        verify(mockVideoRepository).findAllByActiveTrue();
        verify(mockVideoRepository, never()).findAll();

    }

    @Test
    void playVideoClipShouldThrowWhenUserIdIsNull() {
        when(mockUserClient.getUserBySub("0000000000000000")).thenReturn(new UserDTO(null));
        when(auth.getName()).thenReturn("0000000000000000");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoService.playVideoClip(1L, auth));

        assertEquals("UserClientImpl returned id null", exception.getMessage());
        verify(mockVideoRepository, never()).findVideoClipByIdAndActiveTrue(any());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void playVideoClipShouldThrowWhenVideoNotFound(){
        when(mockUserClient.getUserBySub("0000000000000000")).thenReturn(new UserDTO(1L));
        when(auth.getName()).thenReturn("0000000000000000");
        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                videoService.playVideoClip(1L, auth));

        assertEquals("Active VideoClip not found with id: 1", exception.getMessage());
        verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void playVideoClipShouldIncreaseUserHistoryCountWhenEntryExists(){
        Map<Long, Long> history = new HashMap<>();
        history.put(1L, 4L);
        video.setUserHistory(history);

        when(mockUserClient.getUserBySub("0000000000000000")).thenReturn(new UserDTO(1L));
        when(auth.getName()).thenReturn("0000000000000000");
        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));

        videoService.playVideoClip(1L,auth);

        assertEquals(5L, (long) video.getUserHistory().get(1L));
        verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
        verify(mockVideoRepository).save(any());
    }

    @Test
    void playVideoClipShouldAddHistoryEntryWhenUserHasNoHistory(){
        when(mockUserClient.getUserBySub("0000000000000000")).thenReturn(new UserDTO(1L));
        when(auth.getName()).thenReturn("0000000000000000");
        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));

        videoService.playVideoClip(1L,auth);

        assertTrue(video.getUserHistory().containsKey(1L));
        assertEquals(1L, (long) video.getUserHistory().get(1L));
        verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
        verify(mockVideoRepository).save(any());
    }

    @Test
    void playVideoClipShouldReturnCorrectPlayedDTO(){
        when(mockUserClient.getUserBySub("0000000000000000")).thenReturn(new UserDTO(1L));
        when(auth.getName()).thenReturn("0000000000000000");
        when(mockVideoRepository.findVideoClipByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));

        PlayedDTO result = videoService.playVideoClip(1L,auth);

        assertEquals(video.getUrl(), result.getUrl());
        verify(mockVideoRepository).findVideoClipByIdAndActiveTrue(1L);
        verify(mockVideoRepository).save(any());
    }

    @Test
    void getUserHistoryShouldReturnEmptyListWhenNoHistoryExists(){
        when(mockVideoRepository.findVideoIdsByUserIdInHistory(1L)).thenReturn(List.of());

            List<VideoClipResponseDTO> result = videoService.getUserHistory(1L);

            assertEquals(0, result.size());
            verify(mockVideoRepository).findVideoIdsByUserIdInHistory(1L);

    }

    @Test
    void getUserHistoryShouldReturnMappedDtosWhenHistoryExists(){
        when(mockVideoRepository.findVideoIdsByUserIdInHistory(1L)).thenReturn(List.of(video.getId()));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(VideoClipResponseMapper.class)) {
            mockedMapper.when(
                    () -> VideoClipResponseMapper.toDTOClientJustId(video.getId())
            ).thenReturn(videoResponseDTO);

            List<VideoClipResponseDTO> result = videoService.getUserHistory(1L);

            assertEquals(1, result.size());
            assertEquals(videoResponseDTO, result.getFirst());
            verify(mockVideoRepository).findVideoIdsByUserIdInHistory(1L);
        }
    }

    @Test
    void getUserHistoryShouldMapEachVideoIdExactlyOnce(){
        VideoClipResponseDTO dto1 = new VideoClipResponseDTO();
        VideoClipResponseDTO dto2 = new VideoClipResponseDTO();
        VideoClipResponseDTO dto3 = new VideoClipResponseDTO();

        when(mockVideoRepository.findVideoIdsByUserIdInHistory(1L)).thenReturn(List.of(1L,2L,3L));

        try (MockedStatic<VideoClipResponseMapper> mockedMapper = mockStatic(VideoClipResponseMapper.class)) {
            mockedMapper.when(() -> VideoClipResponseMapper.toDTOClientJustId(1L))
                    .thenReturn(dto1);
            mockedMapper.when(() -> VideoClipResponseMapper.toDTOClientJustId(2L))
                    .thenReturn(dto2);
            mockedMapper.when(() -> VideoClipResponseMapper.toDTOClientJustId(3L))
                    .thenReturn(dto3);

            List<VideoClipResponseDTO> result = videoService.getUserHistory(1L);

            assertEquals(List.of(dto1, dto2, dto3), result);
            mockedMapper.verify(() -> VideoClipResponseMapper.toDTOClientJustId(1L), times(1));
            mockedMapper.verify(() -> VideoClipResponseMapper.toDTOClientJustId(2L), times(1));
            mockedMapper.verify(() -> VideoClipResponseMapper.toDTOClientJustId(3L), times(1));
        }
    }

    @Test
    void addVideoClip() {
    }
}