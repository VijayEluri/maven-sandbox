package org.apache.maven.surefire.testng;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.suite.AbstractDirectoryTestSuite;
import org.apache.maven.surefire.testset.SurefireTestSet;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.*;

/**
 * Test suite for TestNG based on a directory of Java test classes. Can also execute JUnit tests.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 */
public class TestNGDirectoryTestSuite
    extends AbstractDirectoryTestSuite
{
    private String groups;

    private String excludedGroups;

    private String parallel;

    private int threadCount;

    private String testSourceDirectory;
    
    private Properties config;
    
    public TestNGDirectoryTestSuite( File basedir, ArrayList includes, ArrayList excludes, Properties config)
    {
        super( basedir, includes, excludes );
        
        this.config = config;
    }
    
    public TestNGDirectoryTestSuite( File basedir, ArrayList includes, ArrayList excludes, String groups,
                                     String excludedGroups, String parallel, Integer threadCount,
                                     String testSourceDirectory )
    {
        super( basedir, includes, excludes );

        this.groups = groups;

        this.excludedGroups = excludedGroups;

        this.parallel = parallel;

        this.threadCount = threadCount.intValue();

        this.testSourceDirectory = testSourceDirectory;

    }

    public TestNGDirectoryTestSuite( File basedir, ArrayList includes, ArrayList excludes, String groups,
                                     String excludedGroups, Boolean parallel, Integer threadCount,
                                     String testSourceDirectory )
    {
        super( basedir, includes, excludes );

        this.groups = groups;

        this.excludedGroups = excludedGroups;

        this.parallel = parallel.toString();

        this.threadCount = threadCount.intValue();

        this.testSourceDirectory = testSourceDirectory;

    }

    public Map locateTestSets( ClassLoader classLoader )
        throws TestSetFailedException
    {
        // TODO: fix
        // override classloader. That keeps us all together for now, which makes it work, but could pose problems of
        // classloader separation if the tests use plexus-utils.
        
        return super.locateTestSets( classLoader );
    }

    protected SurefireTestSet createTestSet( Class testClass, ClassLoader classLoader )
    {
        return new TestNGTestSet( testClass );
    }

    public void execute( String testSetName, ReporterManager reporterManager, ClassLoader classLoader )
        throws ReporterException, TestSetFailedException
    {
        if ( testSets == null )
        {
            throw new IllegalStateException( "You must call locateTestSets before calling execute" );
        }
        SurefireTestSet testSet = (SurefireTestSet) testSets.get( testSetName );

        if ( testSet == null )
        {
            throw new TestSetFailedException( "Unable to find test set '" + testSetName + "' in suite" );
        }

        if ( config != null )
        {
            config.put("classloader", classLoader);
            config.put("testclass", testSet.getTestClass() );
            config.put("suitename", "Single Test");
            config.put("testname", testSet.getName() );

            TestNGExecutor.executeTestNG( this, config, reporterManager );

            config.remove("classloader");
            config.remove("testclass");
            config.remove("suitename");
            config.remove("testname");
            return;
        }
        
        XmlSuite suite = new XmlSuite();
        
        suite.setThreadCount(threadCount);
        
        // have to invoke via reflection because TestNG version 5.5 broke things
        
        try {
            
            TestNGExecutor.execute(suite, "setParallel", parallel);

        } catch (Throwable t) {
            throw new RuntimeException("Failed to configure TestNG properly", t);
        }
        
        createXmlTest( suite, testSet );

        executeTestNG( suite, reporterManager, classLoader );
    }

    public void execute( ReporterManager reporterManager, ClassLoader classLoader )
        throws ReporterException, TestSetFailedException
    {
        if ( testSets == null )
        {
            throw new IllegalStateException( "You must call locateTestSets before calling execute" );
        }

        if ( config != null )
        {
            StringBuffer classNames = new StringBuffer();
            for ( Iterator i = testSets.values().iterator(); i.hasNext(); )
            {
                SurefireTestSet testSet = (SurefireTestSet) i.next();

                classNames.append(testSet.getTestClass().getName());

                if (i.hasNext())
                {
                    classNames.append(",");
                }
            }

            config.put("classloader", classLoader);
            config.put("testclass", classNames.toString() );
            config.put("suitename", "Directory Suite");
            config.put("testname", "Directory tests");

            TestNGExecutor.executeTestNG( this, config, reporterManager );
            
            config.remove("classloader");
            config.remove("testclass");
            config.remove("suitename");
            config.remove("testname");

            return;
        }

        XmlSuite suite = new XmlSuite();
        
        suite.setThreadCount(threadCount);
        
        // have to invoke via reflection because TestNG version 5.5 broke things
        
        try {
            
            TestNGExecutor.execute(suite, "setParallel", parallel);

        } catch (Throwable t) {

            throw new RuntimeException("Failed to configure TestNG properly", t);
        }

        for ( Iterator i = testSets.values().iterator(); i.hasNext(); )
        {
            SurefireTestSet testSet = (SurefireTestSet) i.next();

            createXmlTest( suite, testSet );
        }

        executeTestNG( suite, reporterManager, classLoader );
    }

    private void createXmlTest( XmlSuite suite, SurefireTestSet testSet )
    {
        XmlTest xmlTest = new XmlTest( suite );
        xmlTest.setName( testSet.getName() );
        xmlTest.setXmlClasses( Collections.singletonList( new XmlClass( testSet.getTestClass() ) ) );

        if ( groups != null )
        {
            xmlTest.setIncludedGroups( Arrays.asList( groups.split( "," ) ) );
        }
        if ( excludedGroups != null )
        {
            xmlTest.setExcludedGroups( Arrays.asList( excludedGroups.split( "," ) ) );
        }

        try
        {
            Class junitClass = Class.forName( "junit.framework.Test" );
            Class junitBase = Class.forName( "junit.framework.TestCase" );

            if ( junitClass.isAssignableFrom( testSet.getTestClass() ) ||
                junitBase.isAssignableFrom( testSet.getTestClass() ) )
            {
                xmlTest.setJUnit( true );
            }

        }
        catch ( ClassNotFoundException e )
        {
        }
    }

    private void executeTestNG( XmlSuite suite, ReporterManager reporterManager, ClassLoader classLoader )
    {
        TestNG testNG = new TestNG( false );

        // turn off all TestNG output
        testNG.setVerbose( 0 );

        testNG.setXmlSuites( Collections.singletonList( suite ) );

        testNG.setListenerClasses( new ArrayList() );
        
        TestNGReporter reporter = new TestNGReporter( reporterManager, this );
        testNG.addListener( (ITestListener) reporter );
        testNG.addListener( (ISuiteListener) reporter );

        // Set source path so testng can find javadoc annotations if not in 1.5 jvm
        if ( testSourceDirectory != null )
        {
            testNG.setSourcePath( testSourceDirectory );
        }
        
        // workaround for SUREFIRE-49
        // TestNG always creates an output directory, and if not set the name for the directory is "null"
        testNG.setOutputDirectory( System.getProperty( "java.io.tmpdir" ) );

        TestNGExecutor.configureJreType(testNG, testSourceDirectory);

        testNG.runSuitesLocally();
        
        // need to execute report end after testng has completely finished as the
        // reporter methods don't get called in the order that would allow for capturing
        // failures that happen in before/after suite configuration methods
        
        reporter.cleanupAfterTestsRun();
    }
}
