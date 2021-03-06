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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import com.sun.tools.javadoc.Main;

/**
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @version $Id$
 */
public class XMLDocletTest
    extends PlexusTestCase
{
    private static final String DEFAULT_EXCLUDES = "**/*~,**/#*#,**/.#*,**/%*%,**/._*,**/CVS,**/CVS/**,"
        + "**/.cvsignore,**/SCCS,**/SCCS/**,**/vssver.scc,**/.svn,**/.svn/**,**/.DS_Store";

    /**
     * Call Javadoc tool with XML doclet.
     *
     * @throws Exception if any
     */
    public void testDefaultExecute()
        throws Exception
    {
        File srcDir = new File( getBasedir(), "src/test/resources/javasrc" );

        File outputXML = new File( getBasedir(), "target/unit/xmldoclet-default/javadoc.xml" );

        // test phase is after compile phase, so we are sure that classes dir exists
        List args = new LinkedList();
        args.add( "-package" );
        args.add( "-sourcepath" );
        args.add( srcDir.getAbsolutePath() );
        args.add( "-xmlencoding" );
        args.add( "UTF-8" );
        args.add( "-o" );
        args.add( outputXML.getAbsolutePath() );

        addPackages( args, srcDir );

        StringWriter err = new StringWriter();
        StringWriter warn = new StringWriter();
        StringWriter notice = new StringWriter();
        int exit = Main.execute( "javadoc", new PrintWriter( err ), new PrintWriter( warn ), new PrintWriter( notice ),
                                 XMLDoclet.class.getName(), (String[]) args.toArray( new String[0] ) );

        assertEquals( err.toString(), exit, 0 );

        // Generated files
        assertTrue( outputXML.exists() );
        assertTrue( outputXML.length() > 0 );
        String content = IOUtil.toString( new FileInputStream( outputXML ) );
        assertTrue( content.indexOf( "\"UTF-8\"" ) != -1 );
        File dtd = new File( getBasedir(), "target/unit/xmldoclet-default/" + XMLDoclet.XMLDOCLET_DTD );
        assertTrue( dtd.exists() );
        assertTrue( dtd.length() > 0 );
    }

    /**
     * Call Javadoc tool with XML doclet without out option.
     *
     * @throws Exception if any
     */
    public void testMissingOptionsExecute()
        throws Exception
    {
        File srcDir = new File( getBasedir(), "src/test/resources/javasrc" );

        // test phase is after compile phase, so we are sure that classes dir exists
        List args = new LinkedList();
        args.add( "-package" );
        args.add( "-sourcepath" );
        args.add( srcDir.getAbsolutePath() );

        addPackages( args, srcDir );

        StringWriter err = new StringWriter();
        StringWriter warn = new StringWriter();
        StringWriter notice = new StringWriter();
        int exit = Main.execute( "javadoc", new PrintWriter( err ), new PrintWriter( warn ), new PrintWriter( notice ),
                                 XMLDoclet.class.getName(), (String[]) args.toArray( new String[0] ) );

        assertEquals( err.toString(), exit, 1 );
        assertTrue( err.toString().indexOf( XMLDoclet.USAGE ) != -1 );
    }

    /**
     * Call Javadoc tool with XML doclet and non mandatory option.
     *
     * @throws Exception if any
     */
    public void testNonMandatoryOptionExecute()
        throws Exception
    {
        File srcDir = new File( getBasedir(), "src/test/resources/javasrc" );

        File outputXML = new File( getBasedir(), "target/unit/xmldoclet-default/javadoc.xml" );

        // test phase is after compile phase, so we are sure that classes dir exists
        List args = new LinkedList();
        args.add( "-package" );
        args.add( "-sourcepath" );
        args.add( srcDir.getAbsolutePath() );
        args.add( "-o" );
        args.add( outputXML.getAbsolutePath() );

        addPackages( args, srcDir );

        StringWriter err = new StringWriter();
        StringWriter warn = new StringWriter();
        StringWriter notice = new StringWriter();
        int exit = Main.execute( "javadoc", new PrintWriter( err ), new PrintWriter( warn ), new PrintWriter( notice ),
                                 XMLDoclet.class.getName(), (String[]) args.toArray( new String[0] ) );

        assertEquals( err.toString(), exit, 0 );

        // Generated files
        assertTrue( outputXML.exists() );
        assertTrue( outputXML.length() > 0 );
        String content = IOUtil.toString( new FileInputStream( outputXML ) );
        assertTrue( content.indexOf( "\"" + XMLDoclet.DEFAULT_ENCODING_FORMAT + "\"" ) != -1 );
        File dtd = new File( getBasedir(), "target/unit/xmldoclet-default/" + XMLDoclet.XMLDOCLET_DTD );
        assertTrue( dtd.exists() );
        assertTrue( dtd.length() > 0 );
    }

    private void addPackages( List args, File srcDir )
        throws IOException
    {
        List packages = FileUtils.getDirectoryNames( srcDir, null, DEFAULT_EXCLUDES, false );
        for ( Iterator it = packages.iterator(); it.hasNext(); )
        {
            String p = (String) it.next();

            if ( StringUtils.isEmpty( p ) )
            {
                continue;
            }

            if ( FileUtils.getFileNames( new File( srcDir, p ), "*.java", "", false ).isEmpty() )
            {
                continue;
            }

            args.add( StringUtils.replace( p, File.separator, "." ) );
        }
    }
}
