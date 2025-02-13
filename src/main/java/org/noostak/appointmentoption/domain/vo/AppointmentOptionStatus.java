package org.noostak.appointmentoption.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppointmentOptionStatus {

    SELECTED("선택"),
    UNSELECTED("미선택")

    ;

    private final String message;
}
