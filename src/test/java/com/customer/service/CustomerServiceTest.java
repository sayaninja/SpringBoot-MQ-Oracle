package com.customer.service;

import com.customer.mq.MQProvider;
import com.ibm.mq.MQException;
import org.hibernate.exception.GenericJDBCException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    MQProvider mqProvider;

    @Autowired
    CustomerService customerService;

    @Before
    public void setUp() throws MQException, IOException {
        mqProvider.clearQueue();
    }

    @Test
    public void testCorrectNameReturned() throws MQException, IOException {
        mqProvider.putMessageString("1");
        assertEquals("zhan", customerService.getNameById());
    }

    @Test(expected = GenericJDBCException.class)
    public void testExThrownAndMsgLeftInQueue() throws IOException, MQException {
        mqProvider.putMessageString("1212");
        try {
            customerService.getException();
        } finally {
            assertEquals("1212", mqProvider.getMessageString());
        }
    }
}