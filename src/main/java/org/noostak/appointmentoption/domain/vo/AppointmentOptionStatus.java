package org.noostak.appointmentoption.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentOptionStatus {
    CONFIRMED("확정"),
    UNCONFIRMED("확정되지 않음")
    ;

    private final String message;
}
