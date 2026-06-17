package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<Template> findAll() {
        return templateRepository.findAll();
    }

    public Optional<Template> findById(Long id) {
        return templateRepository.findById(id);
    }
    
    public Template save(Template template) {
        return templateRepository.save(template);
    }
}
