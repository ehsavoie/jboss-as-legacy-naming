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

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import static org.hamcrest.CoreMatchers.is;
import org.jnp.interfaces.Naming;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.argThat;
import org.mockito.Mockito;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2014 Red Hat, inc.
 */
public class NamingStoreWrapperTest {

    /**
     * Test of stripJBossExportedContext method, of class NamingStoreWrapper.
     */
    @Test
    public void testStripJBossExportedContext() throws NamingException {
        Name name = new CompositeName("java:jboss/exported/cn=PUT_TEST.LOAD.TOPIC");
        NamingStoreWrapper instance = new NamingStoreWrapper(null);
        Name result = instance.stripJBossExportedContext(name);
        assertThat(result.toString(), is("cn=PUT_TEST.LOAD.TOPIC"));

        name = new CompositeName("jboss/exported/cn=PUT_TEST.LOAD.TOPIC");
        result = instance.stripJBossExportedContext(name);
        assertThat(result.toString(), is("cn=PUT_TEST.LOAD.TOPIC"));

        name = new CompositeName("jboss/jms/cn=PUT_TEST.LOAD.TOPIC");
        result = instance.stripJBossExportedContext(name);
        assertThat(result.toString(), is("jboss/jms/cn=PUT_TEST.LOAD.TOPIC"));

        name = new CompositeName("jms/cn=PUT_TEST.LOAD.TOPIC");
        result = instance.stripJBossExportedContext(name);
        assertThat(result.toString(), is("jms/cn=PUT_TEST.LOAD.TOPIC"));
    }
    
    
    @Test
    public void testLookupJBossExportedContext() throws Exception {
        Name completeName = new CompositeName("java:jboss/exported/cn=PUT_TEST.LOAD.TOPIC");
        Name simpleName = new CompositeName("cn=PUT_TEST.LOAD.TOPIC");   
        NamingStore eap6Store = Mockito.mock(NamingStore.class);
        Mockito.when(eap6Store.lookup(completeName)).thenThrow(new NameNotFoundException());
        Mockito.when(eap6Store.lookup(simpleName)).thenReturn("failure");
        Naming eap5Server = Mockito.mock(Naming.class);
        Mockito.when(eap5Server.lookup(completeName)).thenThrow(new NameNotFoundException());
        Mockito.when(eap5Server.lookup(simpleName)).thenReturn("success");
        NamingStoreWrapper instance = new NamingStoreWrapper(eap6Store, eap5Server);
        assertThat(instance.lookup(completeName).toString(), is("success"));
    }
    
    
    @Test
    public void testLookupSimple() throws Exception {
        Name completeName = new CompositeName("java:jboss/exported/cn=PUT_TEST.LOAD.TOPIC");
        Name simpleName = new CompositeName("cn=PUT_TEST.LOAD.TOPIC");   
        NamingStore eap6Store = Mockito.mock(NamingStore.class);
        Mockito.when(eap6Store.lookup(simpleName)).thenThrow(new NameNotFoundException());
        Mockito.when(eap6Store.lookup(completeName)).thenReturn("failure");
        Naming eap5Server = Mockito.mock(Naming.class);
        Mockito.when(eap5Server.lookup(simpleName)).thenThrow(new NameNotFoundException());
        Mockito.when(eap5Server.lookup(argThat(new NameMatcher(completeName)))).thenReturn("success");
        NamingStoreWrapper instance = new NamingStoreWrapper(eap6Store, eap5Server);
        assertThat(instance.lookup(simpleName).toString(), is("success"));
    }
    
    class NameMatcher extends ArgumentMatcher<Name> {
        private final Name name;
        public NameMatcher(Name name) {
            this.name = name;
        }
        
        @Override
	   public boolean matches(Object currentName) {
	       return name.toString().equals(currentName.toString());
	   }
	}

}
