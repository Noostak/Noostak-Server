package org.noostak.appointmentmember.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AvailabilityStatus {

    ABLE("참여"),
    NOT_ABLE("불참");

    private final String message;
}
