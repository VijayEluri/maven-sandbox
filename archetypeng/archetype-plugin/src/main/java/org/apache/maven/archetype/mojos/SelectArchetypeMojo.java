/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.maven.archetype.mojos;

import org.apache.maven.archetype.common.ArchetypeRegistryManager;
import org.apache.maven.archetype.generator.ArchetypeSelector;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.ContextEnabled;
import org.apache.maven.settings.Settings;

import java.io.File;

import java.util.List;

/**
 * Select the archetype to be used.
 *
 * @author rafale
 * @requiresProject false
 * @goal select-archetype
 */
public class SelectArchetypeMojo
    extends AbstractMojo
{
    /** @component */
    ArchetypeRegistryManager archetypeRegistryManager;

    /**
     * The archetype's artifactId.
     *
     * @parameter expression="${archetypeArtifactId}"
     */
    private String archetypeArtifactId;

    /**
     * The archetype's groupId.
     *
     * @parameter expression="${archetypeGroupId}"
     */
    private String archetypeGroupId;

    /**
     * The location of the registry file.
     *
     * @parameter expression="${user.home}/.m2/archetype.xml"
     */
    private File archetypeRegistryFile;

    /**
     * The archetype's version.
     *
     * @parameter expression="${archetypeVersion}"
     */
    private String archetypeVersion;

    /**
     * Local maven repository.
     *
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * Remote repositories defined in the project's pom (used only when called from an existing
     * project).
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     */
    private List pomRemoteRepositories;

    /**
     * The property file that holds the plugin configuration.
     *
     * @parameter default-value="archetype.properties" expression="${archetype.properties}"
     */
    private File propertyFile = null;

    /**
     * Other remote repositories available for discovering dependencies and extensions.
     *
     * @parameter expression="${remoteRepositories}"
     */
    private String remoteRepositories;

    /** @component */
    private ArchetypeSelector selector;

    /**
     * User settings use to check the interactiveMode.
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    public void execute()
        throws
        MojoExecutionException,
        MojoFailureException
    {
        try
        {
            List repositories =
                archetypeRegistryManager.getRepositories(
                    pomRemoteRepositories,
                    remoteRepositories,
                    archetypeRegistryFile
                );

            selector.selectArchetype(
                archetypeGroupId,
                archetypeArtifactId,
                archetypeVersion,
                settings.getInteractiveMode(),
                propertyFile,
                archetypeRegistryFile,
                localRepository,
                repositories
            );
        }
        catch ( Exception ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
    }
}
