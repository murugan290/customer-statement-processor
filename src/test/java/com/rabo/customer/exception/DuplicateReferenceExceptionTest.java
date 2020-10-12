package com.rabo.customer.exception;

import com.rabo.customer.constants.Constants;
import com.rabo.customer.model.CustomerStatement;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;


/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(SpringRunner.class)
public class DuplicateReferenceExceptionTest {

    @Test
    public void testDuplicateReferenceException() {
        List<CustomerStatement> recordDetails = new ArrayList<>();
        CustomerStatement recordDetail = new CustomerStatement();
        recordDetail.setTxnReference("177666");
        List<String> failureReasons = new ArrayList<>();
        failureReasons.add(Constants.DUPLICATE_REFERENCE);
        recordDetail.setFailureReason(failureReasons);
        recordDetails.add(recordDetail);

        DuplicateReferenceException duplicateRefException = new DuplicateReferenceException(200,recordDetails, Constants.DUPLICATE_REFERENCE);
        Assert.assertNotEquals(null, duplicateRefException);
        assertEquals(200, duplicateRefException.getStatusCode());
        assertEquals(Constants.DUPLICATE_REFERENCE, duplicateRefException.getMessage());

        assertEquals(1, duplicateRefException.getFailedRecords().size());
        CustomerStatement failedRecord = duplicateRefException.getFailedRecords().get(0);
        assertEquals(Constants.DUPLICATE_REFERENCE, failedRecord.getFailureReason().get(0));

    }
}
