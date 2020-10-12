package com.rabo.customer.controller;


import com.rabo.customer.constants.Constants;
import com.rabo.customer.model.CustomerStatement;
import com.rabo.customer.response.CustomerValidationResult;
import com.rabo.customer.service.CustomerStatementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@RestController
@RequestMapping("/statement")
public class CustomerStatementController {

    @Autowired
    public CustomerStatementService customerStatementService;


    /**
     * This method will process the uploaded file
     * @param file input file for processing customer records
     * @return ResponseEntity of CustomerValidationResult which is empty as all validation checks are passed during file processing
     *
     */
    @PostMapping(value = "/processStatement")
    @ApiOperation(value = "Make a POST request to upload the file - Only XML/CSV formats are allowed",produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "The file is valid", response = CustomerValidationResult.class),
            @ApiResponse(code = 200, message = "The file has invalid record(s)", response = CustomerValidationResult.class),
            @ApiResponse(code = 500, message = "The uploaded file format is not supported", response = CustomerValidationResult.class),
            @ApiResponse(code = 500, message = "The uploaded file is empty", response = CustomerValidationResult.class),
            @ApiResponse(code = 400, message = "The uploaded file could not be parsed", response = CustomerValidationResult.class),
    })
    public ResponseEntity<CustomerValidationResult> processStatement(
            @ApiParam(name = "file", value = "Select the file to Upload", required = true)
            @RequestParam("file") MultipartFile file) {
        return generateResult( customerStatementService.processTransactionRecords(file));
    }

    private ResponseEntity<CustomerValidationResult> generateResult(List<CustomerStatement> txnRecord) {
        CustomerValidationResult outcome = new CustomerValidationResult(Constants.SUCCESS_RESULT,txnRecord);
        return ResponseEntity.status( HttpStatus.OK).body(outcome);
    }
}
