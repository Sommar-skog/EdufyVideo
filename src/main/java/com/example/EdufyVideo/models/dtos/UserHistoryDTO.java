package com.example.EdufyVideo.models.dtos;

import java.util.Map;

public class UserHistoryDTO {

    private Long userId;
    private Map<Long, Long> SingIdAndTimesPlayed;

    public UserHistoryDTO() {}

    public UserHistoryDTO(Long userId, Map<Long, Long> SingIdAndTimesPlayed) {
        this.userId = userId;
        this.SingIdAndTimesPlayed = SingIdAndTimesPlayed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, Long> getSingIdAndTimesPlayed() {
        return SingIdAndTimesPlayed;
    }

    public void setSingIdAndTimesPlayed(Map<Long, Long> singIdAndTimesPlayed) {
        SingIdAndTimesPlayed = singIdAndTimesPlayed;
    }

    @Override
    public String toString() {
        return "UserHistoryDTO{" +
                "userId=" + userId +
                ", SingIdAndTimesPlayed=" + SingIdAndTimesPlayed +
                '}';
    }
}
