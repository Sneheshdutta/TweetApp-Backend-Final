package com.tweetapp.exception;

import com.tweetapp.model.utilityModel.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class TweetAppExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TweetAppException.class)
    public ResponseEntity<ApiResponse> exceptionTweetApp(TweetAppException exception) {
        return ResponseEntity.internalServerError().body(ApiResponse.builder()
                        .status(500).message(exception.getLocalizedMessage())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Invalid Input", errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.ok(ApiResponse.builder().status(status.value()).message(ex.getMessage()).build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.ok(ApiResponse.builder().status(status.value()).message("Required request body is missing").build());
    }
}
