package com.rabo.customer.service;

import com.rabo.customer.exception.FileParsingException;
import com.rabo.customer.model.CustomerStatement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import javax.xml.bind.Unmarshaller;
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
@SpringBootTest
public class XMLProcessorServiceTest {

    @Autowired
    XMLProcessorService xmlProcessorService;

    @Autowired
    Unmarshaller unmarshaller;

    @Test
    public void processXmlDataTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_all_valid.xml").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_all_valid.xml", "text/xml", is);
        is.close();
        List<CustomerStatement> result = xmlProcessorService.processXmlData(multipartFile);
        Assert.assertNotEquals(null, result);
        Assert.assertEquals(9, result.size());
    }

    @Test(expected = FileParsingException.class)
    public void processXmlDataWithCorruptFileTest() throws IOException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_corrupt.xml").getFile());
        InputStream is = new FileInputStream(xmlFile);
        MockMultipartFile multipartFile = new MockMultipartFile("xml", "records_corrupt.xml", "text/xml", is);
        is.close();
        xmlProcessorService.processXmlData(multipartFile);

    }

}
