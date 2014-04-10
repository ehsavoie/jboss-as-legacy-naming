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

package org.jboss.legacy.jnp;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.msc.service.StartException;
/**
 * @author baranowb
 * 
 */
@MessageBundle(projectCode = "LEGACY")
public interface JNPMessages {

    /**
     * The messages
     */
    JNPMessages MESSAGES = Messages.getBundle(JNPMessages.class);

    @Message(id = 54100, value = "Failed to start legacy HA connector service.")
    StartException failedToStartHAConnectorService(@Cause Exception e);
    
    @Message(id = 54102, value = "Failed to start legacy singleton connector service.")
    StartException failedToStartSingletonConnectorService(@Cause Exception e);
    
    @Message(id = 54103, value = "Failed to start legacy HA JNP service.")
    StartException failedToStartHAJNPServerService(@Cause Exception ex);
    
    @Message(id = 54104, value = "Failed to start legacy JNP service.")
    StartException failedToStartJNPServerService(@Cause Exception ex);
}