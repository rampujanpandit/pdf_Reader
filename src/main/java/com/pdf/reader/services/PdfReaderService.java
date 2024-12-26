package com.pdf.reader.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pdf.reader.entity.PdfSection;
import com.pdf.reader.repo.PdfSectionRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfReaderService {

    @Autowired
    private PdfSectionRepository repository;

    public List<PdfSection> savePdfSections(MultipartFile file) throws IOException {
        List<PdfSection> sections = extractPdfSections(file);

        return repository.saveAll(sections);
    }

    private List<PdfSection> extractPdfSections(MultipartFile file) throws IOException {
        List<PdfSection> sections = new ArrayList<>();

        try (InputStream is = file.getInputStream(); PDDocument document = PDDocument.load(is)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String[] lines = text.split("\\r?\\n");
            String currentSection = "Introduction";

            StringBuilder content = new StringBuilder();
            for (String line : lines) {
                if (line.matches("^[A-Z ]{3,}$")) { // Assuming sections are in uppercase
                    if (content.length() > 0) {
                        PdfSection section = new PdfSection();
                        section.setSectionName(currentSection);
                        section.setSectionContent(content.toString().trim());
                        sections.add(section);
                    }

                    currentSection = line;
                    content.setLength(0);
                } else {
                    content.append(line).append("\n");
                }
            }

            if (content.length() > 0) {
                PdfSection section = new PdfSection();
                section.setSectionName(currentSection);
                section.setSectionContent(content.toString().trim());
                sections.add(section);
            }
        }

        return sections;
    }
}

