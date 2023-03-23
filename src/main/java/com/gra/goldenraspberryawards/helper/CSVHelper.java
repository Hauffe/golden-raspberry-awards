package com.gra.goldenraspberryawards.helper;

import com.gra.goldenraspberryawards.model.Films;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVHelper {
    public static String TYPE = "text/csv";
    public static char DELIMITER = ';';
    public static String WIN = "yes";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Films> csvToFilms(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withDelimiter(DELIMITER).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Films> films = new ArrayList<Films>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Films film = new Films(
                        null,
                        Integer.parseInt(csvRecord.get("year")),
                        csvRecord.get("title"),
                        csvRecord.get("studios"),
                        csvRecord.get("producers"),
                        csvRecord.get("winner").equals(WIN)
                );

                films.add(film);
            }

            return films;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
