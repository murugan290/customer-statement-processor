package com.rabo.customer.exception;


import com.rabo.customer.constants.Constants;
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
public class InvalidFileExceptionTest {

    @Test
    public void testEmptyFileException(){
        InvalidFileException emptyFileException = new InvalidFileException(500, Constants.FILE_CANT_BE_EMPTY);
        Assert.assertNotEquals(null, emptyFileException);
        assertEquals(500, emptyFileException.getStatusCode());
        assertEquals("Empty File not allowed, Please check you input file.", emptyFileException.getMessage());
    }

}
