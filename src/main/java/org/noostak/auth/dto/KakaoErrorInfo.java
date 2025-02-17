package org.noostak.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoErrorInfo {
    private String error;
    private String errorDescription;
    private String errorCode;

    public boolean hasError(){
        return (this.error != null)
                || !( this.error.isBlank() && this.error.isEmpty());
    }
}
