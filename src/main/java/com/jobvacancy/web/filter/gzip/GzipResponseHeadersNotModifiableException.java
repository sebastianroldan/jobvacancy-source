package com.jobvacancy.web.filter.gzip;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class GzipResponseHeadersNotModifiableException extends ServletException {

    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
