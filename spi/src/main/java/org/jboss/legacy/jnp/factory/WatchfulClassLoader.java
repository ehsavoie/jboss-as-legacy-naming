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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import sun.misc.CompoundEnumeration;

/**
 * Simple classloader which allows to merge different class loaders to bridge gap between them if needed.
 * 
 * @author wolfc
 * @author baranowb
 * 
 */
public class WatchfulClassLoader extends ClassLoader {
    private ClassLoader[] delegates;

    public WatchfulClassLoader(ClassLoader... delegates) {
        super(null);

        this.delegates = delegates;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader cl : delegates) {
            try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException e) {
                // ignore
            }
        }
        try {
            return super.findClass(name);
        } catch (Exception e) {
            throw new ClassNotFoundException("Did not find class in:\n" + dumpDelegates() + "\nand in current CL", e);
        }
    }

    /**
     * @return
     */
    private String dumpDelegates() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ClassLoader cl : this.delegates)
            stringBuilder.append("\t\n- ").append(cl);
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + "-[" + dumpDelegates() + "]";
    }

    @Override
    protected URL findResource(String name) {
        for (ClassLoader cl : delegates) {
            URL url = cl.getResource(name);
            if (url != null)
                return url;
        }
        return super.findResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        Enumeration tmp[] = new Enumeration[delegates.length];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = delegates[i].getResources(name);
        return new CompoundEnumeration(tmp);
    }
}
