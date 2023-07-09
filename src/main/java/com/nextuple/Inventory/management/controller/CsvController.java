package com.nextuple.Inventory.management.controller;

import com.nextuple.Inventory.management.model.Analysis;
import com.nextuple.Inventory.management.repository.AnalysisRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class CsvController {

    @Autowired
    private AnalysisRepository analysisRepository;

    @PostMapping("/")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            // Read the CSV file
            InputStream inputStream = file.getInputStream();
            CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT);

            for (CSVRecord record : csvParser) {
                Analysis entity = new Analysis();
                entity.setOrganizationName(record.get(0));
                entity.setItemName(record.get(1));
                entity.setQuantity( Integer.parseInt(record.get(2)));
                entity.setCostToCompany(Long.parseLong(record.get(3)));
                entity.setProfit(Long.parseLong(record.get(4)));
                entity.setManPower(Integer.parseInt(record.get(5)));
                entity.setTime(Integer.parseInt(record.get(6)));
                entity.setPredictedSale(Double.parseDouble(record.get(7)));
                entity.setOrderingCost(Long.parseLong(record.get(8)));
                entity.setEoq(Float.parseFloat(record.get(9)));
                analysisRepository.save(entity);

            }
            csvParser.close();
            return ResponseEntity.ok("CSV data inserted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and insert CSV data.");
        }
    }
    @GetMapping("/sales")
    public ResponseEntity<List<Analysis>>getAnalysisData(){
        return new ResponseEntity<>(analysisRepository.findAll(),HttpStatus.OK);
    }
    @GetMapping("/sales/{id}")
    public ResponseEntity<List<Analysis>>getAnalysisDataByOrg(@PathVariable("id") String id){
        return new ResponseEntity<>(analysisRepository.findAllByOrganizationName(id),HttpStatus.OK);
    }
}

