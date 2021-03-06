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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.xcode.stubs.TestCounter;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;

public class XcodeTest
    extends AbstractMojoTestCase
{
    public void testXcode()
        throws Exception
    {
        File pluginXmlFile = new File( getBasedir(), "src/test/xcode-plugin-configs/min-plugin-config.xml" );

        Mojo mojo = lookupMojo( "xcode", pluginXmlFile );

        mojo.execute();

        File basedir = new File( getBasedir(),  "target/test-harness/" + TestCounter.currentCount() );

        String artifactId = "plugin-test-" + TestCounter.currentCount();

        File projectDir = new File( basedir, artifactId + ".xcodeproj" );
        assertTrue( "Test creation of xcodeproj dir", projectDir.exists() );

        File projectFile = new File( projectDir, "project.pbxproj" );
        assertTrue( "Test creation of project.pbxproj", projectFile.exists() );
    }

    public void testXcodeWithMacro()
        throws Exception
    {
        File pluginXmlFile = new File( getBasedir(), "src/test/xcode-plugin-configs/macro-plugin-config.xml" );

        Mojo mojo = lookupMojo( "xcode", pluginXmlFile );

        mojo.execute();

        int testCounter = TestCounter.currentCount();

        File basedir = new File( getBasedir(), "target/test-harness/" + TestCounter.currentCount() );

        String artifactId = "plugin-test-" + testCounter;

        File projectDir = new File( basedir, artifactId + ".xcodeproj" );
        assertTrue( "Test creation of xcodeproj dir", projectDir.exists() );

        File projectFile = new File( projectDir, "project.pbxproj" );
        assertTrue( "Test creation of project.pbxproj", projectFile.exists() );
    }
}
