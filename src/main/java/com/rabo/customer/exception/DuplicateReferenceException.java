package com.rabo.customer.exception;

import com.rabo.customer.model.CustomerStatement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * @author - Murugan Rajendran
 *
 */

@Getter
@Setter
public class DuplicateReferenceException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private final List<CustomerStatement> failedRecords;
    private final  int statusCode;

    public DuplicateReferenceException(int statusCode, List<CustomerStatement> failedRecords, String message) {
        super(message);
        this.failedRecords = failedRecords;
        this.statusCode = statusCode;
    }

}
