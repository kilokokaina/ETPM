package com.example.exceltopdf.service;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.words.EditingLanguage;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class MergeTool {
    public static String PATH = "/src/main/resources/download/%s";

    public static File mergePDF(MultipartFile[] uploadedFiles) throws Exception {
        Date creationDate = new Date();
        File currentDirectory = new File(".", String.format(PATH, creationDate));

        if ((currentDirectory.mkdir())) log.info(String.format("Dir was created: %s", creationDate));
        else log.info("Dir wasn't created");

        for (MultipartFile uploadedFile : uploadedFiles) {
            InputStream inputStream = uploadedFile.getInputStream();
            try(FileOutputStream fileOutputStream = new FileOutputStream(
                    currentDirectory.getAbsolutePath() + "/" + uploadedFile.getOriginalFilename()
            )) {

                int chars;
                while ((chars = inputStream.read()) != -1) fileOutputStream.write(chars);
                fileOutputStream.flush();

            } catch (IOException ignored) {}
        }

        for (File uploadedFile : Objects.requireNonNull(currentDirectory.listFiles())) {
            String[] fileDetails = uploadedFile.getName().split("\\.");
            String uploadedFileExtension = fileDetails[fileDetails.length - 1];

            if (uploadedFileExtension.equals("xls") || uploadedFileExtension.equals("xlsx")
                    || uploadedFileExtension.equals("xlsm")) {
                Workbook excelToPDF = new Workbook(new FileInputStream(uploadedFile.getAbsolutePath()));
                PdfSaveOptions pdfOptions = new PdfSaveOptions();

                pdfOptions.setDefaultEditLanguage(EditingLanguage.RUSSIAN);

                excelToPDF.save(new FileOutputStream(
                        uploadedFile.getParent() + "/excel_to.pdf"),
                        SaveFormat.PDF
                );
            }
        }

        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        for(File file : Objects.requireNonNull(currentDirectory.listFiles())) {
            if (file.getName().split("\\.")[1].equals("pdf")) {
                mergerUtility.addSource(file);
            }
        }

        FileOutputStream downloadFile = new FileOutputStream(currentDirectory.getAbsolutePath() + "/doc.pdf");

        mergerUtility.setDestinationStream(downloadFile);
        mergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

        return new File(currentDirectory.getAbsolutePath() + "/doc.pdf");
    }
}
