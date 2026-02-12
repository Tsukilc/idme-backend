package com.tsukilc.idme.exception;

/**
 * iDME 业务异常
 */
public class IdmeException extends RuntimeException {
    
    public IdmeException(String message) {
        super(message);
    }
    
    public IdmeException(String message, Throwable cause) {
        super(message, cause);
    }
}
