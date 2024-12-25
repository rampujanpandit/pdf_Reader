package com.pdf.reader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pdf.reader.entity.PdfSection;
import com.pdf.reader.services.PdfReaderService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfReaderService pdfReaderService;

    @PostMapping("/upload")
    public ResponseEntity<List<PdfSection>> uploadPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<PdfSection> sections = pdfReaderService.savePdfSections(file);
            return ResponseEntity.ok(sections);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}

