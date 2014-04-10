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

import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

import java.net.InetAddress;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

/**
 * @author baranowb
 * 
 */
@MessageLogger(projectCode = "LEGACY")
public interface JNPLogger extends BasicLogger {

    /**
     * A logger with a category of the package name.
     */
    JNPLogger ROOT_LOGGER = Logger.getMessageLogger(JNPLogger.class, JNPLogger.class.getPackage().getName());

    @LogMessage(level = INFO)
    @Message(id = 54000, value = "Starting legacy HA connector service. \nJNP enpoint:\n\tbinding='%s'\n\taddress='%s:%s'\nRMI endpoint:\n\tbinding='%s'\n\taddress='%s:%s'")
    void startHAConnectorService(final String bindingName, final InetAddress address, final int port,
            final String rmiBindingName, final InetAddress rmiAddress, final int rmiPort);

    @LogMessage(level = INFO)
    @Message(id = 54001, value = "Stopping legacy HA connector service.")
    void stopHAConnectorService();

    @LogMessage(level = WARN)
    @Message(id = 54002, value = "Failed to stop legacy HA connector service.")
    void couldNotStopHAConnectorService(@Cause Exception e);

    @LogMessage(level = INFO)
    @Message(id = 54003, value = "Starting legacy singleton connector service. \nJNP enpoint:\n\tbinding='%s'\n\taddress='%s:%s'\nRMI endpoint:\n\tbinding='%s'\n\taddress='%s:%s'")
    void startSingletonConnectorService(final String bindingName, final InetAddress address, final int port,
            final String rmiBindingName, final InetAddress rmiAddress, final int rmiPort);

    @LogMessage(level = INFO)
    @Message(id = 54004, value = "Stopping legacy singleton connector service.")
    void stopSingletonConnectorService();

    @LogMessage(level = WARN)
    @Message(id = 54005, value = "Failed to stop legacy signleton connector service.")
    void couldNotStopSingletonConnectorService(@Cause Exception e);

    @LogMessage(level = INFO)
    @Message(id = 54006, value = "Starting legacy distributed cache service.")
    void startDistributedCache();

    @LogMessage(level = INFO)
    @Message(id = 54007, value = "Stopping legacy distributed cache service.")
    void stoppingDistributedCache();

    @LogMessage(level = INFO)
    @Message(id = 54008, value = "Starting legacy HA JNP service.")
    void startHAJNPServer();

    @LogMessage(level = INFO)
    @Message(id = 54009, value = "Stopping legacy HA JNP service.")
    void stopHAJNPServer();

    @LogMessage(level = WARN)
    @Message(id = 54010, value = "Failed to stop HA JNP cache service.")
    void couldNotStopHAJNPServer(@Cause Exception ex);
    
    @LogMessage(level = INFO)
    @Message(id = 54011, value = "Starting legacy JNP service.")
    void startJNPServer();

    @LogMessage(level = INFO)
    @Message(id = 54012, value = "Stopping legacy JNP service.")
    void stopJNPServer();

    @LogMessage(level = WARN)
    @Message(id = 54013, value = "Failed to stop JNP cache service.")
    void couldNotStopJNPServer(@Cause Exception ex);

}