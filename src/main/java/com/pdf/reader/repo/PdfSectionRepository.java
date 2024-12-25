package com.pdf.reader.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pdf.reader.entity.PdfSection;

@Repository
public interface PdfSectionRepository extends JpaRepository<PdfSection, Long> {
}

