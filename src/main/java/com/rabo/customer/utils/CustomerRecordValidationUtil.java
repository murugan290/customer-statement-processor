package com.rabo.customer.utils;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.exception.*;
import com.rabo.customer.model.CustomerStatement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

public class CustomerRecordValidationUtil {

    private CustomerRecordValidationUtil(){}

    /**
     * This method will validate the file type and content
     * @param file input xml/csv file
     * @throws InvalidFileException if file is empty
     * @throws FileParsingException if any file content cannot be parsed
     */
    public static void validateInputFile(MultipartFile file) {
        if (file.getContentType().equalsIgnoreCase(Constants.CSV_CONTENT_TYPE_1) || file.getContentType().equalsIgnoreCase(Constants.CSV_CONTENT_TYPE_2)){
            checkForEmptyCsvFile(file);
        }else if(file.isEmpty()){
            throw new InvalidFileException(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.FILE_CANT_BE_EMPTY );
        }
    }

    private static void checkForEmptyCsvFile(MultipartFile file){
        try{
            InputStream is = file.getInputStream();
            CSVParser csvParser = CSVParser.parse(is, StandardCharsets.US_ASCII,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            if(csvParser.getRecords().isEmpty()){
                throw new InvalidFileException(HttpStatus.INTERNAL_SERVER_ERROR.value(), Constants.FILE_CANT_BE_EMPTY );
            }
        }catch(InvalidFileException ife){
            throw ife;
        }catch(Exception e){
            throw new FileParsingException(HttpStatus.BAD_REQUEST.value() , String.join(" ", "Parsing file -",file.getOriginalFilename(),"failed due to invalid content in file"));
        }
    }

    /**
     * This method will get the file extension
     * @param file input xml/csv file
     */
    public static String getFileExtension(MultipartFile file) {
        String fileExtension = file.getContentType();
        if (file.getContentType().equalsIgnoreCase(Constants.XML_CONTENT_TYPE_1) || file.getContentType().equalsIgnoreCase(Constants.XML_CONTENT_TYPE_2)){
            fileExtension = Constants.XML_FILE_TYPE;
        }else if (file.getContentType().equalsIgnoreCase(Constants.CSV_CONTENT_TYPE_1) || file.getContentType().equalsIgnoreCase(Constants.CSV_CONTENT_TYPE_2)){
            fileExtension = Constants.CSV_FILE_TYPE;
        }
        return fileExtension;
    }

    /**
     * This method will process the invalid customer records
     * @param errorRecords List of CustomerStatement
     * @throws DuplicateRefAndBalanceMismatchException if customer records has both duplicate reference & incorrect balance
     * @throws DuplicateReferenceException if customer records contains duplicate reference
     * @throws IncorrectEndBalanceException if customer records contains incorrect end balance
     */
    public static void processErrorRecords(List<CustomerStatement> errorRecords){
        int failedRecords = errorRecords.size();

        long duplicateReferenceAndIncorrectBalance = errorRecords.stream().filter( txn -> (txn.getFailureReason().size() > 1) ).count();
        long duplicateTransactionReference = errorRecords.stream().filter( txn -> txn.getFailureReason().get( 0 ).startsWith( Constants.DUPLICATE_CHECK ) ).count();
        long balanceMismatchRecords = errorRecords.stream().filter( txn -> txn.getFailureReason().get( 0 ).startsWith( Constants.BALANCE_CHECK ) ).count();

        if (duplicateReferenceAndIncorrectBalance > 0) {
            throw new DuplicateRefAndBalanceMismatchException( HttpStatus.OK.value(), errorRecords, Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE );

        } else if (duplicateTransactionReference > 0 && duplicateTransactionReference == failedRecords) {
            throw new DuplicateReferenceException( HttpStatus.OK.value(), errorRecords, Constants.DUPLICATE_REFERENCE);

        } else if (balanceMismatchRecords > 0 && balanceMismatchRecords == failedRecords) {
            throw new IncorrectEndBalanceException( HttpStatus.OK.value(), errorRecords, Constants.INCORRECT_END_BALANCE );
        } else {
            throw new DuplicateRefAndBalanceMismatchException( HttpStatus.OK.value(), errorRecords, Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE );
        }
    }

    /**
     * This method will validate end balance of a customer record
     * @param  txnRecord CustomerStatement object
     * @return true - end balance mismatch for the customer record
     * @return false - end balance match for the customer record
     */
    public static boolean validateEndBalance(CustomerStatement txnRecord){
        boolean response = false;
        BigDecimal startBalance = new BigDecimal(txnRecord.getStartBalance()).setScale(2, RoundingMode.DOWN);
        BigDecimal mutation = new BigDecimal(txnRecord.getMutation()).setScale(2,RoundingMode.DOWN);
        BigDecimal endBalance = new BigDecimal(txnRecord.getEndBalance()).setScale(2,RoundingMode.DOWN);
        if((startBalance.add(mutation)).compareTo(endBalance)!=0){
            response = true;
        }
        return response;
    }

}
