package com.val.axon.aem.exceptions;

import java.text.ParseException;

public class EventDateParseException extends RuntimeException {
    public EventDateParseException(ParseException e) {
        super("Date string didn't match the pattern [yyyy-MM-dd'T'HH:mm:ss.SSS'Z'].", e);
    }
}
