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
public class DuplicateRefAndBalanceMismatchExceptionTest {

    @Test
    public void testDuplicateRefAndBalanceMismatchException() {
        List<CustomerStatement> recordDetails = new ArrayList<>();
        CustomerStatement recordDetail = new CustomerStatement();
        recordDetail.setTxnReference("177666");
        List<String> failureReasons = new ArrayList<>();
        failureReasons.add(Constants.DUPLICATE_REFERENCE);
        failureReasons.add(Constants.BALANCE_MISMATCHED);
        recordDetail.setFailureReason(failureReasons);
        recordDetails.add(recordDetail);

        DuplicateRefAndBalanceMismatchException duplicateRefAndBalanceMismatchException = new DuplicateRefAndBalanceMismatchException(200, recordDetails, Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
        Assert.assertNotEquals(null, duplicateRefAndBalanceMismatchException);
        assertEquals(200, duplicateRefAndBalanceMismatchException.getStatusCode());
        assertEquals(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, duplicateRefAndBalanceMismatchException.getMessage());

        assertEquals(1, duplicateRefAndBalanceMismatchException.getFailedRecords().size());
        CustomerStatement failedRecord = duplicateRefAndBalanceMismatchException.getFailedRecords().get(0);
        assertEquals(2, failedRecord.getFailureReason().size());
        assertEquals(Constants.DUPLICATE_REFERENCE, failedRecord.getFailureReason().get(0));
        assertEquals(Constants.BALANCE_MISMATCHED, failedRecord.getFailureReason().get(1));
    }
}
