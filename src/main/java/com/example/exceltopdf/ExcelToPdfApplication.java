package com.example.exceltopdf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@SpringBootApplication
public class ExcelToPdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelToPdfApplication.class, args);
    }

}
