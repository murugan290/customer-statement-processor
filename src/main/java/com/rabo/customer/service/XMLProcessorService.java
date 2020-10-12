package com.rabo.customer.service;


import com.rabo.customer.exception.FileParsingException;
import com.rabo.customer.model.CustomerRecords;
import com.rabo.customer.model.CustomerStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.Unmarshaller;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@Service
public class XMLProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLProcessorService.class);

    @Autowired
    private Unmarshaller unmarshaller;

    /**
     * This method will parse the xml file content and create list of customer records
     * @param file
     * @return list of CustomerStatement
     */
    public List<CustomerStatement> processXmlData(MultipartFile file){
        String fileName = file.getOriginalFilename();
        CustomerRecords records = null;
        try {
             records = (CustomerRecords) unmarshaller.unmarshal(file.getInputStream());
        }catch(Exception e) {
            LOGGER.error("XMLProcessorService :: processXmlData() - Exception at processXmlData : ", e);
            throw new FileParsingException(HttpStatus.BAD_REQUEST.value() , String.join(" ", "Parsing file -",fileName,"failed due to invalid content in file"));
        }
        return records.getCustomerStatements();
    }
}
