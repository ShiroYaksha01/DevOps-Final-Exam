package net.orderzone.idcard.controller;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "templates/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("template", new Template());
        return "templates/form";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Template template) {
        templateService.save(template);
        return "redirect:/templates";
    }
}
