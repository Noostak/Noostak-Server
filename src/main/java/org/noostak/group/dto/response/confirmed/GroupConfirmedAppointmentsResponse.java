package org.noostak.group.dto.response.confirmed;

import java.util.List;

public record GroupConfirmedAppointmentsResponse(
        GroupConfirmedInfoResponse groupConfirmedInfoResponse,
        List<ConfirmedAppointmentsResponse> confirmedAppointmentsResponse
) {
    public static GroupConfirmedAppointmentsResponse of(GroupConfirmedInfoResponse groupConfirmedInfoResponse, List<ConfirmedAppointmentsResponse> confirmedAppointmentsResponse) {
        return new GroupConfirmedAppointmentsResponse(groupConfirmedInfoResponse, confirmedAppointmentsResponse);
    }
}
