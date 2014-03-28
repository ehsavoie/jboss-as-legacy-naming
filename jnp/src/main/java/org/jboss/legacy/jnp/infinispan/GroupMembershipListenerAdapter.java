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

import java.util.List;
import org.jboss.as.clustering.ClusterNode;
import org.jboss.as.clustering.GroupMembershipListener;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class GroupMembershipListenerAdapter implements GroupMembershipListener {

    private final ClusterListener listener;

    public GroupMembershipListenerAdapter(ClusterListener listener) {
        this.listener = listener;
    }

    @Override
    public void membershipChanged(List<ClusterNode> list, List<ClusterNode> list1, List<ClusterNode> list2) {
        listener.membershipChanged(ClusterNodeAdapter.convertToList(list), ClusterNodeAdapter.convertToList(list1), ClusterNodeAdapter.convertToList(list2));
    }

    @Override
    public void membershipChangedDuringMerge(List<ClusterNode> list, List<ClusterNode> list1, List<ClusterNode> list2, List<List<ClusterNode>> list3) {
        listener.membershipChangedDuringMerge(ClusterNodeAdapter.convertToList(list),ClusterNodeAdapter.convertToList(list1),
                ClusterNodeAdapter.convertToList(list2), ClusterNodeAdapter.convertListOfList(list3));
    }

}
