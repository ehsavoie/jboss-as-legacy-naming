/*
 * Copyright (C) 2014 Red Hat, inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.redhat.jnp.hornetq.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.Authentication;
import org.jboss.as.controller.client.ModelControllerClient;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.INCLUDE_RUNTIME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.as.test.shared.TimeoutUtil;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redhat.jnp.hornetq.mdb.SimpleMDB;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2014 Red Hat, inc.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class HAInvokationTestCase {

    private static final Logger log = Logger.getLogger(HAInvokationTestCase.class.getName());

    // maximum time for HornetQ activation to detect node failover/failback
    private static final int ACTIVATION_TIMEOUT = 30000;

    private static final String DEPLOYMENT = "ping-pong";
    private static final String BACKUP_DEPLOYMENT = "backup";

    private static final String QUEUE_JNDI_NAME = "jms/queue/eap6Queue";
    private static final String REPLY_QUEUE_JNDI_NAME = "jms/queue/eap6ReplyQueue";
    private static final String DEFAULT_CONNECTION_FACTORY = "java:jboss/exported/jms/RemoteConnectionFactory";
    private static final String JNDI_CONFIG = "jndi-ha-eap6.properties";

    public static final String LIVE_SERVER = "jbossas-ha-node1";
    public static final String BACKUP_SERVER = "jbossas-ha-node2";

    @ArquillianResource
    private static ContainerController containerController;

    @ArquillianResource
    Deployer deployer;

    private ModelControllerClient liveClient;
    private ModelControllerClient backupClient;

    @Before
    public void initServer() throws Exception {
        containerController.start(LIVE_SERVER);
        System.out.println("===================");
        System.out.println("LIVE SERVER STARTED...");
        System.out.println("===================");
        if (containerController.isStarted(LIVE_SERVER)) {
            deployer.deploy(DEPLOYMENT);
            System.out.println("===================");
            System.out.println("LIVE SERVER DEPLOYED...");
            System.out.println("===================");
        }
        containerController.start(BACKUP_SERVER);
        System.out.println("===================");
        System.out.println("BACKUP SERVER STARTED...");
        System.out.println("===================");
    }

    @After
    public void closeServer() throws Exception {
        if (containerController.isStarted(BACKUP_SERVER)) {
            containerController.stop(BACKUP_SERVER);
            System.out.println("===================");
            System.out.println("BACKUP SERVER SERVER STOPPED...");
            System.out.println("===================");
        }
        if (containerController.isStarted(LIVE_SERVER)) {
            containerController.stop(LIVE_SERVER);
            System.out.println("===================");
            System.out.println("LIVE SERVER SERVER STOPPED...");
            System.out.println("===================");
        }
        liveClient = null;
        backupClient = null;
    }

    private static ModelControllerClient createLiveClient() throws UnknownHostException {
        return ModelControllerClient.Factory.create(InetAddress.getByName(TestSuiteEnvironment.getServerAddress()),
                TestSuiteEnvironment.getServerPort(), Authentication.getCallbackHandler());
    }

    private static ModelControllerClient createBackupClient() throws UnknownHostException {
        return ModelControllerClient.Factory.create(InetAddress.getByName(TestSuiteEnvironment.getServerAddress()),
                TestSuiteEnvironment.getServerPort() + 500, Authentication.getCallbackHandler());
    }

    private InitialContext getInitialContext() throws NamingException, IOException {
        Properties jndiProperties = new Properties();
        jndiProperties.load(this.getClass().getClassLoader().getResourceAsStream(System.getProperty("jndi_config", JNDI_CONFIG)));
        return new javax.naming.InitialContext(jndiProperties);
    }

    @Deployment(name = DEPLOYMENT, testable = false, managed = false)
    @TargetsContainer(LIVE_SERVER)
    public static Archive createDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, DEPLOYMENT + ".jar");
        jar.addClasses(SimpleMDB.class);
        return jar;
    }

    @Deployment(name = BACKUP_DEPLOYMENT, testable = false, managed = false)
    @TargetsContainer(BACKUP_SERVER)
    public static Archive createBackupDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, DEPLOYMENT + ".jar");
        jar.addClasses(SimpleMDB.class);
        return jar;
    }

    @Test
    public void testBackupActivation() throws Exception {
        liveClient = createLiveClient();
        backupClient = createBackupClient();
        checkHornetQServerStartedAndActiveAttributes(liveClient, true, true);
        checkHornetQServerStartedAndActiveAttributes(backupClient, true, false);
        assertThat(true, is(true));
        System.out.println("===================");
        System.out.println("TESTING LIVE SERVER...");
        System.out.println("===================");
        List<String> responses = sendMessage("Hello World!", "Bye bye world!");
        assertThat(responses, is(notNullValue()));
        assertThat(responses, hasSize(2));
        assertThat(responses, Matchers.containsInAnyOrder("Hello World!", "Bye bye world!"));

        System.out.println("===================");
        System.out.println("STOP LIVE SERVER...");
        System.out.println("===================");
        // shutdown live server
        containerController.stop(LIVE_SERVER);

        backupClient = createBackupClient();
        // let some time for the backup to detect the failure
        waitForHornetQServerActivation(backupClient, true, TimeoutUtil.adjust(ACTIVATION_TIMEOUT));
        checkHornetQServerStartedAndActiveAttributes(backupClient, true, true);
        System.out.println("===================");
        System.out.println("BACKUP SERVER STARTED...");
        System.out.println("===================");
        if (containerController.isStarted(BACKUP_SERVER)) {
            deployer.deploy(BACKUP_DEPLOYMENT);
            System.out.println("===================");
            System.out.println("BACKUP SERVER DEPLOYED...");
            System.out.println("===================");
        }

        responses = sendMessage("Hello World!", "Bye bye world!");
        assertThat(responses, is(notNullValue()));
        assertThat(responses, hasSize(2));
        assertThat(responses, Matchers.containsInAnyOrder("Hello World!", "Bye bye world!"));

        System.out.println("=============================");
        System.out.println("DONE...");
        System.out.println("=============================");
    }

    private void stopServer(ModelControllerClient client) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).setEmptyList();
        operation.get(OP).set("shutdown");
        try {
            execute(client, operation);
        } catch (IOException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof ExecutionException) {
                // ignore, this might happen if the channel gets closed before we got the response
            } else {
                throw e;
            }
        }
    }

    private List<String> sendMessage(String... messages) throws Exception {
        InitialContext initialContext = getInitialContext();
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

    private ConnectionFactory lookupConnectionFactory(InitialContext initialContext) throws NamingException {
        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        log.log(Level.INFO, "Attempting to acquire connection factory \"{0}\"", connectionFactoryString);
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(connectionFactoryString);
        log.log(Level.INFO, "Found connection factory \"{0}\" in JNDI", connectionFactoryString);
        return connectionFactory;
    }

    private Destination lookupDestination(InitialContext initialContext) throws NamingException {
        String destinationString = System.getProperty("destination", QUEUE_JNDI_NAME);
        log.log(Level.INFO, "Attempting to acquire destination \"{0}\"", destinationString);
        Destination destination = (Destination) initialContext.lookup(destinationString);
        log.log(Level.INFO, "Found destination \"{0}\" in JNDI", destinationString);
        return destination;
    }

    private Destination lookupReceiver(InitialContext initialContext) throws NamingException {
        String destinationString = System.getProperty("destination", REPLY_QUEUE_JNDI_NAME);
        log.log(Level.INFO, "Attempting to acquire destination \"{0}\"", destinationString);
        Destination destination = (Destination) initialContext.lookup(destinationString);
        log.log(Level.INFO, "Found destination \"{0}\" in JNDI", destinationString);
        return destination;
    }

    private static void waitForHornetQServerActivation(ModelControllerClient client, boolean expectedActive, int timeout) throws IOException {
        long start = System.currentTimeMillis();
        long now;
        do {
            ModelNode operation = new ModelNode();
            operation.get(OP_ADDR).add("subsystem", "messaging");
            operation.get(OP_ADDR).add("hornetq-server", "default");
            operation.get(OP).set(READ_RESOURCE_OPERATION);
            operation.get(INCLUDE_RUNTIME).set(true);
            try {
                ModelNode result = execute(client, operation);
                boolean started = result.get(RESULT, "started").asBoolean();
                boolean active = result.get(RESULT, "active").asBoolean();
                if (started && expectedActive == active) {
                    // leave some time to the hornetq children resources to be installed after the server is activated
                    Thread.sleep(TimeoutUtil.adjust(500));

                    return;
                }
            } catch (Exception e) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            now = System.currentTimeMillis();
        } while (now - start < timeout);

        fail("Server did not become active in the imparted time.");
    }

    private static void checkHornetQServerStartedAndActiveAttributes(ModelControllerClient client, boolean expectedStarted, boolean expectedActive) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).add("subsystem", "messaging");
        operation.get(OP_ADDR).add("hornetq-server", "default");
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(INCLUDE_RUNTIME).set(true);
        ModelNode result = execute(client, operation);
        assertEquals(expectedStarted, result.get(RESULT, "started").asBoolean());
        assertEquals(expectedActive, result.get(RESULT, "active").asBoolean());
    }

    private static ModelNode execute(ModelControllerClient client, ModelNode operation) throws IOException {
        ModelNode result = client.execute(operation);
        return result;
    }
}
