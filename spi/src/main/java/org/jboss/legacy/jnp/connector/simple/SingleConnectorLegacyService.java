/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.legacy.jnp.connector.simple;

import java.net.UnknownHostException;
import org.jboss.legacy.LegacyService;
import org.jboss.legacy.jnp.server.JNPServer;
import org.jnp.server.Main;

/**
 * 
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a>  (c) 2013 Red Hat, inc.
 */
public class SingleConnectorLegacyService extends LegacyService {

    private Main serverConnector;    

    public SingleConnectorLegacyService(JNPServer jnpServer, String bindAddress, int port) throws UnknownHostException {
        this.serverConnector = new Main();
        this.serverConnector.setNamingInfo(jnpServer.getNamingBean());
        this.serverConnector.setBindAddress(bindAddress);
        this.serverConnector.setPort(port);
    }

    public SingleConnectorLegacyService(JNPServer jnpServer, String bindAddress, int port, String rmiBindAddress, int rmiPort) throws UnknownHostException {
       this(jnpServer, bindAddress, port);
        if (rmiBindAddress != null && !rmiBindAddress.isEmpty()) {
            this.serverConnector.setRmiBindAddress(rmiBindAddress);
            this.serverConnector.setRmiPort(rmiPort);
        }
    }

    @Override
    public void internalStart() throws Exception {       
        this.serverConnector.start();
    }

    @Override
    public void internalStop() {
        this.serverConnector.stop();
        this.serverConnector = null;
    }
}
