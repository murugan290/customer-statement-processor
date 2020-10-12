package com.rabo.customer.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class CustomerRecords {

    @XmlElement(name = "record")
    List<CustomerStatement> customerStatements;

}
