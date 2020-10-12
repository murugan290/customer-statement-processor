package com.rabo.customer;


import com.rabo.customer.constants.Constants;
import com.rabo.customer.constants.TestConstants;
import com.rabo.customer.response.CustomerValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author - Murugan Rajendran
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerStatementProcessorApplicationTests {

    @Autowired
    Unmarshaller unmarshaller;

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void contextLoads() {
        assertNotNull(unmarshaller);
    }

    @Test
    public void uploadValidXmlCustomerDataTest() throws URISyntaxException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_all_valid.xml").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(xmlFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(200, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals("SUCCESS, All Customer records are valid!!!", outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }

    @Test
    public void uploadXmlFileWithDuplicateReferenceTest() throws URISyntaxException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_duplicate_reference.xml").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(xmlFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);


        assertNotEquals(null, result);

        assertEquals(200, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals(Constants.DUPLICATE_REFERENCE, outcome.getMessage());
        assertEquals(2,outcome.getErrorRecords().size());

        assertEquals(TestConstants.REFERENCE_NUMBER, outcome.getErrorRecords().get(0).getTxnReference());
        assertEquals(TestConstants.REFERENCE_NUMBER,outcome.getErrorRecords().get(0).getTxnReference());

        assertEquals(Constants.DUPLICATE_REFERENCE,outcome.getErrorRecords().get(0).getFailureReason().get(0));
        assertEquals(Constants.DUPLICATE_REFERENCE,outcome.getErrorRecords().get(1).getFailureReason().get(0));

    }

    @Test
    public void uploadXmlFileWithDuplicateAndIncorrectBalanceTest() throws URISyntaxException {
        File xmlFile = new File(this.getClass().getResource("/xml/records_dupRef_balMismatch.xml").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(xmlFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);


        assertNotEquals(null, result);

        assertEquals(200, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals(Constants.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, outcome.getMessage());
        assertEquals(3,outcome.getErrorRecords().size());

        assertEquals(TestConstants.REFERENCE_NUMBER, outcome.getErrorRecords().get(0).getTxnReference());
        assertEquals(TestConstants.REFERENCE_NUMBER, outcome.getErrorRecords().get(1).getTxnReference());
        assertEquals("170148", outcome.getErrorRecords().get(2).getTxnReference());
        assertEquals(Constants.DUPLICATE_REFERENCE,outcome.getErrorRecords().get(0).getFailureReason().get(0));
        assertEquals(Constants.DUPLICATE_REFERENCE,outcome.getErrorRecords().get(1).getFailureReason().get(0));
        assertEquals(Constants.BALANCE_MISMATCHED,outcome.getErrorRecords().get(2).getFailureReason().get(0));
    }

    @Test
    public void uploadEmptyXmlFileTest() throws URISyntaxException{
        File xmlFile = new File(this.getClass().getResource("/xml/records_empty.xml").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(xmlFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(500, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals(Constants.FILE_CANT_BE_EMPTY, outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }

    @Test
    public void uploadEmptyCsvFileTest() throws URISyntaxException{
        File csvFile = new File(this.getClass().getResource("/csv/records_empty.csv").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(csvFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(500, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals(Constants.FILE_CANT_BE_EMPTY, outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }

    @Test
    public void uploadValidCsvCustomerDataTest() throws URISyntaxException {
        File csvFile = new File(this.getClass().getResource("/csv/records_all_valid.csv").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(csvFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(200, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals("SUCCESS, All Customer records are valid!!!", outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }

    @Test
    public void uploadCsvFileWithIncorrectBalanceTest() throws URISyntaxException {
        File csvFile = new File(this.getClass().getResource("/csv/records_incorrect_endbalance.csv").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(csvFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);


        assertNotEquals(null, result);

        assertEquals(200, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals(Constants.INCORRECT_END_BALANCE, outcome.getMessage());
        assertEquals(1,outcome.getErrorRecords().size());
        assertEquals("195446", outcome.getErrorRecords().get(0).getTxnReference());
        assertEquals(Constants.BALANCE_MISMATCHED,outcome.getErrorRecords().get(0).getFailureReason().get(0));

    }

    @Test
    public void uploadFileWithErroneousContentTest() throws URISyntaxException {
        File csvFile = new File(this.getClass().getResource("/csv/records_parsing_error.csv").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(csvFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(400, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals("Parsing file - records_parsing_error.csv failed due to invalid content in file", outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }


    @Test
    public void uploadUnsupportedFileFormatTest() throws URISyntaxException {
        File textFile = new File(this.getClass().getResource("/records.txt").getFile());
        final String baseUrl = TestConstants.DOMAIN + randomServerPort+ TestConstants.PATH;
        URI uri = new URI(baseUrl);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(textFile));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<CustomerValidationResult> result = restTemplate.postForEntity(uri, requestEntity, CustomerValidationResult.class);

        assertNotEquals(null, result);

        assertEquals(500, result.getStatusCodeValue());
        CustomerValidationResult outcome = result.getBody();
        assertNotNull(outcome);
        assertNotNull(outcome.getMessage());
        assertEquals("The given file format text/plain is not supported", outcome.getMessage());
        assertEquals(0,outcome.getErrorRecords().size());
    }

}
