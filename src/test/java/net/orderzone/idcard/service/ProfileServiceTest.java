package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save_ShouldGenerateUuidAndRegistrationNumber_WhenNull() {
        Profile p = new Profile();
        p.setType(ProfileType.STUDENT);
        p.setFullName("John Doe");

        when(idGeneratorService.generateUuid()).thenReturn("test-uuid-123");
        when(profileRepository.count()).thenReturn(0L);
        when(idGeneratorService.generateRegistrationNumber("STUDENT", 1L)).thenReturn("2026-STU-0001");
        when(profileRepository.save(any(Profile.class))).thenAnswer(i -> i.getArguments()[0]);

        Profile saved = profileService.save(p);

        assertEquals("test-uuid-123", saved.getUuid());
        assertEquals("2026-STU-0001", saved.getRegistrationNumber());
        verify(profileRepository).save(p);
    }
}
