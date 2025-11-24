package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.models.dtos.CreatorDTO;
import com.example.EdufyVideo.models.dtos.MediaDTO;
import com.example.EdufyVideo.models.enums.MediaType;

import java.util.List;

//ED-345-AA
public interface ClientService {
    CreatorDTO getCreatorById(Long creatorId);
    List<MediaDTO> getMediaListFromCreator(Long creatorId, MediaType mediaType);
    List<CreatorDTO> getCreatorsByMediaTypeAndMediaId(MediaType mediaType, long mediaId);
    boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, List<Long> creatorIds);
    List<String> getCreatorUsernamesByMedia(MediaType mediaType, long mediaId);
    List<String> getCreatorIdAndUsernameByMedia(MediaType mediaType, long mediaId);
}
