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
public class UnsupportedFileFormatExceptionTest {

    @Test
    public void testEmptyFileException(){
        UnsupportedFileFormatException unSupportedFileException = new UnsupportedFileFormatException(500,"The given file format records.txt is not supported");
        Assert.assertNotEquals(null, unSupportedFileException);
        assertEquals(500, unSupportedFileException.getStatusCode());
        assertEquals("The given file format records.txt is not supported", unSupportedFileException.getMessage());
    }
}
