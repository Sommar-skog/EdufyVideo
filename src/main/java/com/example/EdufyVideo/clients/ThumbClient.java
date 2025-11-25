package com.example.EdufyVideo.clients;


import com.example.EdufyVideo.models.enums.MediaType;

//ED-345-AA
public interface ThumbClient {
    boolean createRecordeOfMedia(MediaType mediaType, Long mediaId, String mediaName);
}
