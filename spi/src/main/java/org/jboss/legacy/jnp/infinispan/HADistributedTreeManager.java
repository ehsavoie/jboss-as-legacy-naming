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
package org.jboss.legacy.jnp.infinispan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import org.jboss.ha.jndi.spi.DistributedTreeManager;
import org.jboss.logging.Logger;
import org.jnp.interfaces.Naming;
import org.jnp.interfaces.NamingContext;
import org.jnp.interfaces.NamingParser;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class HADistributedTreeManager implements org.jnp.interfaces.Naming, DistributedTreeManager {

    static final long serialVersionUID = 6342802270002172451L;

    private static Logger log = Logger.getLogger(HADistributedTreeManager.class);

    private static final NamingParser parser = new NamingParser();

    private final InfinispanDistributedCacheTree tree;
    private Naming haStub;
    protected boolean acquiredCache = false;

    public HADistributedTreeManager(InfinispanDistributedCacheTree tree) {
        super();
        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        tree.init();
    }

    @Override
    public void shutdown() {
        this.tree.shutdown();
    }

    @Override
    public Naming getHAStub() {
        return this.haStub;
    }

    @Override
    public void setHAStub(Naming stub) {
        this.haStub = stub;
    }

    @Override
    public void bind(Name name, Object obj, String className) throws NamingException {
        this.tree.bind(name, obj, className);
    }

    @Override
    public void rebind(Name name, Object obj, String className) throws NamingException {
        this.tree.rebind(name, obj, className);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        this.tree.unbind(name);
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        boolean trace = log.isTraceEnabled();
        if (trace) {
            log.trace("lookup, name=" + name);
        }

        if (name.isEmpty()) {
            // Return this
            return new NamingContext(null, parser.parse(""), this.getHAStub());
        }

        // is the name a context?
        if (this.tree.isContextName(name)) {
            Name fullName = (Name) name.clone();
            return new NamingContext(null, fullName, this.getHAStub());
        }
        return this.tree.lookup(name);
    }

    @Override
    public Collection<NameClassPair> list(Name name) throws NamingException {
        return this.tree.list(name);
    }

    @Override
    public Collection<Binding> listBindings(Name name) throws NamingException {
        Collection<PseudoBinding> pseudos = this.tree.listBindings(name);
        List<Binding> bindings = new ArrayList<Binding>(pseudos.size());
        for (PseudoBinding pseudo : pseudos) {
            if (pseudo.isBinding()) {
                bindings.add(pseudo.getBinding());
            } else {
                NamingContext subCtx = new NamingContext(null, pseudo.getFullName(), this.getHAStub());
                bindings.add(new Binding(pseudo.getChild(), NamingContext.class.getName(), subCtx, true));
            }
        }
        return bindings;
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        this.tree.createSubcontext(name);

        Name fullName = parser.parse("");
        fullName.addAll(name);
        return new NamingContext(null, fullName, this.getHAStub());
    }
}
