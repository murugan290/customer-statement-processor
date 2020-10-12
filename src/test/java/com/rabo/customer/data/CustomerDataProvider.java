package com.rabo.customer.data;

import com.rabo.customer.model.CustomerStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

public class CustomerDataProvider {

    private CustomerDataProvider(){

    }

    public static List<CustomerStatement> getXmLData() {

        List<CustomerStatement> customerStatements = new ArrayList<>();
        CustomerStatement customerStatement1 = new CustomerStatement();
        customerStatement1.setTxnReference("130498");
        customerStatement1.setAccountNumber("NL69ABNA0433647324");
        customerStatement1.setDescription("Tickets for Peter Theuß");
        customerStatement1.setStartBalance("26.9");
        customerStatement1.setMutation("-18.78");
        customerStatement1.setEndBalance("8.12");
        customerStatements.add(customerStatement1);

        CustomerStatement customerStatement2 = new CustomerStatement();
        customerStatement2.setTxnReference("130498");
        customerStatement2.setAccountNumber("NL93ABNA0585619023");
        customerStatement2.setDescription("Tickets from Erik de Vries");
        customerStatement2.setStartBalance("5429");
        customerStatement2.setMutation("-939");
        customerStatement2.setEndBalance("6368");
        customerStatements.add(customerStatement2);

        CustomerStatement customerStatement3 = new CustomerStatement();
        customerStatement3.setTxnReference("147674");
        customerStatement3.setAccountNumber("NL93ABNA0585619024");
        customerStatement3.setDescription("Subscription from Peter Dekker");
        customerStatement3.setStartBalance("75.69");
        customerStatement3.setMutation("-44.91");
        customerStatement3.setEndBalance("30.78");
        customerStatements.add(customerStatement3);

        CustomerStatement customerStatement4 = new CustomerStatement();
        customerStatement4.setTxnReference("135607");
        customerStatement4.setAccountNumber("NL27SNSB0917829871");
        customerStatement4.setDescription("Subscription from Peter Theuß");
        customerStatement4.setStartBalance("74.69");
        customerStatement4.setMutation("-44.90");
        customerStatement4.setEndBalance("30.87");
        customerStatements.add(customerStatement4);
        return customerStatements;

    }


    public static List<CustomerStatement> getCsvData(){

        List<CustomerStatement> customerStatements = new ArrayList<>();
        CustomerStatement customerStatement1 = new CustomerStatement();
        customerStatement1.setTxnReference("130499");
        customerStatement1.setAccountNumber("NL69ABNA0433647324");
        customerStatement1.setDescription("Tickets for Peter Theuß");
        customerStatement1.setStartBalance("26.9");
        customerStatement1.setMutation("-18.78");
        customerStatement1.setEndBalance("8.12");
        customerStatements.add(customerStatement1);

        CustomerStatement customerStatement2 = new CustomerStatement();
        customerStatement2.setTxnReference("130499");
        customerStatement2.setAccountNumber("NL93ABNA0585619025");
        customerStatement2.setDescription("Tickets from Erik de Vries");
        customerStatement2.setStartBalance("5429");
        customerStatement2.setMutation("-939");
        customerStatement2.setEndBalance("6368");
        customerStatements.add(customerStatement2);

        CustomerStatement customerStatement3 = new CustomerStatement();
        customerStatement3.setTxnReference("147674");
        customerStatement3.setAccountNumber("NL93ABNA0585619026");
        customerStatement3.setDescription("Subscription from Peter Dekker");
        customerStatement3.setStartBalance("75.69");
        customerStatement3.setMutation("-44.91");
        customerStatement3.setEndBalance("30.78");
        customerStatements.add(customerStatement3);

        CustomerStatement customerStatement4 = new CustomerStatement();
        customerStatement4.setTxnReference("135607");
        customerStatement4.setAccountNumber("NL27SNSB0917829871");
        customerStatement4.setDescription("Subscription from Peter Theuß");
        customerStatement4.setStartBalance("74.69");
        customerStatement4.setMutation("-44.92");
        customerStatement4.setEndBalance("30.87");
        customerStatements.add(customerStatement4);
        return customerStatements;

    }
}
