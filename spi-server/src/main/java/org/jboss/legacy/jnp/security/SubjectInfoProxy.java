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

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.security.SubjectInfo;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.extensions.CredentialIdentity;

/**
 * @author baranowb
 *
 */
public class SubjectInfoProxy {

    private final SubjectInfo subjectInfo;

    /**
     * @param subjectInfo
     */
    public SubjectInfoProxy(SubjectInfo subjectInfo) {
        super();
        this.subjectInfo = subjectInfo;
    }

    public Subject getAuthenticatedSubject() {
        return subjectInfo.getAuthenticatedSubject();
    }

    /**
     * @return
     * @see org.jboss.security.SubjectInfo#getIdentities()
     */
    public Set<CredentialIdentityProxy> getIdentities() {
        final Set<Identity> proxied = this.subjectInfo.getIdentities();
        if(proxied == null || proxied.size() ==0){
            return null;
        }
        Set<CredentialIdentityProxy> shown = new HashSet<CredentialIdentityProxy>(proxied.size()); 
        for(Identity i:proxied){
            if(i instanceof CredentialIdentity){
                shown.add(new CredentialIdentityProxy((CredentialIdentity) i));
            } else {
                //TODO: warn ?
            }
        }
        return shown;
    }
    //TODO: SubjectInfo.roles?
}
