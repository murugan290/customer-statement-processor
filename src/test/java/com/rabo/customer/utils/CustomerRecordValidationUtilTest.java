package com.rabo.customer.utils;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.constants.TestConstants;
import com.rabo.customer.exception.DuplicateRefAndBalanceMismatchException;
import com.rabo.customer.exception.DuplicateReferenceException;
import com.rabo.customer.exception.IncorrectEndBalanceException;
import com.rabo.customer.exception.InvalidFileException;
import com.rabo.customer.model.CustomerStatement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(SpringRunner.class)
public class CustomerRecordValidationUtilTest {


    @Test(expected = InvalidFileException.class)
    public void validateForEmptyInputXmlFileTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_empty.xml").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_empty.xml", TestConstants.XML_CONTENT_TYPE_2, is);
        is.close();
        CustomerRecordValidationUtil.validateInputFile(multipartFile);
    }

    @Test(expected = InvalidFileException.class)
    public void validateForEmptyInputCsvFileTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/csv/records_empty.csv").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("csv", "records_empty.csv", TestConstants.CSV_CONTENT_TYPE_2, is);
        is.close();
        CustomerRecordValidationUtil.validateInputFile(multipartFile);
    }

    @Test(expected = DuplicateReferenceException.class)
    public void processErrorRecordsWithDuplicateRef(){
        List<CustomerStatement> reports = new ArrayList<>();
        CustomerStatement report1 = new CustomerStatement();
        report1.setTxnReference("112806");
        List<String> failureReason = new ArrayList<>();
        failureReason.add(Constants.DUPLICATE_REFERENCE);
        report1.setFailureReason(failureReason);
        reports.add(report1);
        CustomerStatement report2 = new CustomerStatement();
        report2.setTxnReference("112806");
        report2.setFailureReason(failureReason);
        reports.add(report2);
        CustomerRecordValidationUtil.processErrorRecords(reports);
    }

    @Test(expected = IncorrectEndBalanceException.class)
    public void processErrorRecordsWithIncorrectBalance(){
        List<CustomerStatement> reports = new ArrayList<>();
        CustomerStatement report1 = new CustomerStatement();
        List<String> failureReason = new ArrayList<>();
        failureReason.add(Constants.BALANCE_MISMATCHED);
        report1.setFailureReason(failureReason);
        reports.add(report1);
        CustomerStatement report2 = new CustomerStatement();
        report2.setFailureReason(failureReason);
        reports.add(report2);
        CustomerRecordValidationUtil.processErrorRecords(reports);
    }

    @Test(expected = DuplicateRefAndBalanceMismatchException.class)
    public void processErrorRecordsWithIncorrectBalanceAndDuplicateRef(){
        List<String> failureReasonBalMis = new ArrayList<>();
        failureReasonBalMis.add(Constants.BALANCE_MISMATCHED);
        List<String> failureReasonDupRef = new ArrayList<>();
        failureReasonDupRef.add(Constants.DUPLICATE_REFERENCE);
        List<CustomerStatement> reports = new ArrayList<>();
        CustomerStatement report1 = new CustomerStatement();
        report1.setFailureReason(failureReasonBalMis);
        reports.add(report1);
        CustomerStatement report2 = new CustomerStatement();
        report2.setFailureReason(failureReasonDupRef);
        reports.add(report2);
        CustomerRecordValidationUtil.processErrorRecords(reports);
    }

    @Test(expected = DuplicateRefAndBalanceMismatchException.class)
    public void processErrorRecordsWithIncorrectBalanceAndDuplicateRefInSingleCustomerRecord(){
        List<String> failureReason = new ArrayList<>();
        failureReason.add(Constants.BALANCE_MISMATCHED);
        failureReason.add(Constants.DUPLICATE_REFERENCE);
        List<CustomerStatement> reports = new ArrayList<>();
        CustomerStatement report1 = new CustomerStatement();
        report1.setFailureReason(failureReason);
        reports.add(report1);
        CustomerRecordValidationUtil.processErrorRecords(reports);
    }

}
