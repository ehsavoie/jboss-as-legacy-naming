/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.redhat.jnp.hornetq.client;

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.common.jms.JMSOperations;
import org.jboss.as.test.integration.common.jms.JMSOperationsProvider;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redhat.jnp.hornetq.mdb.ExternalSendFace;
import org.redhat.jnp.hornetq.mdb.ExternalToInternalSenderBean;
import org.redhat.jnp.hornetq.mdb.SimpleMDB;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2014 Red Hat, inc.
 * @author baranowb
 */
@RunWith(Arquillian.class)
public class SimpleExternalContextInvokationTestCase {

    private static final Logger log = Logger.getLogger(SimpleExternalContextInvokationTestCase.class.getName());

    public static final String QUEUE_NAME = "eap6Queue";
    public static final String QUEUE_JNDI_NAME = "jms/queue/"+QUEUE_NAME;
    public static final String REPLY_QUEUE_NAME = "eap6ReplyQueue";
    public static final String REPLY_QUEUE_JNDI_NAME = "jms/queue/"+REPLY_QUEUE_NAME;
    public static final String DEFAULT_CONNECTION_FACTORY = "java:jboss/exported/jms/RemoteConnectionFactory";
    public static final String JNDI_CONFIG = "jndi-eap6.properties";
    public static final String DEPLOYMENT = "ping-pong";
    public static final String DEPLOYMENT_NAME = DEPLOYMENT+".jar";
    public static final String DEPLOYMENT_ROCKET = "rocket";
    public static final String DEPLOYMENT_ROCKET_NAME= DEPLOYMENT_ROCKET+".jar";

    public static final String SERVER = "jbossas";
    
    @ArquillianResource
    private static ContainerController containerController;

    private ManagementClient managementClient = new ManagementClient(TestSuiteEnvironment.getModelControllerClient(),
            TestSuiteEnvironment.getServerAddress(), TestSuiteEnvironment.getServerPort(), "http-remoting");

    private JMSOperations jmsAdminOperations = JMSOperationsProvider.getInstance(managementClient);

    @ArquillianResource
    private Deployer deployer;

    @Before
    public void initServer() throws Exception {
        containerController.start(SERVER);
        if (containerController.isStarted(SERVER)) {
            jmsAdminOperations.createJmsQueue(QUEUE_NAME, QUEUE_JNDI_NAME);
            jmsAdminOperations.createJmsQueue(REPLY_QUEUE_NAME, REPLY_QUEUE_JNDI_NAME);
            deployer.deploy(DEPLOYMENT);
            deployer.deploy(DEPLOYMENT_ROCKET);
        }
    }

    @After
    public void closeServer() throws Exception {
        if (containerController.isStarted(SERVER)) {
            deployer.undeploy(DEPLOYMENT);
            deployer.undeploy(DEPLOYMENT_ROCKET);
            jmsAdminOperations.removeJmsQueue(QUEUE_NAME);
            jmsAdminOperations.removeJmsQueue(REPLY_QUEUE_NAME);
            jmsAdminOperations.close();
            containerController.stop(SERVER);
        }
    }
    
    @Deployment(name = DEPLOYMENT, testable = false, managed = false)
    public static Archive<JavaArchive> createDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, DEPLOYMENT_NAME);
        jar.addClasses(SimpleMDB.class);
        return jar;
    }
    
    @Deployment(name = DEPLOYMENT_ROCKET, testable = false, managed = false)
    public static Archive<JavaArchive> createRocketDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, DEPLOYMENT_ROCKET_NAME);
        jar.addClasses(ExternalToInternalSenderBean.class, ExternalSendFace.class);
        jar.addClass(SimpleExternalContextInvokationTestCase.class);
        jar.addPackage(Matchers.class.getPackage());
        jar.addPackage(org.hamcrest.core.Is.class.getPackage());
        jar.addPackage(org.junit.Assert.class.getPackage());

        jar.addAsManifestResource(new StringAsset("Dependencies: org.apache.activemq.artemis\n"), "MANIFEST.MF");
        return jar;
    }

    @Test
    public void testSendMessages() throws Exception {
        final InitialContext ctx = getInitialContext();
        final String name = "ejb:/" + DEPLOYMENT_ROCKET + "/" + ExternalToInternalSenderBean.class.getSimpleName()
                + "!"+ExternalSendFace.class.getName();
        ExternalSendFace face = (ExternalSendFace) ctx.lookup(name);
        face.testSendMessages();
    }

    private static InitialContext getInitialContext() throws NamingException {
        final Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");
        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        env.put(Context.PROVIDER_URL, "remote://" + TestSuiteEnvironment.getServerAddress() + ":" + 4447);
        env.put("jboss.naming.client.ejb.context", true);
        return new InitialContext(env);
    }
}
