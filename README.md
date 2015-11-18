jboss-as-legacy-naming
======================

#Legacy Naming extension

The full documentation is at https://mojo.redhat.com/docs/DOC-950104

EAP5 used a different naming and remote protocols than EAP6. 
Thus when you want to interact with EAP6 from an EAP5 client (or server) you are confronted with a lot of issues.  
One solution would be to upgrade your client to EAP6, but sometimes this is not as simple as it seems.  
For these cases we have developed a set of extensions for seamless integration of legacy clients with EAP6 and above.  
<!--This extension is there to facilitate the migration from EAP 5 to EAP6, as such it is supported only as long as EAP 5 support last.  
_Being a migration tool there won't be any backport, only the latest greatest version (so no backports) is supported within the same lifecycle as EAP 5.3_.-->

Another tool exists for adding support to remote EJB 3 invokation and use of UserTransaction with an EAP 5 client.  
This tool uses the curent extension but to avoid any compatibility issue **DO NOT INSTALL THIS MODULE YOURSELF** if you are using or about to use the 
**jboss-as-legacy extension**.

#Configuration parameters
 - *jnp-connector* : mandatory element
    - _socket-binding_ : mandatory, defines the port used for te JNP server.
    - _rmi-socket-binding_ : optionnal, defines the port used for RMI connection. If it is not defined it will default to *1099*.
 - *distributed-cache* : optionnal element
    - _cache-container_ : name of the cache container to use.
    - _cache-ref_ : name of the cache in the cache container to use.

##Minimal configuration example

    <extensions>  
    ...  
      <extension module="org.jboss.legacy.jnp"/>  
    </extensions>  
    ...  
    <subsystem xmlns="urn:jboss:domain:legacy-jnp:1.0">  
      <jnp-server/>  
      <jnp-connector socket-binding="jnp" />
    </subsystem>    
    ...  
    <socket-binding-group>  
      ...  
      <socket-binding name="jnp" port="5599" interface="jnp"/>  
      ...  
    </socket-binding-group> 

##Full configuration example

    <extensions>  
    ...  
      <extension module="org.jboss.legacy.jnp"/>  
    </extensions>  
    ...  
    <subsystem xmlns="urn:jboss:domain:legacy-jnp:1.0">  
      <jnp-server/>  
      <jnp-connector socket-binding="jnp" rmi-socket-binding="rmi-jnp" />
      <!-- Reference to the Infinispan cache that will be used -->
      <distributed-cache cache-container="singleton" cache-ref="default" />  
    </subsystem>
    ...
    <!-- Default Infinispan configuration to show how it matches the distributed cache -->  
    <subsystem xmlns="urn:jboss:domain:infinispan:1.4">  
      <cache-container name="singleton" aliases="cluster ha-partition" default-cache="default">  
        <transport lock-timeout="60000"/>  
        <replicated-cache name="default" mode="SYNC" batching="true">  
          <locking isolation="REPEATABLE_READ"/>  
        </replicated-cache>  
      </cache-container>  
      ...  
    </subsystem>  
    ...  
    <socket-binding-group>  
      ...  
      <socket-binding name="jnp" port="5599" interface="jnp"/>  
      <socket-binding name="rmi-jnp" port="1099" interface="jnp"/>  
      ...  
    </socket-binding-group> 

#Client calls

It is possible to enable backward calls from EAP6 to EAP5. In order to do so:
    1. EAP6 has to define external-context factory
    2. This extension lib module must have all required dependencies linked
    
## External context configuration:
    <subsystem xmlns="urn:jboss:domain:naming:1.4">
        <bindings>
            <external-context name="java:global/client-context" module="org.jboss.legacy.naming.spi" class="javax.naming.InitialContext">
                <environment>
                    <property name="java.naming.provider.url" value="jnp://localhost:5599"/>
                    <property name="java.naming.factory.url.pkgs" value="org.jnp.interfaces"/>
                    <property name="java.naming.factory.initial" value="org.jboss.legacy.jnp.factory.WatchfulContextFactory"/>
                </environment>
            </external-context>
        </bindings>
        <remote-naming/>
    </subsystem>

#Build
##Simple build
Run _build.sh_  
##Build and Test
Download a zip archive of EAP 7.x (at least).   
Run _build.sh -Djbossas.eap.zip=/complicated_path/jboss-eap-7.0.0.Beta.zip  -Djbossas.eap.distro=jboss-eap-7.0  
##Build and deploy
Download and install EAP 7.x (at least). 
Define the environment variable $JBOSS_HOME pointing towards your EAP6 installation.  
Run _deploy.sh_   
Define your configuration in the EAP7 server. 
#Installation
Download the zip or tar.gz archive of the extension.  
Unarchive it in the EAP7 installation directory.  
Define your configuration in the EAP7 server. 
#Uninstallation
Remove the configuration entries from your JBoss configuration file.  
Delete the folder _$JBOSS_HOME/modules/system/layers/base/org/jboss/legacy/_
