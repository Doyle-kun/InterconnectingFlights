package org.gzyrek.interconnecting.rest.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
@Component
public class InterconnectingFligthsConverterImpl {

    public LocalDateTime convertISODateTimeToLocal(String dateTime) {
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(dateTime, format);
        return date;
    }
}
