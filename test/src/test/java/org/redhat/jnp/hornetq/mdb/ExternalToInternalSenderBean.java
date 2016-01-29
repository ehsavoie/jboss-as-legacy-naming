/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.redhat.jnp.hornetq.mdb;

import static org.redhat.jnp.hornetq.client.SimpleExternalContextInvokationTestCase.DEFAULT_CONNECTION_FACTORY;
import static org.redhat.jnp.hornetq.client.SimpleExternalContextInvokationTestCase.QUEUE_JNDI_NAME;
import static org.redhat.jnp.hornetq.client.SimpleExternalContextInvokationTestCase.REPLY_QUEUE_JNDI_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import org.hamcrest.Matchers;
import org.junit.Assert;
/**
 * Use external context to lookup local one and do the MDB magic
 * @author baranowb
 *
 */
@Singleton
public class ExternalToInternalSenderBean implements ExternalSendFace{
    private static final Logger log = Logger.getLogger(ExternalToInternalSenderBean.class.getName());
    private static final String JNDI_NAME = "java:global/client-context";
    @Resource(lookup = JNDI_NAME)
    private Context initialContext;


    public void testSendMessages() throws Exception {
        List<String> responses = sendMessage("Hello World!", "Bye bye world!");
        Assert.assertThat(responses, Matchers.is(Matchers.notNullValue()));

    }

    private List<String> sendMessage(String... messages) throws Exception {
        Connection connection = null;
        List<String> responses = new ArrayList<String>(messages.length);
        try {
            // Step 1. Perfom a lookup on the queue
            Destination destination = lookupDestination(initialContext);

            Destination target = lookupReceiver(initialContext);
            // Step 2. Perform a lookup on the Connection Factory
            ConnectionFactory connectionFactory = lookupConnectionFactory(initialContext);
            // Step 3. Open a connection
            connection = connectionFactory.createConnection("quickstartUser", "quickstartPwd1!");
            // Step 4. Open a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Step 5. Create a producer
            MessageProducer producer = session.createProducer(destination);
            MessageConsumer consumer = session.createConsumer(target);
            log.log(Level.INFO, "Sending {0} messages", messages.length);

            connection.start();

            // Send the specified number of messages
            for (String content : messages) {
                TextMessage message = session.createTextMessage(content);
                producer.send(message);
                log.log(Level.INFO, "Sent message: {0}", message.getText());
            }
            while (true) {
                TextMessage message = (TextMessage) consumer.receive(1000);
                if (message != null) {
                    System.out.println("Reading message: " + message.getText());
                    responses.add(message.getText());
                } else {
                    break;
                }
            }
            return responses;
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        } finally {
            // Step 12. Be sure to close our JMS resources!
            if (initialContext != null) {
                initialContext.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private ConnectionFactory lookupConnectionFactory(Context initialContext) throws NamingException {
        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        log.log(Level.INFO, "Attempting to acquire connection factory \"{0}\"", connectionFactoryString);
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connectionFactoryString);
        log.log(Level.INFO, "Found connection factory \"{0}\" in JNDI", connectionFactoryString);
        return connectionFactory;
    }
    
    private Destination lookupDestination(Context initialContext) throws NamingException {
        String destinationString = System.getProperty("destination", QUEUE_JNDI_NAME);
        log.log(Level.INFO, "Attempting to acquire destination \"{0}\"", destinationString);
        Destination destination = (Destination) initialContext.lookup(destinationString);
        log.log(Level.INFO, "Found destination \"{0}\" in JNDI", destinationString);
        return destination;
    }

    private Destination lookupReceiver(Context initialContext) throws NamingException {
        String destinationString = System.getProperty("destination", REPLY_QUEUE_JNDI_NAME);
        log.log(Level.INFO, "Attempting to acquire destination \"{0}\"", destinationString);
        Destination destination = (Destination) initialContext.lookup(destinationString);
        log.log(Level.INFO, "Found destination \"{0}\" in JNDI", destinationString);
        return destination;
    }
}
