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
package org.jboss.legacy.jnp.infinispan;

import java.util.Collection;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public interface InfinispanDistributedCacheTree {

    public void init();
    
    public void shutdown();

    public void bind(Name name, Object obj, String className) throws NamingException;

    public void rebind(Name name, Object obj, String className) throws NamingException;

    public void unbind(Name name) throws NamingException;
    
    public Object lookup(Name name) throws NamingException;
    
    public Collection<PseudoBinding> listBindings(Name name) throws NamingException;
    
    public Collection<NameClassPair> list(Name name) throws NamingException;
    
    public void createSubcontext(Name name) throws NamingException;
    
    public boolean isContextName(Name name) throws NamingException;
}
