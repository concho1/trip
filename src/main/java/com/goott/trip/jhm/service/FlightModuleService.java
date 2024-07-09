package com.goott.trip.jhm.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.goott.trip.jhm.mapper.FlightMapper;
import com.goott.trip.jhm.model.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightModuleService {

    @Autowired
    private AmadeusSearchFlightDAO dao;

    @Autowired
    private FlightMapper mapper;

    public FlightOfferSearch[] searchFlight(Flight flight) throws ResponseException {
        return dao.searchFlight(flight);
    }

    public void uploadAirport(MultipartFile file) throws IOException {
        List<Airport> list = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀 파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if(extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }else if(extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++){
            Row row = worksheet.getRow(i);
            Airport dto = new Airport();

            dto.setEngName(row.getCell(0).getStringCellValue());
            dto.setKorName(row.getCell(1).getStringCellValue());
            if(row.getCell(2) != null) {
                dto.setIata(row.getCell(2).getStringCellValue());
            }else {
                dto.setIata("");
            }

            if(row.getCell(3) != null) {
                dto.setIcao(row.getCell(3).getStringCellValue());
            }else {
                dto.setIcao("");
            }

            dto.setRegion(row.getCell(4).getStringCellValue());
            dto.setEngCountry(row.getCell(5).getStringCellValue());
            dto.setKorCountry(row.getCell(6).getStringCellValue());
            dto.setEngCity(row.getCell(7).getStringCellValue());
            dto.setKorCity(row.getCell(8).getStringCellValue());

            list.add(dto);
        }
        this.mapper.uploadAirport(list);
    }

    public void uploadAirline(MultipartFile file) throws IOException {
        List<AirLine> list = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀 파일만 업로드해 주세요.");
        }

        Workbook workbook = null;

        if(extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        }else if(extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            AirLine dto = new AirLine();

            dto.setEngName(row.getCell(0).getStringCellValue());
            dto.setKorName(row.getCell(1).getStringCellValue());
            if(row.getCell(2) != null) {
                dto.setIata(row.getCell(2).getStringCellValue());
            }else {
                dto.setIata("");
            }

            if(row.getCell(3) != null) {
                dto.setIcao(row.getCell(3).getStringCellValue());
            }else {
                dto.setIcao("");
            }

            if(row.getCell(4) != null) {
                dto.setStatus(row.getCell(4).getStringCellValue());
            }else {
                dto.setStatus("");
            }

            if(row.getCell(5) != null) {
                dto.setModel(row.getCell(5).getStringCellValue());
            }else {
                dto.setModel("");
            }

            if(row.getCell(6) != null) {
                dto.setCountry(row.getCell(6).getStringCellValue());
            }else {
                dto.setCountry("");
            }

            if(row.getCell(7) != null) {
                dto.setRegion(row.getCell(7).getStringCellValue());
            }else {
                dto.setRegion("");
            }

            list.add(dto);
        }
        this.mapper.uploadAirline(list);
    }

}
