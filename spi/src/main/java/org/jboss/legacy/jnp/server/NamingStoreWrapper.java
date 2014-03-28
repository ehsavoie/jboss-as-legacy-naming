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
package org.jboss.legacy.jnp.server;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import org.jnp.interfaces.Naming;
import org.jnp.interfaces.NamingContext;
import org.jnp.interfaces.NamingParser;
import org.jnp.server.SingletonNamingServer;

/**
 * Fallback to the NamingStore if the operation fails on this NamingServer.
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2014 Red Hat, inc.
 */
public class NamingStoreWrapper implements Naming {
    private static final NamingParser parser = new NamingParser();

    private final Naming singletonNamingServer;
    private final NamingStore namingStore;

    public NamingStoreWrapper(NamingStore namingStore) throws NamingException {
        this(namingStore, new SingletonNamingServer());
    }
    
    protected NamingStoreWrapper(NamingStore namingStore, Naming singletonNamingServer) throws NamingException {
        this.namingStore = namingStore;
        this.singletonNamingServer = singletonNamingServer;
        NamingContext.setLocal(this);
    }

    @Override
    public void bind(Name name, Object obj, String className) throws NamingException, RemoteException {
        singletonNamingServer.bind(name, obj, className);
    }

    @Override
    public void rebind(Name name, Object obj, String className) throws NamingException, RemoteException {
        singletonNamingServer.rebind(name, obj, className);
    }

    @Override
    public void unbind(Name name) throws NamingException, RemoteException {
        singletonNamingServer.unbind(name);
    }

    @Override
    public Object lookup(Name name) throws NamingException, RemoteException {
        try {
            return lookupInternal(name);
        } catch (Exception t) {
           if(hasExportedPrefix(name)) {
                return lookupInternal(stripJBossExportedContext(name));
            } 
            return lookupInternal(parser.parse("java:jboss/exported/" + name.toString()));
        }
    }

    private Object lookupInternal(Name name) throws NamingException, RemoteException {
        try {
            return singletonNamingServer.lookup(name);
        } catch (Exception t) {
            return namingStore.lookup(name);
        }
    }

    @Override
    public Collection<NameClassPair> list(Name name) throws NamingException, RemoteException {
        try {
            return singletonNamingServer.list(name);
        } catch (Exception t) {
            return namingStore.list(name);
        }
    }

    @Override
    public Collection<Binding> listBindings(Name name) throws NamingException, RemoteException {
        try {
            return singletonNamingServer.listBindings(name);
        } catch (Exception t) {
            return namingStore.listBindings(name);
        }
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException, RemoteException {
        synchronized (this) {
            try {
                return singletonNamingServer.createSubcontext(name);
            } catch (NamingException e) {
                Object value = singletonNamingServer.lookup(name);
                if (value instanceof Context) {
                    return (Context) value;
                }
                throw e;
            }
        }
    }
    
    private boolean hasExportedPrefix(Name name) {
       return getJBossExportedIndex(name) >=0;
    }

    protected Name stripJBossExportedContext(Name name) {
        int index = getJBossExportedIndex(name);
        if (index > 0) {
            return name.getSuffix(index);
        }
        return name;
    }

    private int getJBossExportedIndex(Name name) {
        boolean hasJboss = false;
        for (int i = 0; i < name.size(); i++) {
            String component = name.get(i);
            if (hasJboss) {
                if ("exported".equals(component)) {
                    return i + 1;
                }
                return -1;
            } else {
                hasJboss = "jboss".equals(component) || "java:jboss".equals(component);
            }
        }
        return -1;
    }
}
