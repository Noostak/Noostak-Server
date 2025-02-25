package org.noostak.likes.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecreaseResponse {
    int likes;

    public static DecreaseResponse of(int likes){
        return new DecreaseResponse(likes);
    }
}
