package org.gzyrek.interconnecting.rest.service;

import java.time.LocalDateTime;

public interface InterconnectingFlightsConverter {

    LocalDateTime convertISODateTimeToLocal(String dateTime);

    String convertHourToISODate(String hour, String parameterDate);

}