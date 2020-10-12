package com.rabo.customer.service;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.constants.TestConstants;
import com.rabo.customer.data.CustomerDataProvider;
import com.rabo.customer.exception.UnsupportedFileFormatException;
import com.rabo.customer.model.CustomerStatement;
import com.rabo.customer.utils.CustomerRecordValidationUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CustomerRecordValidationUtil.class})
public class CustomerStatementServiceTest {

    private static final String METHOD_NAME = "applyBusinessRules";
    private static final String PROCESS_FILE_METHOD_NAME ="processInputFile";

    @InjectMocks
    CustomerStatementService customerStatementService;

    @Mock
    XMLProcessorService xmlProcessorService;

    @Mock
    CSVProcessorService csvProcessorService;

    @Test
    public void duplicateReferenceTest() throws Exception {
        List<CustomerStatement> customerDetails = CustomerDataProvider.getXmLData();
        PowerMockito.mockStatic(CustomerRecordValidationUtil.class);
        PowerMockito.doNothing().when(CustomerRecordValidationUtil.class);
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        List<CustomerStatement> duplicateRefRecords = Whitebox.invokeMethod(customerStatementService,METHOD_NAME,customerDetails);
        PowerMockito.verifyStatic(CustomerRecordValidationUtil.class, Mockito.times(1));
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        assertNotEquals(null, duplicateRefRecords);
        assertEquals(2, duplicateRefRecords.size());
        assertEquals(TestConstants.REFERENCE_NUMBER,duplicateRefRecords.get(0).getTxnReference());
        assertEquals(TestConstants.REFERENCE_NUMBER,duplicateRefRecords.get(1).getTxnReference());

    }

    @Test
    public void incorrectEndBalanceTest() throws Exception{
        List<CustomerStatement> records =CustomerDataProvider.getCsvData();
        PowerMockito.spy(CustomerRecordValidationUtil.class);
        PowerMockito.doNothing().when(CustomerRecordValidationUtil.class);
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        List<CustomerStatement> errorRecords = Whitebox.invokeMethod(customerStatementService,METHOD_NAME,records);
        PowerMockito.verifyStatic(CustomerRecordValidationUtil.class,Mockito.times(1));
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        assertNotEquals(null, errorRecords);
        assertEquals("130499",errorRecords.get(1).getTxnReference());
        assertEquals("135607",errorRecords.get(2).getTxnReference());
    }

    @Test
    public void duplicateReferenceAndIncorrectBalanceInSameCustomerRecordTest() throws Exception{
        List<CustomerStatement> records =CustomerDataProvider.getXmLData();
        PowerMockito.spy(CustomerRecordValidationUtil.class);
        PowerMockito.doNothing().when(CustomerRecordValidationUtil.class);
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        List<CustomerStatement> errorRecords = Whitebox.invokeMethod(customerStatementService,METHOD_NAME,records);
        PowerMockito.verifyStatic(CustomerRecordValidationUtil.class,Mockito.times(1));
        CustomerRecordValidationUtil.processErrorRecords(any(List.class));
        Assert.assertNotEquals(null, errorRecords);
        assertEquals(3, errorRecords.size());
        assertEquals(TestConstants.REFERENCE_NUMBER,errorRecords.get(0).getTxnReference());
        assertEquals(TestConstants.REFERENCE_NUMBER,errorRecords.get(1).getTxnReference());
        assertEquals(Constants.BALANCE_MISMATCHED, errorRecords.get(2).getFailureReason().get(0));
    }

    @Test
    public void processInputFileWithXmlTest() throws Exception {
        File xmlFile = new File(this.getClass().getResource("/xml/records_all_valid.xml").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_all_valid.xml", TestConstants.XML_CONTENT_TYPE_2, is);
        is.close();
        List<CustomerStatement> recordDetails = new ArrayList<>();
        PowerMockito.spy(CustomerRecordValidationUtil.class);
        PowerMockito.when(CustomerRecordValidationUtil.getFileExtension(multipartFile)).thenReturn("xml");
        Whitebox.invokeMethod(customerStatementService,PROCESS_FILE_METHOD_NAME,multipartFile);
        Mockito.when(xmlProcessorService.processXmlData(multipartFile)).thenReturn(recordDetails);
        verify(xmlProcessorService, times(1)).processXmlData(multipartFile);
        PowerMockito.verifyStatic(CustomerRecordValidationUtil.class,Mockito.times(1));
        CustomerRecordValidationUtil.getFileExtension(multipartFile);
    }

    @Test
    public void processInputFileWithCsvTest() throws Exception {
        File csvFile = new File(this.getClass().getResource("/csv/records_all_valid.csv").getFile());
        InputStream is = new FileInputStream(csvFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_all_valid.csv", TestConstants.XML_CONTENT_TYPE_2, is);
        is.close();
        List<CustomerStatement> recordDetails = new ArrayList<>();
        PowerMockito.spy(CustomerRecordValidationUtil.class);
        PowerMockito.when(CustomerRecordValidationUtil.getFileExtension(multipartFile)).thenReturn("csv");
        Whitebox.invokeMethod(customerStatementService,PROCESS_FILE_METHOD_NAME,multipartFile);
        Mockito.when(csvProcessorService.processCsvData(multipartFile)).thenReturn(recordDetails);
        verify(csvProcessorService, times(1)).processCsvData(multipartFile);
        PowerMockito.verifyStatic(CustomerRecordValidationUtil.class,Mockito.times(1));
        CustomerRecordValidationUtil.getFileExtension(multipartFile);
    }

    @Test(expected = UnsupportedFileFormatException.class)
    public void processInputFileWithUnsupportedFileTest() throws Exception {
        File textFile = new File(this.getClass().getResource("/records.txt").getFile());
        InputStream is = new FileInputStream(textFile);
        MockMultipartFile multipartFile = new MockMultipartFile("text", "records.txt", "text/plain", is);
        is.close();
        PowerMockito.spy(CustomerRecordValidationUtil.class);
        PowerMockito.when(CustomerRecordValidationUtil.getFileExtension(multipartFile)).thenReturn("text");
        Whitebox.invokeMethod(customerStatementService,PROCESS_FILE_METHOD_NAME,multipartFile);
    }

}
