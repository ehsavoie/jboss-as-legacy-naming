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
package org.jboss.legacy.jnp.server;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NamingException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

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

}
