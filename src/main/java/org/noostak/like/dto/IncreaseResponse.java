package org.noostak.like.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncreaseResponse {
    long likes;

    public static IncreaseResponse of(long likes){
        return new IncreaseResponse(likes);
    }
}
