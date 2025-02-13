package org.noostak.appointmentoption.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentOptionStatus {

    USED("사용"),
    UNUSED("미사용")

    ;

    private final String message;
}
