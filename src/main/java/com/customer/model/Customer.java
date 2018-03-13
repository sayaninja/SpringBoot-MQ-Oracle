package com.customer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.StoredProcedureParameter;
import java.util.Date;

@Entity
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Customer.getNameFromId", procedureName = "GET_NAME_FROM_ID", parameters = {
        @StoredProcedureParameter(name = "IN_ID", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name = "OUT_NAME", mode = ParameterMode.OUT, type = String.class)}),
    @NamedStoredProcedureQuery(name = "Customer.throwEx", procedureName = "THROW_EX_PROCEDURE", parameters = {
        @StoredProcedureParameter(name = "IN_ID", mode = ParameterMode.IN, type = Integer.class),
        @StoredProcedureParameter(name = "OUT_NAME", mode = ParameterMode.OUT, type = String.class)})
})
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "customer_seq", initialValue = 1, allocationSize = 1, name = "CUST_SEQ")
    Long id;

    String name;
    String email;

    @Column(name = "CREATED_DATE")
    Date date;

    public Customer(String name, String email, Date date) {
        this.name = name;
        this.email = email;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                '}';
    }
}
