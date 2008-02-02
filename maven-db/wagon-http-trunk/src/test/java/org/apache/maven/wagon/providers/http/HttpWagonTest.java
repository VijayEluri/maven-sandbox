package org.apache.maven.wagon.providers.http;

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

import org.apache.maven.wagon.FileTestUtils;
import org.apache.maven.wagon.WagonTestCase;
import org.codehaus.plexus.jetty.Httpd;

import java.io.File;

/**
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * @version $Id$
 */
public class HttpWagonTest
    extends WagonTestCase
{
    private Httpd httpd;

    protected String getProtocol()
    {
        return "http";
    }

    protected String getTestRepositoryUrl()
    {
        return "http://localhost:10007";
    }

    protected void setupWagonTestingFixtures()
        throws Exception
    {
        // For a PUT the artifact must exist already which is how a PUT works by
        // default so we must place a dummy artifact in the http repo first before
        // the actual PUT operation.

        // File round trip testing
        
        File file = FileTestUtils.createUniqueFile( "local-repository", "test-resource.txt" );

        file.delete();

        file.getParentFile().mkdirs();

        FileTestUtils.generateFile( file.getAbsolutePath(), "file-dummy" );

        // For a PUT the artifact must exist already which is how a PUT works by
        // default so we must place a dummy artifact in the http repo first before
        // the actual PUT operation.

        File f = new File( FileTestUtils.createDir( "http-repository" ), "test-resource.txt" );

        f.delete();

        f.getParentFile().mkdirs();

        FileTestUtils.generateFile( f.getAbsolutePath(), "artifact-dummy" );

        httpd = (Httpd) lookup( Httpd.ROLE );
    }

    public void testWagonGetFileList()
        throws Exception
    {
        File f = new File( FileTestUtils.createDir( "http-repository" ), "file-list" );
        f.mkdirs();

        super.testWagonGetFileList();
    }

    protected void tearDownWagonTestingFixtures()
        throws Exception
    {
        release( httpd );
    }
}
