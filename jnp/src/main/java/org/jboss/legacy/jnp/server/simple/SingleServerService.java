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
 *//*
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
package org.jboss.legacy.jnp.server.simple;

import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.legacy.jnp.server.JNPServer;
import org.jboss.legacy.jnp.server.JNPServerService;
import org.jboss.legacy.jnp.server.LegacyJNPServerService;
import org.jboss.legacy.jnp.server.NamingStoreAdapter;
import org.jboss.legacy.jnp.JNPLogger;
import org.jboss.legacy.jnp.JNPMessages;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * @author baranowb
 */
public class SingleServerService implements JNPServerService {

    private final InjectedValue<ServiceBasedNamingStore> namingStoreValue = new InjectedValue<ServiceBasedNamingStore>();
    private LegacyJNPServerService service;

    public SingleServerService() {
        super();
    }

    @Override
    public JNPServer getValue() throws IllegalStateException, IllegalArgumentException {
        return service.getJNPServer();
    }

    /**
     * Get the naming store injector.
     *
     * @return the injector
     */
    public Injector<ServiceBasedNamingStore> getNamingStoreInjector() {
        return namingStoreValue;
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        JNPLogger.ROOT_LOGGER.startJNPServer();
        try {
            this.service = new SingleServerLegacyService(new NamingStoreAdapter(namingStoreValue.getValue()));
            this.service.start();
        } catch (Exception e) {
            throw JNPMessages.MESSAGES.failedToStartJNPServerService(e);
        }
    }

    @Override
    public void stop(StopContext stopContext) {
        JNPLogger.ROOT_LOGGER.stopJNPServer();
        try {
            this.service.stop();
        } catch (Exception ex) {
            JNPLogger.ROOT_LOGGER.couldNotStopJNPServer(ex);
        }
    }
}
