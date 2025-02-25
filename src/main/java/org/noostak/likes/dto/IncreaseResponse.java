package org.noostak.likes.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncreaseResponse {
    int likes;

    public static IncreaseResponse of(int likes){
        return new IncreaseResponse(likes);
    }
}
