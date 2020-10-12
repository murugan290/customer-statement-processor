package com.rabo.customer.constants;

/**
 * @author - Murugan Rajendran
 *
 */

public class Constants {

    private Constants() {

    }

    public static final String XML_CONTENT_TYPE_1 = "application/xml"; //  Swagger UI
    public static final String XML_CONTENT_TYPE_2 = "text/xml"; //  Integration Test
    public static final String CSV_CONTENT_TYPE_1 = "application/vnd.ms-excel";  //  Swagger UI
    public static final String CSV_CONTENT_TYPE_2 = "text/csv"; //  Integration Test
    public static final String XML_FILE_TYPE = "xml";
    public static final String CSV_FILE_TYPE = "csv";
    public static final String REFERENCE = "Reference";
    public static final String ACCOUNT_NUMBER = "AccountNumber";
    public static final String DESCRIPTION = "Description";
    public static final String START_BALANCE = "Start Balance";
    public static final String MUTATION = "Mutation";
    public static final String END_BALANCE = "End Balance";

    public static final String SUCCESS_RESULT = "SUCCESS, All Customer records are valid!!!";
    public static final String FILE_CANT_BE_EMPTY = "Empty File not allowed, Please check you input file.";
    public static final String DUPLICATE_CHECK= "DUPLICATE";
    public static final String BALANCE_CHECK= "BALANCE";
    public static final String DUPLICATE_REFERENCE_INCORRECT_END_BALANCE ="DUPLICATE_REFERENCE_INCORRECT_END_BALANCE";
    public static final String DUPLICATE_REFERENCE ="DUPLICATE_REFERENCE";
    public static final String INCORRECT_END_BALANCE ="INCORRECT_END_BALANCE";
    public static final String BALANCE_MISMATCHED ="BALANCE_MISMATCHED";
}
