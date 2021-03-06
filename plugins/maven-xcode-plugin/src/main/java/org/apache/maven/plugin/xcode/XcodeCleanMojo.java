package org.apache.maven.plugin.xcode;

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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Removes all existing XCode files for the project.
 *
 * @goal clean
 */
public class XcodeCleanMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        File projectDir = new File(project.getBasedir(),
                project.getArtifactId() + ".xcodeproj");
        if (projectDir.isDirectory()) {
            File[] files = projectDir.listFiles();
            boolean allDeleted = true;
            for (int i = 0; i < files.length; i++) {
                allDeleted &= files[i].delete();
            }
            if (allDeleted) {
                projectDir.delete();
            }
        }
    }
}
