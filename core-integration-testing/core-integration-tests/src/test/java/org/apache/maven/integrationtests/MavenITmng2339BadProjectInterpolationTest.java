package org.apache.maven.integrationtests;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MavenITmng2339BadProjectInterpolationTest
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng2339BadProjectInterpolationTest()
        throws org.apache.maven.artifact.versioning.InvalidVersionSpecificationException
    {
        super( "(2.0.8,)" ); // 2.0.9+
    }

    public void testitMNG2339a()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(),
                                                                 "/mng-2339-badProjectInterpolation/a" );

        Verifier verifier;

        verifier = new Verifier( testDir.getAbsolutePath() );

        List cliOptions = new ArrayList();
        cliOptions.add( "-Dversion=foo" );
        verifier.setCliOptions( cliOptions );
        verifier.executeGoal( "validate" );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

    // test that -Dversion=1.0 is still available for interpolation.
    public void testitMNG2339b()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(),
                                                                 "/mng-2339-badProjectInterpolation/b" );

        Verifier verifier;

        verifier = new Verifier( testDir.getAbsolutePath() );

        verifier.executeGoal( "initialize" );

        assertTrue( "Touchfile using ${project.version} for ${version} does not exist.",
                    new File( testDir, "target/touch-1.txt" ).exists() );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier = new Verifier( testDir.getAbsolutePath() );

        List cliOptions = new ArrayList();
        cliOptions.add( "-Dversion=2" );
        verifier.setCliOptions( cliOptions );
        verifier.executeGoal( "initialize" );

        assertTrue( "Touchfile using CLI-specified ${version} does not exist.",
                    new File( testDir, "target/touch-2.txt" ).exists() );

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }

}
