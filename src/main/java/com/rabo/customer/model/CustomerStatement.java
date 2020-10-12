package com.rabo.customer.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author - Murugan Rajendran
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@JsonPropertyOrder({"txnReference","description","failureReason"})
public class CustomerStatement implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "reference")
    @JsonProperty("reference")
    private String txnReference;

    @JsonIgnore
    private String accountNumber;


    private String description;

    @JsonIgnore
    private String startBalance;

    @JsonIgnore
    private String mutation;

    @JsonIgnore
    private String endBalance;

    private List<String> failureReason = new ArrayList<>();

}
