package org.gzyrek.interconnecting.rest.service.implementation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.gzyrek.interconnecting.rest.service.InterconnectingFlightsConverter;
import org.springframework.stereotype.Component;
@Component
public class InterconnectingFligthsConverterImpl implements InterconnectingFlightsConverter {

    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.service.InterconnectingFlightsConverter#convertISODateTimeToLocal(java.lang.String)
     */
    @Override
    public LocalDateTime convertISODateTimeToLocal(String dateTime) {
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(dateTime, format);
        return date;
    }
    /* (non-Javadoc)
     * @see org.gzyrek.interconnecting.rest.service.InterconnectingFlightsConverter#convertHourToISODate(java.lang.String, java.lang.String)
     */
    @Override
    public String convertHourToISODate(String hour, String parameterDate) {
        String flightDate = parameterDate.substring(0, parameterDate.length()-5) + hour;
        return flightDate;
    };
}
