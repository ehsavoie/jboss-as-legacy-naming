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
package org.jboss.legacy.jnp.connector.clustered;

import java.util.ArrayList;
import java.util.List;
//import org.jboss.as.clustering.ClusterNode;
//import org.jboss.as.clustering.impl.CoreGroupCommunicationService;
import org.jboss.legacy.jnp.infinispan.ClusterListener;
import org.jboss.legacy.jnp.infinispan.ClusterNodeAdapter;
import org.jboss.legacy.jnp.infinispan.ClusterNodeProxy;
import org.jboss.legacy.jnp.infinispan.ClusterResponseFilter;
import org.jboss.legacy.jnp.infinispan.ClusterResponseFilterAdapter;
import org.jboss.legacy.jnp.infinispan.ClusterStateTransferProvider;
import org.jboss.legacy.jnp.infinispan.ClusterStateTransferProviderAdapter;
import org.jboss.legacy.jnp.infinispan.GroupMembershipListenerAdapter;
import org.jboss.legacy.jnp.infinispan.HAGroupCommunicationService;
import org.jboss.msc.service.ServiceName;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class InfinispanGroupCommunicationService implements HAGroupCommunicationService {

    //TODO: XXX
    //private final CoreGroupCommunicationService service;

    public InfinispanGroupCommunicationService(/*CoreGroupCommunicationService service*/) {
        //this.service = service;
    }

    public static ServiceName getServiceName(String name) {
        //return CoreGroupCommunicationService.getServiceName(name);
        return null;
    }

    @Override
    public String getNodeName() {
        //return service.getNodeName();
        return null;
    }

    @Override
    public String getGroupName() {
        //return service.getGroupName();
        return null;
    }

    @Override
    public List<String> getCurrentView() {
        //return service.getCurrentView();
        return null;
    }

    @Override
    public long getCurrentViewId() {
        //return service.getCurrentViewId();
        return 1;
    }

    @Override
    public List<ClusterNodeProxy> getClusterNodes() {
        List<ClusterNodeProxy> result = new ArrayList<ClusterNodeProxy>();
//        for(ClusterNode node : service.getClusterNodes()) {
//            result.add(new ClusterNodeProxy(node.getIpAddress(), node.getName(), node.getPort()));
//        }
        return result;
    }

    @Override
    public ClusterNodeProxy getClusterNode() {
//        ClusterNode node = service.getClusterNode();
//        if (node != null) {
//            return new ClusterNodeProxy(node.getIpAddress(), node.getName(), node.getPort());
//        }
        return null;
    }

    @Override
    public void registerRPCHandler(String objName, Object subscriber) {
        //service.registerRPCHandler(objName, subscriber);
    }

    @Override
    public void unregisterRPCHandler(String objName, Object subscriber) {
        //service.unregisterRPCHandler(objName, subscriber);
    }

    @Override
    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        //return service.callMethodOnCluster(serviceName, methodName, args, types, excludeSelf);
        return null;
    }

    @Override
    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, ClusterResponseFilter filter) throws Exception {
        //return service.callMethodOnCluster(serviceName, methodName, args, types, excludeSelf, new ClusterResponseFilterAdapter(filter));
        return null;
    }

    @Override
    public Object callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeProxy targetNode) throws Exception {
        //return service.callMethodOnNode(serviceName, methodName, args, types, new ClusterNodeAdapter(targetNode));
        return null;
    }

    @Override
    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeProxy targetNode) throws Exception {
        //service.callAsyncMethodOnNode(serviceName, methodName, args, types, new ClusterNodeAdapter(targetNode));
    }

    @Override
    public void callAsynchMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        //service.callAsynchMethodOnCluster(serviceName, methodName, args, types, excludeSelf);
    }

    @Override
    public boolean getAllowSynchronousMembershipNotifications() {
        //return service.getAllowSynchronousMembershipNotifications();
        return false;
    }

    @Override
    public void setAllowSynchronousMembershipNotifications(boolean allowSync) {
        //service.setAllowSynchronousMembershipNotifications(allowSync);
    }

    @Override
    public void registerGroupMembershipListener(ClusterListener listener) {
        //service.registerGroupMembershipListener(new GroupMembershipListenerAdapter(listener));
    }

    @Override
    public void unregisterGroupMembershipListener(ClusterListener listener) {
        //service.unregisterGroupMembershipListener(new GroupMembershipListenerAdapter(listener));
    }

    @Override
    public void registerStateTransferProvider(String serviceName, ClusterStateTransferProvider provider) {
        //service.registerStateTransferProvider(serviceName, new ClusterStateTransferProviderAdapter(provider));
    }

    @Override
    public void unregisterStateTransferProvider(String serviceName) {
        //service.unregisterStateTransferProvider(serviceName);
    }
    
    @Override
    public ArrayList callMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        //return service.callMethodOnCoordinatorNode(serviceName, methodName, args, types, excludeSelf);
        return null;
    }
}
