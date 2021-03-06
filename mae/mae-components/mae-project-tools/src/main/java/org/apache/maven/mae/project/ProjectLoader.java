/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.maven.mae.project;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.maven.mae.project.key.FullProjectKey;
import org.apache.maven.mae.project.session.ProjectToolsSession;
import org.apache.maven.project.MavenProject;

public interface ProjectLoader
{

    List<MavenProject> buildReactorProjectInstances( final ProjectToolsSession session, final boolean recursive,
                                                     final File... rootPoms )
        throws ProjectToolsException;

    MavenProject buildProjectInstance( final File pomFile, final ProjectToolsSession session )
        throws ProjectToolsException;

    MavenProject buildProjectInstance( final String groupId, final String artifactId, final String version,
                                       final ProjectToolsSession session )
        throws ProjectToolsException;

    MavenProject buildProjectInstance( final FullProjectKey key, final ProjectToolsSession session )
        throws ProjectToolsException;

    Set<String> retrieveReactorProjectIds( final File rootPom )
        throws ProjectToolsException;

}
