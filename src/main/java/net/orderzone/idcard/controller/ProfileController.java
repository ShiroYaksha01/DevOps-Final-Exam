package net.orderzone.idcard.controller;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.service.CodeService;
import net.orderzone.idcard.service.PdfExportService;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.StorageService;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final StorageService storageService;
    private final CodeService codeService;
    private final PdfExportService pdfExportService;
    private final TemplateService templateService;

    public ProfileController(ProfileService profileService, StorageService storageService,
                             CodeService codeService, PdfExportService pdfExportService,
                             TemplateService templateService) {
        this.profileService = profileService;
        this.storageService = storageService;
        this.codeService = codeService;
        this.pdfExportService = pdfExportService;
        this.templateService = templateService;
    }

    @GetMapping
    public String listProfiles(Model model) {
        model.addAttribute("profiles", profileService.findAll());
        return "profiles/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("templates", templateService.findAll());
        model.addAttribute("types", ProfileType.values());
        return "profiles/form";
    }

    @PostMapping("/create")
    public String saveProfile(@Valid @ModelAttribute Profile profile, BindingResult result, 
                              @RequestParam("photo") MultipartFile photo, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("templates", templateService.findAll());
            model.addAttribute("types", ProfileType.values());
            return "profiles/form";
        }
        if (!photo.isEmpty()) {
            String filename = storageService.store(photo);
            profile.setPhotoFileName(filename);
            profile.setPhotoContentType(photo.getContentType());
        }
        profileService.save(profile);
        return "redirect:/profiles";
    }

    @GetMapping("/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile Id:" + id));
            
        String qrCode = codeService.generateQRCodeBase64("https://verify.orderzone.net/" + profile.getUuid(), 200, 200);
        String barcode = codeService.generateBarcodeBase64(profile.getRegistrationNumber(), profile.getBarcodeType(), 300, 100);
        
        model.addAttribute("profile", profile);
        model.addAttribute("qrCodeBase64", qrCode);
        model.addAttribute("barcodeBase64", barcode);
        return "profiles/view";
    }

    @GetMapping("/{id}/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        Profile profile = profileService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid profile Id:" + id));
        
        byte[] pdfBytes = pdfExportService.exportProfileToPdf(profile);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "idcard_" + profile.getRegistrationNumber() + ".pdf");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
    @GetMapping("/batch-export-pdf")
    public ResponseEntity<byte[]> batchExportPdf() {
        List<Profile> profiles = profileService.findAll();
        byte[] pdfBytes = pdfExportService.exportBatchToPdf(profiles);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "batch_idcards.pdf");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
