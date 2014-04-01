<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2014 Red Hat, inc., and individual contributors
as indicated by the @author tags. See the copyright.txt file in the
distribution for a full listing of individual contributors.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
-->

<!--
    Description: The aim is to enable the JNP extension with the matching socket-bindings.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:jnp="urn:jboss:domain:legacy-jnp:1.0"
                xmlns:domain="urn:jboss:domain:1.5"
                exclude-result-prefixes="domain jnp">
    
    <xsl:variable name="nsMessagingInf" select="'urn:jboss:domain:messaging:'"/>
    <xsl:variable name="nsDomainInf" select="'urn:jboss:domain:'"/>

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:param name="jnpPort" select="'5599'"/>
    <xsl:param name="rmijnpPort" select="'1099'"/>

    <xsl:template match="node()|@*" exclude-result-prefixes="yes">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="//*[local-name()='extensions' and starts-with(namespace-uri(), $nsDomainInf)]">
        <xsl:copy>
            <xsl:apply-templates />
            <xsl:choose>
                <xsl:when test="//*[local-name()='extensions']/*[local-name()='extension']/@module='org.jboss.legacy.jnp'">
                </xsl:when>
                <xsl:otherwise>
                    <extension module="org.jboss.legacy.jnp" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="node() | @*" name="copy">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>


    <xsl:template match="//*[local-name()='subsystem'][last()]">
        <xsl:call-template name="copy" />
        <xsl:choose>
            <xsl:when test="//jnp:subsystem">
            </xsl:when>
            <xsl:otherwise>
                <subsystem xmlns="urn:jboss:domain:legacy-jnp:1.0">
                    <jnp-server/>
                    <jnp-connector socket-binding="jnp" rmi-socket-binding="rmi-jnp" />
                </subsystem>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="//*[local-name()='socket-binding-group']/*[local-name()='socket-binding'][last()]">
        <xsl:call-template name="copy" />
        <xsl:choose>
            <xsl:when test="//*[local-name()='socket-binding-group']/*[local-name()='socket-binding']/@name='jnp'">
            </xsl:when>
            <xsl:when test="//*[local-name()='socket-binding-group']/*[local-name()='socket-binding']/@name='rmi-jnp'">
            </xsl:when>
            <xsl:otherwise>
                <socket-binding>
                    <xsl:attribute name="name">jnp</xsl:attribute>
                    <xsl:attribute name="port">
                        <xsl:value-of select="$jnpPort" />
                    </xsl:attribute>
                    <xsl:attribute name="interface">public</xsl:attribute>
                </socket-binding>
                <socket-binding>
                    <xsl:attribute name="name">rmi-jnp</xsl:attribute>
                    <xsl:attribute name="port">
                        <xsl:value-of select="$rmijnpPort" />
                    </xsl:attribute>
                    <xsl:attribute name="interface">public</xsl:attribute>
                </socket-binding>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

     <xsl:template match="//*[local-name()='subsystem' and starts-with(namespace-uri(), $nsMessagingInf)]
     					  /*[local-name()='hornetq-server']/*[local-name()='jms-connection-factories']">
        <xsl:call-template name="copy" />
        <xsl:choose>
            <xsl:when test="//*[local-name()='jms-destinations']">
            </xsl:when>
            <xsl:otherwise>
                <jms-destinations>
                    <jms-queue name="eap6Queue">
                        <entry name="jms/queue/eap6Queue"/>
                    </jms-queue>
                    <jms-queue name="eap6ReplyQueue">
                        <entry name="jms/queue/eap6ReplyQueue"/>
                    </jms-queue>
                </jms-destinations>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
