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
package org.jboss.legacy.jnp.connector.clustered;

import java.net.UnknownHostException;
import javax.naming.NamingException;
import org.jboss.ha.jndi.HANamingService;
import org.jboss.legacy.LegacyService;
import org.jboss.legacy.jnp.infinispan.InfinispanDistributedTreeManager;
import org.jboss.legacy.jnp.infinispan.InfinispanHAPartition;
import org.jboss.legacy.jnp.server.NamingStore;
import org.jboss.legacy.jnp.server.NamingStoreWrapper;

/**
 * @author baranowb
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class HAConnectorLegacyService extends LegacyService {
    
    private HANamingService haNamingService;

    public HAConnectorLegacyService(NamingStore namingStore, InfinispanDistributedTreeManager distributedCache, InfinispanHAPartition partition, String bindAddress, int port)
            throws UnknownHostException, NamingException {
        this.haNamingService = new HANamingService();
        NamingStoreWrapper singletonNamingServer = new NamingStoreWrapper(namingStore);
        this.haNamingService.setHAPartition(partition);
        this.haNamingService.setDistributedTreeManager(distributedCache);
        this.haNamingService.setLocalNamingInstance(singletonNamingServer);
        haNamingService.setBindAddress(bindAddress);
        haNamingService.setPort(port);
    }

    public HAConnectorLegacyService(NamingStore namingStore, InfinispanDistributedTreeManager distributedCache, InfinispanHAPartition partition, String bindAddress, int port, String rmiBindAddress, int rmiPort)
            throws UnknownHostException, NamingException {
        this(namingStore, distributedCache, partition, bindAddress, port);
        if (rmiBindAddress != null && !rmiBindAddress.isEmpty()) {
            this.haNamingService.setRmiBindAddress(rmiBindAddress);
            this.haNamingService.setRmiPort(rmiPort);
        }
    }

    public HANamingService getHaNamingService() {
        return haNamingService;
    }

    @Override
    public void internalStart() throws Exception {
        ((InfinispanHAPartition) haNamingService.getHAPartition()).start();
        haNamingService.create();
        haNamingService.start();
    }

    @Override
    public void internalStop() {
        this.haNamingService.stop();
        this.haNamingService = null;
    }
}
