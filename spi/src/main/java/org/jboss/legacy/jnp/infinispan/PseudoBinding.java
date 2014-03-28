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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.naming.Binding;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class PseudoBinding {

    private final Binding binding;
    private final Name fullName;
    private final String child;

    public PseudoBinding(Binding binding) {
       this(binding, null, null);
    }

    public PseudoBinding(Name fullName, String child) {
       this(null, fullName, child);
    }

    private PseudoBinding(Binding binding, Name fullName, String child) {
        this.binding = binding;
        this.fullName = fullName;
        this.child = child;
    }

    public Binding getBinding() {
        return binding;
    }

    public Name getFullName() {
        return fullName;
    }

    public String getChild() {
        return child;
    }

    public boolean isBinding() {
        return this.binding != null;
    }
    
    public static Collection<PseudoBinding> convert(NamingEnumeration<Binding> bindings) throws NamingException {
        Collection<PseudoBinding> pseudoBindings = new LinkedList<PseudoBinding>();
        while(bindings.hasMore()) {
            pseudoBindings.add(new PseudoBinding(bindings.next()));
        }
        return pseudoBindings;
    }
    
    public static Collection<PseudoBinding> convert(Collection<Binding> bindings) throws NamingException {
        List<PseudoBinding> pseudoBindings = new ArrayList<PseudoBinding>(bindings.size());
        for(Binding binding : bindings) {
            pseudoBindings.add(new PseudoBinding(binding));
        }
        return pseudoBindings;
    }
}
