package org.noostak.appointment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.noostak.appointment.common.exception.AppointmentErrorCode;
import org.noostak.appointment.common.exception.AppointmentException;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum AppointmentCategory {
    IMPORTANT("중요"),
    SCHEDULE("일정"),
    HOBBY("취미"),
    OTHER("기타");

    private final String message;

    public static AppointmentCategory from(String category) {
        validateCategory(category);
        return findCategory(category)
                .orElseThrow(() -> new AppointmentException(AppointmentErrorCode.APPOINTMENT_CATEGORY_NOT_FOUND));
    }

    private static void validateCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CATEGORY_NULL_OR_BLANK);
        }
        if (!category.trim().equals(category) || category.contains(" ")) {
            throw new AppointmentException(AppointmentErrorCode.APPOINTMENT_CATEGORY_INVALID_FORMAT);
        }
    }

    private static Optional<AppointmentCategory> findCategory(String category) {
        return Arrays.stream(values())
                .filter(c -> c.message.equals(category))
                .findFirst();
    }
}
