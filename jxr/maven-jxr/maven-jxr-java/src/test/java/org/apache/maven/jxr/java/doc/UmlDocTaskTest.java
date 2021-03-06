package org.apache.maven.jxr.java.doc;

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

import java.io.File;

import org.apache.maven.jxr.util.DotUtil.DotNotPresentInPathException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.PathTool;

/**
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @version $Id$
 */
public class UmlDocTaskTest
    extends PlexusTestCase
{
    /** {@inheritDoc} */
    protected void setUp()
        throws Exception
    {
        File srcDir = new File( getBasedir(), "target/unit/src" );
        // safety
        FileUtils.deleteDirectory( srcDir );

        Project antProject = new Project();
        antProject.setBasedir( getBasedir() );

        Copy copy = new Copy();
        copy.setProject( antProject );
        copy.setTodir( srcDir );
        FileSet set = new FileSet();
        set.setDir( new File( getBasedir(), "src/main/java" ) );
        set.setIncludes( "**/*.java" );
        copy.addFileset( set );
        set = new FileSet();
        set.setDir( new File( getBasedir(), "target/generated-sources/antlr" ) );
        set.setIncludes( "**/*.java" );
        copy.addFileset( set );
        copy.execute();
    }

    /**
     * Call UMLdoc task
     *
     * @throws Exception if any.
     */
    public void testDefaultExecute()
        throws Exception
    {
        File out = new File( getBasedir(), "target/unit/umldoc-default-with-ant/umlDefault.svg" );
        File srcDir = new File( getBasedir(), "src/test/resources/javasrc" );

        Project antProject = new Project();
        antProject.setBasedir( getBasedir() );

        UmlDocTask task = new UmlDocTask();
        task.setProject( antProject );
        task.setSrcDir( srcDir );
        task.setOut( out );
        task.setDiagramEncoding( "UTF-8" );
        try
        {
            task.execute();
            assertTrue( "DOT exists in the path", true );
        }
        catch ( BuildException e )
        {
            if ( e.getException() instanceof DotNotPresentInPathException )
            {
                assertTrue( "DOT doesnt exist in the path. Ignored test", true );
                return;
            }

            throw e;
        }

        // Generated files
        assertTrue( out.exists() );
        assertTrue( out.length() > 0 );
    }

    /**
     * Call UMLdoc task
     *
     * @throws Exception if any.
     */
    public void testLinkExecute()
        throws Exception
    {
        File out = new File( getBasedir(), "target/unit/umldoc-default-with-ant/umlLink.svg" );
        File srcDir = new File( getBasedir(), "target/unit/src" );

        Project antProject = new Project();
        antProject.setBasedir( getBasedir() );

        UmlDocTask task = new UmlDocTask();
        task.setProject( antProject );
        task.setSrcDir( srcDir );
        task.setOut( out );
        // All tests passed...
        task.setJavasrcPath( PathTool.getRelativePath( "./target/unit/src" ) + "/target/unit/jxrdoc-default/" );
        try
        {
            task.execute();
            assertTrue( "DOT exists in the path", true );
        }
        catch ( BuildException e )
        {
            if ( e.getException() instanceof DotNotPresentInPathException )
            {
                assertTrue( "DOT doesnt exist in the path. Ignored test", true );
                return;
            }

            throw e;
        }

        // Generated files
        assertTrue( out.exists() );
        assertTrue( out.length() > 0 );
    }
}
