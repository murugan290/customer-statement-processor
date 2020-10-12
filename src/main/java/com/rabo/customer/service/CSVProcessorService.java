package com.rabo.customer.service;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.exception.FileParsingException;
import com.rabo.customer.model.CustomerStatement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - Murugan Rajendran
 *
 */

@Service
public class CSVProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVProcessorService.class);


    /**
     * This method will parse the csv file content and create list of customer records
     * @param file
     * @return list of CustomerStatement
     */
    public List<CustomerStatement> processCsvData(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        List<CustomerStatement> customerDetails = null;
        try (InputStream is = file.getInputStream()) {
            CSVParser csvParser = CSVParser.parse(is, StandardCharsets.US_ASCII, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            customerDetails = csvParser.getRecords().stream().parallel().map(csvRecord -> {
                CustomerStatement customerRecord = new CustomerStatement();
                customerRecord.setTxnReference(csvRecord.get(Constants.REFERENCE));
                customerRecord.setAccountNumber(csvRecord.get(Constants.ACCOUNT_NUMBER));
                customerRecord.setDescription(csvRecord.get(Constants.DESCRIPTION));
                customerRecord.setStartBalance(csvRecord.get(Constants.START_BALANCE));
                customerRecord.setMutation(csvRecord.get(Constants.MUTATION));
                customerRecord.setEndBalance(csvRecord.get(Constants.END_BALANCE));
                return customerRecord;
            }).collect(Collectors.toList());
        }catch(Exception e){
            LOGGER.error("CSVProcessorService :: processCsvData() ERROR -> Parsing file with fileName = {} failed. And the reason is : {}", fileName , e);
            throw new FileParsingException(HttpStatus.BAD_REQUEST.value() , String.join(" ", "Parsing file -",fileName,"failed due to invalid content in file"));
        }
        return customerDetails;
    }

}
