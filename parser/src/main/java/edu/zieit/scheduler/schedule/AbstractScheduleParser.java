package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.api.schedule.ScheduleParser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractScheduleParser implements ScheduleParser {

    protected Workbook loadWorkbook(ScheduleInfo info) throws ScheduleParseException {
        String file = info.getUrl().toString();
        Workbook workbook;

        try (InputStream in = info.getUrl().openStream()) {
            if (file.endsWith(".xls")) {
                workbook = new HSSFWorkbook(in);
            } else if (file.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(in);
            } else {
                throw new ScheduleParseException("Schedule file doesn't match any of Excel file format");
            }
        } catch (IOException e) {
            throw new ScheduleParseException(e);
        }

        return workbook;
    }

}
