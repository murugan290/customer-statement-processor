package com.rabo.customer.service;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.exception.UnsupportedFileFormatException;
import com.rabo.customer.model.CustomerStatement;
import com.rabo.customer.utils.CustomerRecordValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author - Murugan Rajendran
 *
 */

@Service
public class CustomerStatementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerStatementService.class);

    @Autowired
    public XMLProcessorService xmlProcessorService;
    @Autowired
    public CSVProcessorService csvProcessorService;


    /**
     * This method will parse,process and validate the uploaded file content
     * @param file input xml/csv file
     * @return List of CustomerStatement
     *
     */
    public List<CustomerStatement> processTransactionRecords(MultipartFile file){
        LOGGER.info("FileValidationService -> fileName={} and fileType={}" , file.getOriginalFilename(), file.getContentType());
        CustomerRecordValidationUtil.validateInputFile(file);
        return applyBusinessRules(processInputFile(file));
    }

    private List<CustomerStatement> processInputFile(MultipartFile file){
        String fileExtension = CustomerRecordValidationUtil.getFileExtension(file);
        List<CustomerStatement> recordDetails;
        if(fileExtension.equals(Constants.XML_FILE_TYPE)){
            recordDetails = xmlProcessorService.processXmlData(file);
            return recordDetails;
        }else if(fileExtension.equals(Constants.CSV_FILE_TYPE)) {
            recordDetails = csvProcessorService.processCsvData(file);
            return recordDetails;
        }else{
            LOGGER.error("CustomerStatementService :: processInputFile() ERROR -> The given file format {} is not supported",file.getContentType() );
            throw new UnsupportedFileFormatException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    String.join(" ", "The given file format",file.getContentType(),"is not supported"));
        }
    }

    private List<CustomerStatement> applyBusinessRules(List<CustomerStatement> customerStatements){
        Set<String> referenceNumbers = findDuplicateReferenceData(customerStatements);
        List<CustomerStatement> errorRecords = validateForDuplicateReference(customerStatements, referenceNumbers );
        List<CustomerStatement> incorrectBalanceRecords = validateForEndBalanceMismatch(customerStatements, referenceNumbers);
        errorRecords.addAll( incorrectBalanceRecords );
        if(!errorRecords.isEmpty()){
            CustomerRecordValidationUtil.processErrorRecords(errorRecords);
        }
        return errorRecords;
    }




    private List<CustomerStatement> validateForEndBalanceMismatch(List<CustomerStatement> customerStatements, Set<String> referenceNumbers){
        List<CustomerStatement> incorrectBalanceRecords = customerStatements.stream()
                .filter( txn -> !referenceNumbers.contains( txn.getTxnReference()) )
                .filter(CustomerRecordValidationUtil::validateEndBalance)
                .collect( Collectors.toList());
        incorrectBalanceRecords.stream().forEach( txn -> txn.getFailureReason().add(Constants.BALANCE_MISMATCHED) );
        return incorrectBalanceRecords;
    }


    private List<CustomerStatement> validateForDuplicateReference(List<CustomerStatement> customerStatements, Set<String> referenceNumbers){
        List<CustomerStatement> invalidRecords = customerStatements.stream()
                .filter( txn -> referenceNumbers.contains( txn.getTxnReference() ) )
                .collect( Collectors.toList() );

        invalidRecords.stream().forEach( txn -> {
            txn.getFailureReason().add( Constants.DUPLICATE_REFERENCE );
            if (CustomerRecordValidationUtil.validateEndBalance( txn )) {
                txn.getFailureReason().add( Constants.BALANCE_MISMATCHED );
            }
        } );
        return invalidRecords;
    }

    private Set<String> findDuplicateReferenceData(List<CustomerStatement> customerStatements){
        Set<String> customerTxnReference = new HashSet<>();
        return customerStatements.stream().map( CustomerStatement::getTxnReference)
                .filter( txnData -> !customerTxnReference.add( txnData ) ).collect( Collectors.toSet() );
    }

}
