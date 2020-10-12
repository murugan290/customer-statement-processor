package com.rabo.customer.service;


import com.rabo.customer.constants.TestConstants;
import com.rabo.customer.exception.FileParsingException;
import com.rabo.customer.model.CustomerStatement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(SpringRunner.class)
public class CSVProcessorServiceTest {

    @InjectMocks
    CSVProcessorService csvProcessorService;

    @Test
    public void processCSVDataTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/csv/records_all_valid.csv").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("csv", "records_all_valid.csv", TestConstants.CSV_CONTENT_TYPE_2, is);
        is.close();
        List<CustomerStatement> result = csvProcessorService.processCsvData(multipartFile);
        Assert.assertNotEquals(null, result);
        Assert.assertEquals(10, result.size());
    }

    @Test(expected = FileParsingException.class)
    public void processXmlDataWithCorruptFileTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/csv/records_corrupt.csv").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("csv", "records_corrupt.csv", "text/csv", is);
        is.close();
        csvProcessorService.processCsvData(multipartFile);

    }


}
