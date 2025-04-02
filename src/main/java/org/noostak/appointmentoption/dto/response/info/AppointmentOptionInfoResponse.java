package org.noostak.appointmentoption.dto.response.info;

public record AppointmentOptionInfoResponse(AppointmentOptionInfoTimeResponse appointmentTime,
                                            String category,
                                            String appointmentName,
                                            AppointmentOptionInfoMyInfoResponse myInfo,
                                            AppointmentOptionInfoAvailableFriendsResponse availableFriends,
                                            AppointmentOptionInfoUnavailableFriendsResponse unavailableFriends
) {
    public static AppointmentOptionInfoResponse of(AppointmentOptionInfoTimeResponse appointmentTime, String category, String appointmentName, AppointmentOptionInfoMyInfoResponse myInfo, AppointmentOptionInfoAvailableFriendsResponse availableFriends, AppointmentOptionInfoUnavailableFriendsResponse unavailableFriends) {
        return new AppointmentOptionInfoResponse(appointmentTime, category, appointmentName, myInfo, availableFriends, unavailableFriends);
    }
}