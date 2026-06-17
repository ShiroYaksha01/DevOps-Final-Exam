package net.orderzone.idcard.service;

import org.springframework.stereotype.Service;
import java.util.UUID;
import java.time.Year;

@Service
public class IdGeneratorService {

    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public String generateRegistrationNumber(String type, long sequence) {
        String year = String.valueOf(Year.now().getValue());
        String typeCode = type != null && type.length() >= 3 ? type.substring(0, 3).toUpperCase() : "USR";
        return String.format("%s-%s-%04d", year, typeCode, sequence);
    }
}
