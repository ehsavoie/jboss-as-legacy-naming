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

package org.jboss.legacy.jnp.security;

import java.security.Principal;
import java.security.acl.Group;

import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.extensions.CredentialIdentity;

/**
 * @author baranowb
 * 
 */
public class CredentialIdentityProxy {

    private final CredentialIdentity identity;

    /**
     * @param identity
     */
    public CredentialIdentityProxy(CredentialIdentity identity) {
        super();
        this.identity = identity;
    }

    /**
     * @return
     * @see org.jboss.security.identity.Identity#asGroup()
     */
    public Group asGroup() {
        return identity.asGroup();
    }

    /**
     * @return
     * @see org.jboss.security.identity.Identity#asPrincipal()
     */
    public Principal asPrincipal() {
        return identity.asPrincipal();
    }

    /**
     * @return
     * @see org.jboss.security.identity.Identity#getName()
     */
    public String getName() {
        return identity.getName();
    }

    /**
     * @return
     * @see org.jboss.security.identity.Identity#getRole()
     */
    public Role getRole() {
        return identity.getRole();
    }

    /**
     * @return
     * @see org.jboss.security.identity.extensions.CredentialIdentity#getCredential()
     */
    public Object getCredential() {
        return identity.getCredential();
    }

}
