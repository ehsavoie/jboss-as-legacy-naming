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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.jnp.interfaces.NamingContextFactory;

/**
 * InitialContext factory which should be used to configure external-context in AS configuration. Internally it relies on JNP context
 * factory. 
 * 
 * @author baranowb
 * 
 */
public class WatchfulContextFactory implements InitialContextFactory {

    private NamingContextFactory wrappedFactory = new NamingContextFactory();

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        final Context toWrap = this.wrappedFactory.getInitialContext(environment);
        final WatchfulContext toReturn = new WatchfulContext(toWrap);
        return toReturn;
    }

}
