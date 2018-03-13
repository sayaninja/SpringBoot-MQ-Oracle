package com.customer.mq;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class MQProvider {
    @Value("${servers.mq.host}")
    private String host;
    @Value("${servers.mq.port}")
    private Integer port;
    @Value("${servers.mq.queue-manager}")
    private String queueManagerName;
    @Value("${servers.mq.channel}")
    private String channelName;
    @Value("${servers.mq.request-queue}")
    private String requestQueueName;
    @Value("${servers.mq.reply-queue}")
    private String replyQueueName;

    private MQQueueManager queueManager;
    private MQQueue queue;

    @PostConstruct
    public void init() throws MQException {
        // Set MQEnvironment before calling MQQueueManager constructor
        MQEnvironment.hostname = host;
        MQEnvironment.channel = channelName;
        MQEnvironment.port = port;

        queueManager = new MQQueueManager(queueManagerName);
        queue = queueManager.accessQueue(requestQueueName,
                CMQC.MQOO_INPUT_AS_Q_DEF +
                CMQC.MQOO_OUTPUT +
                CMQC.MQOO_INQUIRE);
    }

    public void putMessageString(String strMessage) throws IOException, MQException {
        MQMessage message = new MQMessage();
        message.writeString(strMessage);
        this.putMessage(message);
    }

    public void putMessage(MQMessage message) throws MQException {
        MQPutMessageOptions pmo = new MQPutMessageOptions();
        queue.put(message, pmo);
    }

    public String getMessageString() throws IOException, MQException {
        MQMessage message = this.getMessage();
        int strLen = message.getMessageLength();
        byte[] strData = new byte[strLen];
        message.readFully(strData, 0, strLen);
        return new String(strData);
    }

    // TODO: Handle when no messages available
    public MQMessage getMessage() throws MQException {
        MQMessage message = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options += CMQC.MQGMO_SYNCPOINT;
        queue.get(message, gmo);
        return message;
    }

    public void commit() throws MQException {
        queueManager.commit();
    }

    public void backout() {
        try {
            queueManager.backout();
        } catch (MQException e) {
            e.printStackTrace();
        }
    }

    public void clearQueue() throws MQException, IOException {
        int currDepth = queue.getCurrentDepth();
        while (currDepth > 0) {
            MQMessage message= new MQMessage();
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            queue.get(message, gmo);

            message.clearMessage();
            currDepth--;
        }
    }
}
