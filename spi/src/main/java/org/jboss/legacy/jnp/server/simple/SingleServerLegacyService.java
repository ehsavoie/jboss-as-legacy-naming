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

import javax.naming.NamingException;
import org.jboss.legacy.jnp.server.JNPServer;
import org.jboss.legacy.jnp.server.LegacyJNPServerService;
import org.jboss.legacy.jnp.server.NamingStore;
import org.jboss.legacy.jnp.server.NamingStoreWrapper;
import org.jnp.interfaces.Naming;
import org.jnp.interfaces.NamingContext;
import org.jnp.server.NamingBean;

/**
 * 
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a>  (c) 2013 Red Hat, inc.
 */
public class SingleServerLegacyService extends LegacyJNPServerService {

    private NamingStoreWrapper namingStoreWrapper;

    public SingleServerLegacyService(NamingStore namingStore) throws NamingException {
        this.namingStoreWrapper = new NamingStoreWrapper(namingStore);
    }

    @Override
    public JNPServer getJNPServer() {
        return new SingleJNPServer();
    }

    @Override
    public void internalStart() throws Exception {
        NamingContext.setLocal(this.namingStoreWrapper);
    }

    @Override
    public void internalStop() {
        NamingContext.setLocal(null);
        this.namingStoreWrapper = null;
    }

    class SingleJNPServer implements JNPServer {

        @Override
        public NamingBean getNamingBean() {
            return new NamingBean() {

                @Override
                public Naming getNamingInstance() {
                    return namingStoreWrapper;
                }
            };
        }
    }

}
