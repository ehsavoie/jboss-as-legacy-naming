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

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.wildfly.clustering.group.Node;
import org.jgroups.stack.IpAddress;
import org.jboss.legacy.jnp.infinispan.ClusterNodeProxy;
/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class ClusterNodeAdapter implements Node {

    private final ClusterNodeProxy node;
    
    public ClusterNodeAdapter(ClusterNodeProxy node) {
        this.node = node;
        
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return this.node.getAddress();
    }

    public int compareTo(Node o) {
        return getId(this).compareTo(getId(o));
    }
    

    private String getId(Node node) {
        IpAddress address = new IpAddress(node.getSocketAddress().getAddress(), node.getSocketAddress().getPort());
        return address.getIpAddress().getHostAddress() + ":" + address.getPort();
    }
    
    public static List<ClusterNodeProxy> convertToList(List<Node> nodes) {
        List<ClusterNodeProxy> result = new ArrayList<ClusterNodeProxy>(nodes.size());
        for(Node node : nodes) {
            result.add(new ClusterNodeProxy(node.getSocketAddress().getAddress(), node.getName(), node.getSocketAddress().getPort()));
        }
        return result;
    }
    
    public static List<List<ClusterNodeProxy>> convertListOfList(List<List<Node>> nodes) {
        List<List<ClusterNodeProxy>> result = new ArrayList<List<ClusterNodeProxy>>(nodes.size());
        for(List<Node> list : nodes) {
            result.add(convertToList(list));
        }
        return result;
    }
}
