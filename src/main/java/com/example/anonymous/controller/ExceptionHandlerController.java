package com.example.anonymous.controller;

import javax.servlet.http.HttpServletRequest;

import com.example.anonymous.exception.InvalidInputException;
import com.example.anonymous.exception.NoAuthException;
import com.example.anonymous.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerErrorException;


@ControllerAdvice()
public class ExceptionHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseBody
    public ResponseEntity<?> InvalidInputException(HttpServletRequest request, InvalidInputException ex)
    {
        LOGGER.error("Exception type : " + ex.getClass().getName());
        LOGGER.error("Exception message : " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MailException.class)
    @ResponseBody
    public ResponseEntity<?> MailSendException(HttpServletRequest request, MailException ex)
    {
        LOGGER.error("Exception type : " + ex.getClass().getName());
        LOGGER.error("Exception message : " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<String>("메일 인증 중 오류 발생.",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ServerException.class)
    @ResponseBody
    public ResponseEntity<?> ServerException(HttpServletRequest request, ServerException ex)
    {
        LOGGER.error("Exception type : " + ex.getClass().getName());
        LOGGER.error("Exception message : " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = NoAuthException.class)
    @ResponseBody
    public ResponseEntity<?> NoAuthExceptoin(HttpServletRequest request, NoAuthException ex)
    {
        LOGGER.error("Exception type : " + ex.getClass().getName());
        LOGGER.error("Exception message : " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.FORBIDDEN);
    }

}

