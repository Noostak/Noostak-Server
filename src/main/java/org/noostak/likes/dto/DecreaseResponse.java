package org.noostak.likes.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecreaseResponse {
    long likes;

    public static DecreaseResponse of(long likes){
        return new DecreaseResponse(likes);
    }
}
