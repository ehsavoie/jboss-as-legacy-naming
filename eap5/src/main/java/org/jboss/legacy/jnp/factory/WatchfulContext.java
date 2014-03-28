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

package org.jboss.legacy.jnp.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Context which wraps around another one. This context should be used when wrapped context operate in different classloader
 * space than invoking entity. Currently {@link WatchfulContext} has hardcoded Classloader space to intersection of invoking
 * context class loader and class loader which loaded this class. <br>
 * Furthemore {@link WatchfulContext} decorates instance of java.lang.reflect.Proxy with handler which takes care of setting
 * proper class loader to allow said Proxy to execute in class loader space which has all required classes( currently
 * {@link WatchfulContext}.getClass().getClassLoader() ).
 * 
 * @author baranowb
 * 
 */
public class WatchfulContext implements Context {

    private final Object classLoaderLock = new Object();
    private ClassLoader classLoader;

    private final Context wrappedContext;

    public WatchfulContext(Context wrappedContext) {
        super();
        this.wrappedContext = wrappedContext;
    }

    public Object lookup(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            Object o = wrappedContext.lookup(name);
            if (o instanceof Proxy) {
                o = decorate(o, classLoader);
            }
            return o;
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Object lookup(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            Object o = wrappedContext.lookup(name);
            if (o instanceof Proxy) {
                o = decorate(o, classLoader);
            }
            return o;
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void bind(Name name, Object obj) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.bind(name, obj);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void bind(String name, Object obj) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.bind(name, obj);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void rebind(Name name, Object obj) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.rebind(name, obj);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void rebind(String name, Object obj) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.rebind(name, obj);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void unbind(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.unbind(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void unbind(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.unbind(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void rename(Name oldName, Name newName) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.rename(oldName, newName);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void rename(String oldName, String newName) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.rename(oldName, newName);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.list(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.list(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.listBindings(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.listBindings(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void destroySubcontext(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.destroySubcontext(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void destroySubcontext(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.destroySubcontext(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Context createSubcontext(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.createSubcontext(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Context createSubcontext(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.createSubcontext(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Object lookupLink(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.lookupLink(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Object lookupLink(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.lookupLink(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NameParser getNameParser(Name name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.getNameParser(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public NameParser getNameParser(String name) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.getNameParser(name);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.composeName(name, prefix);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public String composeName(String name, String prefix) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.composeName(name, prefix);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.addToEnvironment(propName, propVal);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.removeFromEnvironment(propName);
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.getEnvironment();
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public void close() throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            wrappedContext.close();
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    public String getNameInNamespace() throws NamingException {
        final ClassLoader classLoader = SecurityActions.getContextClassLoader();
        try {
            SecurityActions.setContextClassLoader(getWatchfulClassLoader());
            return wrappedContext.getNameInNamespace();
        } finally {
            SecurityActions.setContextClassLoader(classLoader);
        }
    }

    protected ClassLoader getWatchfulClassLoader() {

        if (this.classLoader == null) {
            synchronized (this.classLoaderLock) {
                if (this.classLoader != null)
                    return this.classLoader;

                // use module?
                final ClassLoader eap5EnabledClassLoader = this.getClass().getClassLoader();
                // contains classes for deserialization in eap5EnabledClassLoader
                final ClassLoader invocationClassLoader = SecurityActions.getContextClassLoader();
                this.classLoader = new WatchfulClassLoader(eap5EnabledClassLoader, invocationClassLoader);
            }
        }

        return this.classLoader;
    }

    private Object decorate(Object o, ClassLoader classLoader) {
        Proxy toWrap = (Proxy) o;
        Class[] interfaces = toWrap.getClass().getInterfaces();
        return Proxy.newProxyInstance(classLoader, interfaces, new ClassLoaderSwitchInvocationHandler(getWatchfulClassLoader(),
                o));
    }

    private class ClassLoaderSwitchInvocationHandler implements InvocationHandler {

        private final ClassLoader vengance;
        private final Object originalTarget;

        public ClassLoaderSwitchInvocationHandler(ClassLoader vengance, Object o) {
            super();
            this.vengance = vengance;
            this.originalTarget = o;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final ClassLoader old = SecurityActions.getContextClassLoader();
            try {
                SecurityActions.setContextClassLoader(this.vengance);
                return method.invoke(this.originalTarget, args);
            } finally {
                SecurityActions.setContextClassLoader(old);
            }

        }

    }
}
