package com.tugasktp.ktp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateNomorKtpException extends RuntimeException {

    public DuplicateNomorKtpException(String message) {
        super(message);
    }
}
