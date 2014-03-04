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

package org.jboss.legacy;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public abstract class LegacyService {
    public void start() throws Exception {
        ClassLoader old = switchLoader(this.getClass().getClassLoader());
        try {
            internalStart();
        } finally {
            switchLoader(old);
        }
    }

    public void stop() throws Exception {
        ClassLoader old = switchLoader(this.getClass().getClassLoader());
        try {
            internalStop();
        } finally {
            switchLoader(old);
        }
    }

    /**
     * Implementing class should allocate/start resource in this method.
     * 
     * @throws Exception
     */
    protected abstract void internalStart() throws Exception;

    /**
     * Implementing class should free/stop resource in this method.
     * 
     * @throws Exception
     */
    protected abstract void internalStop() throws Exception;

    public static ClassLoader switchLoader(final ClassLoader loader) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        return current;
    }
}
