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
import java.util.List;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public interface HAGroupCommunicationService {

    public String getGroupName();

    public String getNodeName();

    public void registerRPCHandler(String serviceName, Object handler);

    public void unregisterRPCHandler(String serviceName, Object subscriber);

    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception;

    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, 
            ClusterResponseFilter responseFilter) throws Exception;

    public void callAsynchMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception;

    public ArrayList callMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception;

    public Object callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeProxy clusterNode) throws Exception;

    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeProxy clusterNode) throws Exception;

    public void registerStateTransferProvider(String serviceName, ClusterStateTransferProvider stateTransferProvider);

    public void unregisterStateTransferProvider(String serviceName);

    public void registerGroupMembershipListener(ClusterListener get);

    public void unregisterGroupMembershipListener(ClusterListener get);

    public boolean getAllowSynchronousMembershipNotifications();

    public void setAllowSynchronousMembershipNotifications(boolean allowSync);

    public long getCurrentViewId();

    public List<String> getCurrentView();

    public List<ClusterNodeProxy> getClusterNodes();

    public ClusterNodeProxy getClusterNode();
    
}
