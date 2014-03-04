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
package org.jboss.legacy.jnp.server.clustered;

import org.jboss.ha.jndi.HANamingService;
import org.jboss.legacy.jnp.connector.clustered.HAConnectorLegacyService;
import org.jboss.legacy.jnp.server.JNPServer;
import org.jboss.legacy.jnp.server.LegacyJNPServerService;
import org.jnp.interfaces.Naming;
import org.jnp.server.NamingBean;

/**
 * @author baranowb
 */
public class HAServerLegacyService  extends LegacyJNPServerService {

    private final HANamingService haNamingService;

    private JNPServer server;

    public HAServerLegacyService(HAConnectorLegacyService haConnectorService) {
       this.haNamingService = haConnectorService.getHaNamingService();
    }

    @Override
    public JNPServer getJNPServer()  {
        return this.server;
    }


    @Override
    public void internalStart() throws Exception {
        this.server = new JNPServer() {
            @Override
            public NamingBean getNamingBean() {
                return new NamingBean() {

                    @Override
                    public Naming getNamingInstance() {
                        return haNamingService.getLocalNamingInstance();
                    }
                };
            }
        };
    }

    @Override
    public void internalStop() {
        this.server = null;
    }
}
