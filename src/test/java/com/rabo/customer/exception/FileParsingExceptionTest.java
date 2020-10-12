package com.rabo.customer.exception;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;


/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(SpringRunner.class)
public class FileParsingExceptionTest {

    @Test
    public void testFileParsingException(){
        FileParsingException fileParsingException = new FileParsingException(400,"Parsing file - records.csv failed due to invalid content in file");
        Assert.assertNotEquals(null, fileParsingException);
        assertEquals(400, fileParsingException.getStatusCode());
        assertEquals("Parsing file - records.csv failed due to invalid content in file", fileParsingException.getMessage());
    }
}
