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
import java.util.Collection;
import java.util.List;
import org.infinispan.Cache;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;
import org.wildfly.clustering.infinispan.spi.service.CacheServiceName;

/**
 * 
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a>  (c) 2013 Red Hat, inc.
 */
public class DistributedTreeManagerServiceAddStepHandler extends AbstractBoottimeAddStepHandler {

    public static final DistributedTreeManagerServiceAddStepHandler INSTANCE = new DistributedTreeManagerServiceAddStepHandler();

    public DistributedTreeManagerServiceAddStepHandler() {
        super(DistributedTreeManagerResourceDefinition.CACHE_CONTAINER, DistributedTreeManagerResourceDefinition.CACHE_REF);
    }

    @Override
    protected void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
            ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {
        newControllers.addAll(this.installRuntimeServices(context, operation, model, verificationHandler));
    }

    Collection<ServiceController<?>> installRuntimeServices(final OperationContext context, final ModelNode operation,
            final ModelNode model, final ServiceVerificationHandler verificationHandler) throws OperationFailedException {
        final String cacheRef = DistributedTreeManagerResourceDefinition.CACHE_REF.resolveModelAttribute(context, operation).asString();
        final String containerRef = DistributedTreeManagerResourceDefinition.CACHE_CONTAINER.resolveModelAttribute(context, operation).asString();
        final DistributedTreeManagerService service = new DistributedTreeManagerService();
        final ServiceTarget serviceTarget = context.getServiceTarget();
        final ServiceBuilder<InfinispanDistributedTreeManager> serviceBuilder = serviceTarget.addService(DistributedTreeManagerService.SERVICE_NAME, service);
        serviceBuilder.addDependency(CacheServiceName.CACHE.getServiceName(containerRef, cacheRef), Cache.class, service.getCache());
        final ServiceController<InfinispanDistributedTreeManager> distributedTreeManagerController = serviceBuilder.install();
        final List<ServiceController<?>> installedServices = new ArrayList<ServiceController<?>>();
        installedServices.add(distributedTreeManagerController);
        return installedServices;
    }
}
