package org.noostak.appointmentmember.dto.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record AppointmentMemberAvailableTimesRequest(
        List<AppointmentMemberAvailableTimeRequest> appointmentMemberAvailableTimes
) {
    public static AppointmentMemberAvailableTimesRequest of(List<AppointmentMemberAvailableTimeRequest> appointmentMemberAvailableTimes) {
        return new AppointmentMemberAvailableTimesRequest(appointmentMemberAvailableTimes);
    }

    public static List<AppointmentMemberAvailableTimeRequest> combineContinuousTimes
            (AppointmentMemberAvailableTimesRequest request){

        List<AppointmentMemberAvailableTimeRequest> times = request.appointmentMemberAvailableTimes();

        if(times.isEmpty()){
            return times;
        }

        List<AppointmentMemberAvailableTimeRequest> createTimes = new ArrayList<>();

        LocalDateTime saveDate = times.getFirst().date();
        LocalDateTime saveStartTime = times.getFirst().startTime();
        LocalDateTime saveEndTime = times.getFirst().endTime();

        for(int i=0; i < times.size(); i++){
            AppointmentMemberAvailableTimeRequest time = times.get(i);
            boolean isContinuousTime = time.startTime().isEqual(saveEndTime);

            // 연속적인 시간일 때 이어붙이기
            if(isContinuousTime || i == 0){
                saveEndTime = time.endTime();

                // 마지막 시간일 때 저장하고 루프 종료
                if(i == times.size() - 1){
                    createTimes.add(AppointmentMemberAvailableTimeRequest.of(saveDate,saveStartTime,saveEndTime));
                    break;
                }

                continue;
            }

            // 연속적인 시간이 아니라면 저장하고, 다음 저장시간을 갱신하기
            createTimes.add(AppointmentMemberAvailableTimeRequest.of(saveDate,saveStartTime,saveEndTime));

            saveDate = time.date();
            saveStartTime = time.startTime();
            saveEndTime = time.endTime();
        }

        return createTimes;
    }
}
