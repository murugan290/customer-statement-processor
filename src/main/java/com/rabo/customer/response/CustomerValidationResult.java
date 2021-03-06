package com.rabo.customer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rabo.customer.model.CustomerStatement;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CustomerValidationResult {

    private String message;
    private List<CustomerStatement> errorRecords;

    public CustomerValidationResult(){

    }

    /**
     *
     * @param message
     * @param errorRecords
     */
    public CustomerValidationResult(String message, List<CustomerStatement> errorRecords) {
        this.message = message;
        this.errorRecords = errorRecords;
    }
}
