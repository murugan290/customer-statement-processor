package com.rabo.customer;

import com.rabo.customer.model.CustomerRecords;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@SpringBootApplication
public class CustomerStatementProcessorApplication {


    @Bean
    public Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CustomerRecords.class);
        return jaxbContext.createUnmarshaller();
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerStatementProcessorApplication.class, args);
    }

}
