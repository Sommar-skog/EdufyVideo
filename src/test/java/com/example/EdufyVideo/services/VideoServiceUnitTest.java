package com.example.EdufyVideo.services;

import com.example.EdufyVideo.clients.*;
import com.example.EdufyVideo.exceptions.InvalidInputException;
import com.example.EdufyVideo.exceptions.ResourceNotFoundException;
import com.example.EdufyVideo.exceptions.UniqueConflictException;
import com.example.EdufyVideo.models.dtos.videos.inputs.AddVideoClipDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.PlayedDTO;
import com.example.EdufyVideo.models.dtos.clients.users.UserDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.VideoClipResponseDTO;
import com.example.EdufyVideo.models.dtos.videos.responses.mappers.VideoClipResponseMapper;
import com.example.EdufyVideo.models.enteties.PlaylistEntry;
import com.example.EdufyVideo.models.enteties.VideoClip;
import com.example.EdufyVideo.models.enums.MediaType;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//ED-316-AA All unit tests
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
        System.out.println(addVideoClipDTO);

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
            assertEquals(videoResponseDTO, result.getFirst());

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
    void addVideoClipShouldThrowWhenTitleIsNull(){
        addVideoClipDTO.setTitle(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Title cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenTitleIsTooLong(){
        addVideoClipDTO.setTitle("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Title cannot exceed 100 characters", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenTitleBlank(){
        addVideoClipDTO.setTitle("");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Title cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenUrlBlank(){
        addVideoClipDTO.setUrl("");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Url cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenUrlNull(){
        addVideoClipDTO.setUrl(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Url cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenUrlInvalid() {
        addVideoClipDTO.setUrl("fppt://test.se");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Url must start with http:// or https://", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldAcceptValidHttpUrl() {
        addVideoClipDTO.setUrl("http://example.com");

        when(mockVideoRepository.existsByUrl("http://example.com"))
                .thenReturn(false);
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(anyLong())).thenReturn(video);

        try (MockedStatic<VideoClipResponseMapper> mapper = mockStatic(VideoClipResponseMapper.class)) {
            mapper.when(() -> VideoClipResponseMapper.toDTOAdmin(video, mockCreatorClient, mockGenreClient))
                    .thenReturn(videoResponseDTO);

            VideoClipResponseDTO result = videoService.addVideoClip(addVideoClipDTO);

            assertNotNull(result);
        }
    }

    @Test
    void addVideoClipShouldAcceptValidHttpsUrl() {
        addVideoClipDTO.setUrl("https://example.com");

        when(mockVideoRepository.existsByUrl("https://example.com"))
                .thenReturn(false);
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(anyLong())).thenReturn(video);

        try (MockedStatic<VideoClipResponseMapper> mapper = mockStatic(VideoClipResponseMapper.class)) {
            mapper.when(() -> VideoClipResponseMapper.toDTOAdmin(video, mockCreatorClient, mockGenreClient))
                    .thenReturn(videoResponseDTO);

            VideoClipResponseDTO result = videoService.addVideoClip(addVideoClipDTO);

            assertNotNull(result);
        }
    }

    @Test
    void addVideoClipShouldThrowWhenDescriptionBlank(){
        addVideoClipDTO.setDescription("");

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Description cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenDescriptionNull(){
        addVideoClipDTO.setDescription(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Description cannot be null or blank", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenDescriptionIsTooLong(){
        String desc256 = "a".repeat(256);
        addVideoClipDTO.setDescription(desc256);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Description cannot exceed 255 characters", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenLengthIsNull(){
        addVideoClipDTO.setLength(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Length cannot be null", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenLengthIsZero(){
        addVideoClipDTO.setLength(LocalTime.of(0, 0,0));

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("Length cannot be 00:00:00", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenGenreIdsIsNull(){
        addVideoClipDTO.setGenreIds(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("At least one genres must be provided", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenGenreIdsIsEmpty(){
        addVideoClipDTO.setGenreIds(List.of());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("At least one genres must be provided", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenCreatorIdsIsNull(){
        addVideoClipDTO.setCreatorIds(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("At least one creator must be provided", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenCreatorIdsIsEmpty(){
        addVideoClipDTO.setCreatorIds(List.of());

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                videoService.addVideoClip(addVideoClipDTO));

        assertEquals("At least one creator must be provided", exception.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldThrowWhenPlaylistIdIsNotPositive() {
        addVideoClipDTO.setPlaylistIds(List.of(-1L));

        InvalidInputException ex = assertThrows(
                InvalidInputException.class,
                () -> videoService.addVideoClip(addVideoClipDTO)
        );

        assertEquals("Playlist id must be a positive number", ex.getMessage());
        verify(mockVideoRepository, never()).save(any());
    }

    @Test
    void addVideoClipShouldNotValidatePlaylistIdsWhenListIsNullOrEmpty() {
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.existsByUrl(any())).thenReturn(false);
        when(mockVideoRepository.findWithPlaylists(any())).thenReturn(video);

        addVideoClipDTO.setPlaylistIds(null);
        videoService.addVideoClip(addVideoClipDTO);
        verify(mockPlaylistService, never()).addVideoClipToPlaylists(any(), any());

        addVideoClipDTO.setPlaylistIds(Collections.emptyList());
        videoService.addVideoClip(addVideoClipDTO);
        verify(mockPlaylistService, never()).addVideoClipToPlaylists(any(), any());
    }


    @Test
    void addVideoClipShouldThrowWhenUrlIsNotUnique() {
        addVideoClipDTO.setUrl("https://duplicate.com");

        when(mockVideoRepository.existsByUrl("https://duplicate.com")).thenReturn(true);

        UniqueConflictException ex = assertThrows(
                UniqueConflictException.class, () -> videoService.addVideoClip(addVideoClipDTO));

        assertEquals("url: {https://duplicate.com} already exists, duplicates is not allowed.", ex.getMessage());

        verify(mockVideoRepository).existsByUrl("https://duplicate.com");
        verify(mockVideoRepository, never()).save(any());
    }



    @Test
    void addVideoClipShouldSaveVideoClipAndReturnMappedDto(){
        VideoClip saved = mock(VideoClip.class);
        when(mockVideoRepository.save(any())).thenReturn(saved);

        VideoClip withPlaylists = mock(VideoClip.class);
        when(mockVideoRepository.findWithPlaylists(any())).thenReturn(withPlaylists);

        try (MockedStatic<VideoClipResponseMapper> mapper = mockStatic(VideoClipResponseMapper.class)) {

            mapper.when(() ->
                    VideoClipResponseMapper.toDTOAdmin(withPlaylists, mockCreatorClient, mockGenreClient)
            ).thenReturn(videoResponseDTO);

            VideoClipResponseDTO result = videoService.addVideoClip(addVideoClipDTO);

            assertEquals(videoResponseDTO, result);
            verify(mockVideoRepository).save(any());
            verify(mockVideoRepository).findWithPlaylists(any());
            mapper.verify(() -> VideoClipResponseMapper.toDTOAdmin(withPlaylists, mockCreatorClient, mockGenreClient), times(1));
        }
    }

    @Test
    void addVideoClipShouldCallPlaylistServiceWhenPlaylistIdsExist() {
        addVideoClipDTO.setPlaylistIds(List.of(10L, 20L));

        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(1L)).thenReturn(video);

        try (MockedStatic<VideoClipResponseMapper> mocked = mockStatic(VideoClipResponseMapper.class)) {
            mocked.when(() -> VideoClipResponseMapper.toDTOAdmin(any(), any(), any()))
                    .thenReturn(videoResponseDTO);

            videoService.addVideoClip(addVideoClipDTO);

            verify(mockPlaylistService).addVideoClipToPlaylists(List.of(10L, 20L), video);
        }
    }

    @Test
    void addVideoClipShouldNotCallPlaylistServiceWhenPlaylistIdsNullOrEmpty() {
        addVideoClipDTO.setPlaylistIds(null);

        VideoClip saved = mock(VideoClip.class);
        when(mockVideoRepository.save(any())).thenReturn(saved);
        when(mockVideoRepository.findWithPlaylists(anyLong())).thenReturn(saved);

        try (MockedStatic<VideoClipResponseMapper> mapper = mockStatic(VideoClipResponseMapper.class)) {
            mapper.when(() -> VideoClipResponseMapper.toDTOAdmin(any(), any(), any()))
                    .thenReturn(videoResponseDTO);

            videoService.addVideoClip(addVideoClipDTO);

            verify(mockPlaylistService, never()).addVideoClipToPlaylists(any(), any());
        }
    }

    @Test
    void addVideoClipShouldCallGenreClientWithCorrectValues(){
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(video.getId())).thenReturn(video);

        videoService.addVideoClip(addVideoClipDTO);

        verify(mockGenreClient).createRecordeOfMedia(
                MediaType.VIDEO_CLIP,
                video.getId(),
                addVideoClipDTO.getGenreIds()
        );
    }

    @Test
    void addVideoClipShouldCallThumbClientWithCorrectValues() {
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(video.getId())).thenReturn(video);

        videoService.addVideoClip(addVideoClipDTO);

        verify(mockThumbClient).createRecordeOfMedia(
                MediaType.VIDEO_CLIP,
                video.getId(),
                video.getTitle()
        );
    }

    @Test
    void addVideoClipShouldCallCreatorClientWithCorrectValues(){
        when(mockVideoRepository.save(any())).thenReturn(video);
        when(mockVideoRepository.findWithPlaylists(anyLong())).thenReturn(video);

        videoService.addVideoClip(addVideoClipDTO);

        verify(mockCreatorClient).createRecordeOfMedia(
                MediaType.VIDEO_CLIP,
                video.getId(),
                addVideoClipDTO.getCreatorIds()
        );
    }

    @Test
    void addVideoClipShouldMapUsingToDTOAdmin() {
        VideoClip saved = new VideoClip("t","u","d",LocalTime.of(0,1,0));
        saved.setId(10L);

        when(mockVideoRepository.save(any(VideoClip.class))).thenReturn(saved);
        when(mockVideoRepository.findWithPlaylists(10L)).thenReturn(saved);

        try (MockedStatic<VideoClipResponseMapper> mocked = mockStatic(VideoClipResponseMapper.class)) {

            VideoClipResponseDTO dto = new VideoClipResponseDTO();
            mocked.when(() -> VideoClipResponseMapper.toDTOAdmin(saved, mockCreatorClient, mockGenreClient))
                    .thenReturn(dto);

            VideoClipResponseDTO result = videoService.addVideoClip(addVideoClipDTO);

            assertSame(dto, result);
            mocked.verify(() -> VideoClipResponseMapper.toDTOAdmin(saved, mockCreatorClient, mockGenreClient), times(1));
        }
    }

    @Test
    void addVideoClipShouldReloadVideoClipBeforeMapping() {
        VideoClip saved = mock(VideoClip.class);
        VideoClip reloaded = mock(VideoClip.class);

        when(mockVideoRepository.save(any())).thenReturn(saved);
        when(mockVideoRepository.findWithPlaylists(saved.getId())).thenReturn(reloaded);

        try (MockedStatic<VideoClipResponseMapper> mapper = mockStatic(VideoClipResponseMapper.class)) {
            mapper.when(() -> VideoClipResponseMapper.toDTOAdmin(eq(reloaded), any(), any()))
                    .thenReturn(videoResponseDTO);

            videoService.addVideoClip(addVideoClipDTO);

            verify(mockVideoRepository).findWithPlaylists(saved.getId());
            mapper.verify(() -> VideoClipResponseMapper.toDTOAdmin(eq(reloaded), any(), any()));
        }
    }

}