package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final IdGeneratorService idGeneratorService;

    public ProfileService(ProfileRepository profileRepository, IdGeneratorService idGeneratorService) {
        this.profileRepository = profileRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id);
    }

    @Transactional
    public Profile save(Profile profile) {
        if (profile.getUuid() == null) {
            profile.setUuid(idGeneratorService.generateUuid());
        }
        if (profile.getRegistrationNumber() == null || profile.getRegistrationNumber().isEmpty()) {
            long count = profileRepository.count() + 1;
            profile.setRegistrationNumber(idGeneratorService.generateRegistrationNumber(profile.getType().name(), count));
        }
        return profileRepository.save(profile);
    }

    @Transactional
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }

    @Transactional
    public List<Profile> saveAll(List<Profile> profiles) {
        for (Profile p : profiles) {
            if (p.getUuid() == null) {
                p.setUuid(idGeneratorService.generateUuid());
            }
            if (p.getRegistrationNumber() == null || p.getRegistrationNumber().isEmpty()) {
                long count = profileRepository.count() + 1;
                p.setRegistrationNumber(idGeneratorService.generateRegistrationNumber(p.getType().name(), count));
            }
        }
        return profileRepository.saveAll(profiles);
    }
}
