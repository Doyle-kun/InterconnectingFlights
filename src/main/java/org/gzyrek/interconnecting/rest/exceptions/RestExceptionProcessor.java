package org.gzyrek.interconnecting.rest.exceptions;


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class RestExceptionProcessor{

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> requestHandlingNoHandlerFound(HttpServletRequest req, NoHandlerFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = "NOT FOUND";

        String errorURL = req.getRequestURL().toString();

       // ErrorInfo errorInfo = new ErrorInfo(errorURL, errorMessage);
        return new ResponseEntity<String>("errorMessage", HttpStatus.OK);
    }

}
