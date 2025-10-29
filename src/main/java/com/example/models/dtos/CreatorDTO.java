package com.example.models.dtos;


import com.example.models.enteties.VideoClip;
import com.example.models.enteties.VideoPlaylist;
import org.bouncycastle.jcajce.provider.asymmetric.edec.KeyPairGeneratorSpi;

import java.util.List;

//ED-124-AA
public class CreatorDTO {

    private Long id;
    private String username;

    public CreatorDTO() {}

    public CreatorDTO(Long id, String username, List<VideoClip> videoClips, List<VideoPlaylist> videoPlaylists) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CreatorDTO{" +
                "id=" + id +
                ", username='" + username +
                '}';
    }
}
