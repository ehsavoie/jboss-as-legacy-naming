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
package org.jboss.legacy.jnp.connector.clustered;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import org.jboss.as.clustering.ClusterNode;
import org.jboss.as.clustering.GroupMembershipListener;
import org.jboss.as.clustering.ResponseFilter;
import org.jboss.as.clustering.SerializableStateTransferResult;
import org.jboss.as.clustering.StateTransferProvider;
import org.jboss.as.clustering.StreamStateTransferResult;
import org.jboss.as.clustering.impl.CoreGroupCommunicationService;
import org.jboss.legacy.jnp.infinispan.ClusterNodeAdapter;
import org.jboss.legacy.jnp.infinispan.HAGroupCommunicationService;
import org.jboss.legacy.jnp.infinispan.ResponseFilterAdapter;
import org.jboss.legacy.jnp.infinispan.StateTransferProviderAdapter;
import org.jboss.msc.service.ServiceName;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class InfinispanGroupCommunicationService implements HAGroupCommunicationService {

    private final CoreGroupCommunicationService service;

    public InfinispanGroupCommunicationService(CoreGroupCommunicationService service) {
        this.service = service;
    }

    public static ServiceName getServiceName(String name) {
        return CoreGroupCommunicationService.getServiceName(name);
    }

    @Override
    public String getNodeName() {
        return service.getNodeName();
    }

    @Override
    public String getGroupName() {
        return service.getGroupName();
    }

    @Override
    public List<String> getCurrentView() {
        return service.getCurrentView();
    }

    @Override
    public long getCurrentViewId() {
        return service.getCurrentViewId();
    }

    @Override
    public List<ClusterNode> getClusterNodes() {
        return service.getClusterNodes();
    }

    @Override
    public ClusterNode getClusterNode() {
        return service.getClusterNode();
    }
    @Override
    public void registerRPCHandler(String objName, Object subscriber) {
        service.registerRPCHandler(objName, subscriber);
    }

    @Override
    public void unregisterRPCHandler(String objName, Object subscriber) {
        service.unregisterRPCHandler(objName, subscriber);
    }

    @Override
    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        return service.callMethodOnCluster(serviceName, methodName, args, types, excludeSelf);
    }

    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, ResponseFilter filter) throws Exception {
        return service.callMethodOnCluster(serviceName, methodName, args, types, excludeSelf, filter);
    }

    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, ResponseFilter filter, long methodTimeout, boolean unordered) throws Exception {
        return service.callMethodOnCluster(serviceName, methodName, args, types, excludeSelf, filter, methodTimeout, unordered);
    }

    public <T> T callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNode targetNode) throws Exception {
        return service.callMethodOnNode(serviceName, methodName, args, types, targetNode);
    }

    public <T> T callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, long methodTimeout, ClusterNode targetNode) throws Exception {
        return service.callMethodOnNode(serviceName, methodName, args, types, methodTimeout, targetNode);
    }

    public <T> T callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, long methodTimeout, ClusterNode targetNode, boolean unordered) throws Exception {
        return service.callMethodOnNode(serviceName, methodName, args, types, methodTimeout, targetNode, unordered);
    }

    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNode targetNode) throws Exception {
        service.callAsyncMethodOnNode(serviceName, methodName, args, types, targetNode);
    }

    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNode targetNode, boolean unordered) throws Exception {
        service.callAsyncMethodOnNode(serviceName, methodName, args, types, targetNode, unordered);
    }

    @Override
    public void callAsynchMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        service.callAsynchMethodOnCluster(serviceName, methodName, args, types, excludeSelf);
    }

    public void callAsynchMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, boolean unordered) throws Exception {
        service.callAsynchMethodOnCluster(serviceName, methodName, args, types, excludeSelf, unordered);
    }

    public void callAsyncMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        service.callAsyncMethodOnCoordinatorNode(serviceName, methodName, args, types, excludeSelf);
    }

    public void callAsyncMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, boolean unordered) throws Exception {
        service.callAsyncMethodOnCoordinatorNode(serviceName, methodName, args, types, excludeSelf, unordered);
    }

    @Override
    public boolean getAllowSynchronousMembershipNotifications() {
        return service.getAllowSynchronousMembershipNotifications();
    }

    @Override
    public void setAllowSynchronousMembershipNotifications(boolean allowSync) {
        service.setAllowSynchronousMembershipNotifications(allowSync);
    }

    @Override
    public void registerGroupMembershipListener(GroupMembershipListener listener) {
        service.registerGroupMembershipListener(listener);
    }

    @Override
    public void unregisterGroupMembershipListener(GroupMembershipListener listener) {
        service.unregisterGroupMembershipListener(listener);
    }

    public long getStateTransferTimeout() {
        return service.getStateTransferTimeout();
    }

    public void setStateTransferTimeout(long timeout) {
        service.setStateTransferTimeout(timeout);
    }

    public Future<SerializableStateTransferResult> getServiceState(String serviceName, ClassLoader classloader) {
        return service.getServiceState(serviceName, classloader);
    }

    public Future<SerializableStateTransferResult> getServiceState(String serviceName) {
        return service.getServiceState(serviceName);
    }

    public Future<StreamStateTransferResult> getServiceStateAsStream(String serviceName) {
        return service.getServiceStateAsStream(serviceName);
    }

    public void registerStateTransferProvider(String serviceName, StateTransferProvider provider) {
        service.registerStateTransferProvider(serviceName, provider);
    }

    @Override
    public void unregisterStateTransferProvider(String serviceName) {
        service.unregisterStateTransferProvider(serviceName);
    }

    @Override
    public List callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf, ResponseFilterAdapter responseFilterAdapter) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList callMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types, boolean excludeSelf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeAdapter clusterNodeAdapter) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types, ClusterNodeAdapter clusterNodeAdapter) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerStateTransferProvider(String serviceName, StateTransferProviderAdapter stateTransferProviderAdapter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
