package com.customer.service;

import com.customer.mq.MQProvider;
import com.ibm.mq.MQException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.io.IOException;

@Component
public class CustomerService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MQProvider mqProvider;

    @Transactional
    public String getNameById() {
        String name = null;

        try {
            StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Customer.getNameFromId");
            int id = Integer.parseInt(mqProvider.getMessageString());
            query.setParameter("IN_ID", id);
            name = (String) query.getOutputParameterValue("OUT_NAME");
            mqProvider.commit();
        } catch (Exception e) {
            mqProvider.backout();
            e.printStackTrace();
        }

        return name;
    }

    @Transactional
    public String getException() throws IOException, MQException {
        String name = null;

        try {
            StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("Customer.throwEx");
            int id = Integer.parseInt(mqProvider.getMessageString());
            query.setParameter("IN_ID", id);
            name = (String) query.getOutputParameterValue("OUT_NAME");
            mqProvider.commit();
        } catch (Exception e) {
            mqProvider.backout();
            e.printStackTrace();
            throw e;
        }

        return name;
    }
}
