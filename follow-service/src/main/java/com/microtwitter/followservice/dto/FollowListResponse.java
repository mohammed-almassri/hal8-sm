package com.microtwitter.followservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class FollowListResponse {
    private List<FollowResponse> follows;
    private long totalCount;
}
