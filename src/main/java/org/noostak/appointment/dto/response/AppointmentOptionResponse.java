package org.noostak.appointment.dto.response;

public record AppointmentOptionResponse(
        Long optionId,
        Long likes,
        boolean liked,
        Long groupMemberCount,
        Long availableMemberCount,
        AppointmentOptionTimeResponse appointmentOptionTime,
        AppointmentMyInfoResponse myInfo,
        AvailableFriendsResponse availableFriends,
        UnavailableFriendsResponse unavailableFriends
) {
    public static AppointmentOptionResponse of(
            Long optionId, Long likes, boolean liked, Long groupMemberCount, Long availableMemberCount,
            AppointmentOptionTimeResponse appointmentOptionTime, AppointmentMyInfoResponse myInfo,
            AvailableFriendsResponse availableFriends, UnavailableFriendsResponse unavailableFriends) {
        return new AppointmentOptionResponse(optionId, likes, liked, groupMemberCount, availableMemberCount,
                appointmentOptionTime, myInfo, availableFriends, unavailableFriends);
    }
}
