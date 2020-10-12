package com.rabo.customer.controller;

import com.rabo.customer.constants.TestConstants;
import com.rabo.customer.exception.UnsupportedFileFormatException;
import com.rabo.customer.model.CustomerStatement;
import com.rabo.customer.response.CustomerValidationResult;
import com.rabo.customer.service.CustomerStatementService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class CustomerStatementControllerTest {

    @InjectMocks
    CustomerStatementController customerStatementController;

    @Mock
    CustomerStatementService customerStatementService;

    @Test
    public void xmlFileWithGivenData() throws IOException {
        String message = "SUCCESS, All Customer records are valid!!!";
        File xmlFile = new File(this.getClass().getResource("/xml/records_all_valid.xml").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_all_valid.xml", TestConstants.XML_CONTENT_TYPE_2, is);
        is.close();
        List<CustomerStatement> reports = new ArrayList<>();
        Mockito.when(customerStatementService.processTransactionRecords(multipartFile)).thenReturn(reports);
        ResponseEntity<CustomerValidationResult> result = customerStatementController.processStatement(multipartFile);
        Assert.assertNotEquals(null, result);
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals(message, result.getBody().getMessage());
        verify(customerStatementService, times(1)).processTransactionRecords(multipartFile);
    }

    @Test
    public void csvFileWithGivenData() throws IOException {
        String message = "SUCCESS, All Customer records are valid!!!";
        File xmlFile = new File(this.getClass().getResource("/csv/records_all_valid.csv").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("csv", "records_all_valid.csv", TestConstants.CSV_CONTENT_TYPE_2, is);
        is.close();
        List<CustomerStatement> reports = new ArrayList<>();
        Mockito.when(customerStatementService.processTransactionRecords(multipartFile)).thenReturn(reports);
        ResponseEntity<CustomerValidationResult> result = customerStatementController.processStatement(multipartFile);
        Assert.assertNotEquals(null, result);
        Assert.assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertEquals(message, result.getBody().getMessage());
        verify(customerStatementService, times(1)).processTransactionRecords(multipartFile);
    }

    @Test(expected = UnsupportedFileFormatException.class)
    public void unsupportedFileFormatWithTextFile() throws IOException {
        File textFile = new File(this.getClass().getResource("/records.txt").getFile());
        InputStream is = new FileInputStream(textFile);
        MockMultipartFile multipartFile = new MockMultipartFile("text", "records.txt", "text/plain", is);
        is.close();
        Mockito.when(customerStatementService.processTransactionRecords(multipartFile)).thenThrow(new UnsupportedFileFormatException(500,"The given file format txt is not supported"));
        customerStatementController.processStatement(multipartFile);
        verify(customerStatementService, times(1)).processTransactionRecords(multipartFile);
    }

}
