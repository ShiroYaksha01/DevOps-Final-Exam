package net.orderzone.idcard.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Utility to build default profiles.
 */
public class ProfileBuilder {

    public static Profile buildDefaultProfile(ProfileType type, String registrationNumber, String fullName) {
        return Profile.builder()
                .uuid(UUID.randomUUID().toString())
                .registrationNumber(registrationNumber)
                .type(type)
                .fullName(fullName)
                .issueDate(LocalDate.now())
                .barcodeType(BarcodeType.CODE_128)
                .build();
    }

    public static Profile buildDefaultStudent(String registrationNumber, String fullName) {
        return buildDefaultProfile(ProfileType.STUDENT, registrationNumber, fullName);
    }

    public static Profile buildDefaultEmployee(String registrationNumber, String fullName) {
        return buildDefaultProfile(ProfileType.EMPLOYEE, registrationNumber, fullName);
    }
}
