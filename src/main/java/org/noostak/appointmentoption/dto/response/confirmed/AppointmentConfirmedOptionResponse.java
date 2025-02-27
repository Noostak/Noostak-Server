package org.noostak.appointmentoption.dto.response.confirmed;


public record AppointmentConfirmedOptionResponse(
        AppointmentConfirmedOptionTimeResponse appointmentTime,
        String category,
        String appointmentName,
        AppointmentConfirmedOptionMyInfoResponse myInfo,
        AppointmentConfirmedOptionAvailableFriendsResponse availableFriends,
        AppointmentConfirmedOptionUnavailableFriendsResponse unavailableFriends
) {
    public static AppointmentConfirmedOptionResponse of(AppointmentConfirmedOptionTimeResponse appointmentTime, String category, String appointmentName, AppointmentConfirmedOptionMyInfoResponse myInfo, AppointmentConfirmedOptionAvailableFriendsResponse availableFriends, AppointmentConfirmedOptionUnavailableFriendsResponse unavailableFriends) {
        return new AppointmentConfirmedOptionResponse(appointmentTime, category, appointmentName, myInfo, availableFriends, unavailableFriends);
    }
}
