package com.example.exceltopdf.controller;

import com.example.exceltopdf.service.MergeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Controller
public class MainController {

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @PostMapping("process")
    public ResponseEntity<InputStreamResource> processFile(@RequestParam(value = "inputFiles") MultipartFile[] inputFiles) throws Exception {
        MediaType contentType = MediaType.APPLICATION_PDF;

        InputStream inputStream = new FileInputStream(MergeTool.mergePDF(inputFiles));

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(inputStream));
    }

}
