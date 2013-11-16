package org.apache.maven.dist.tools;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 *
 * @author skygo
 */
public abstract class AbstractDistCheckMojo
    extends AbstractMavenReport
{
    private static final String CONF = "dist-tool.conf";

    private static final String EOL = System.getProperty( "line.separator" );

    /**
     * URL of repository where artifacts are stored. 
     */
    @Parameter( property = "repositoryUrl", defaultValue = "http://repo.maven.apache.org/maven2/" )
    protected String repoBaseUrl;

    /**
     * List of configuration line for specific inspection.
     */
    @Parameter( property = "configurationLines", defaultValue = "" )
    private List<String> configurationLines;

    /**
     * Site renderer.
     */
    @Component
    protected Renderer siteRenderer;

    /**
     * Reporting directory.
     */
    @Parameter( defaultValue = "${project.reporting.outputDirectory}", required = true )
    protected File outputDirectory;

    /**
     * Maven project.
     */
    @Component
    protected MavenProject project;

    @Parameter( defaultValue = "${project.build.directory}/dist-tool" )
    protected File failuresDirectory;

    /**
     * list of artifacts repositories.
     */
    protected List<ArtifactRepository> artifactRepositories = new LinkedList<>();

    protected String distributionAreaUrl;

    /**
     * Path in index page mapping, when path is not the classical /artifact-id/ 
     * The configuration in <code>dist-tool.conf</code> looks like this:
     * <pre>artifact-id index-path = /directory/</pre>
     */
    protected Map<String, String> paths = new HashMap<String, String>();

    /**
     * is it index page check mojo?
     * necessary to only check index page information for plugins marked with asterisk * in db,
     * because they are released as part of a global component (archetype, scm, release, ...)
     */
    abstract boolean isIndexPageCheck();
    
    protected abstract void checkArtifact( ConfigurationLineInfo request, String repoBase )
        throws MojoExecutionException;

    protected abstract String getFailuresFilename();

    @Override
    public String getOutputName()
    {
        return "dist-tool-" + getFailuresFilename().replace( ".old", "" );
    }

    @Override
    protected String getOutputDirectory()
    {
        return outputDirectory.getAbsolutePath();
    }

    @Override
    protected Renderer getSiteRenderer()
    {
        return siteRenderer;
    }

    @Override
    protected MavenProject getProject()
    {
        return project;
    }

    private void loadConfiguration()
        throws MojoExecutionException
    {
        URL configuration = Thread.currentThread().getContextClassLoader().getResource( CONF );
        try ( BufferedReader in = new BufferedReader( new InputStreamReader( configuration.openStream() ) ) )
        {
            String text;
            while ( ( text = in.readLine() ) != null )
            {
                configurationLines.add( text );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "error while reading " + configuration, e );
        }
    }

    @Override
    public void execute()
        throws MojoExecutionException
    {
        ArtifactRepository aa =
            new MavenArtifactRepository( "central", repoBaseUrl, new DefaultRepositoryLayout(),
                                         new ArtifactRepositoryPolicy( false,
                                                                       ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS,
                                                                       ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ),
                                         new ArtifactRepositoryPolicy( true,
                                                                       ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS,
                                                                       ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ) );
        artifactRepositories.add( aa );

        if ( configurationLines.isEmpty() )
        {
            loadConfiguration();
        }

        File failures = getFailuresFile();
        if ( failures.exists() )
        {
            failures.delete();
        }
        else
        {
            failuresDirectory.mkdirs();
        }

        ConfigurationLineInfo currentGroup = null;
        for ( String line : configurationLines )
        {
            ConfigurationLineInfo aLine = null;

            if ( "".equals( line ) || line.startsWith( "##" ) )
            {
                continue;
            }
            else if ( line.contains( "=" ) )
            {
                int index = line.indexOf( '=' );
                String param = line.substring( 0, index ).trim();
                String value = line.substring( index + 1 ).trim();

                if ( "dist-area".equals( param ) )
                {
                    distributionAreaUrl = value;
                }
                else if ( param.contains( " " ) )
                {
                    index = param.indexOf( ' ' );
                    String artifactId = param.substring( 0, index );
                    param = param.substring( index ).trim();

                    if ( "index-path".equals( param ) )
                    {
                        paths.put( artifactId, value );
                    }
                    else
                    {
                        throw new MojoExecutionException( "unknown parameter '" + param + "' in configuration line: "
                            + line );
                    }
                }
                else
                {
                    throw new MojoExecutionException( "unparseable configuration line: " + line );
                }

                continue;
            }
            else if ( line.startsWith( "/" ) )
            {
                currentGroup = new ConfigurationLineInfo( line.split( " " ) );
                if ( currentGroup.getArtifactId() == null )
                {
                    continue;
                }
                // check group's parent pom artifact
                aLine = currentGroup;
            }
            else
            {
                line = line.trim();

                if ( line.startsWith( "*" ) )
                {
                    // special artifact
                    if ( !isIndexPageCheck() )
                    {
                        // not check-index-page mojo, so ignore this artifact
                        continue;
                    }

                    // remove the asterisk before running the check
                    line = line.substring( 1 ).trim();
                }

                try
                {
                    aLine = new ConfigurationLineInfo( currentGroup, line.split( " " ) );

                }
                catch ( InvalidVersionSpecificationException e )
                {
                    throw new MojoExecutionException( e.getMessage() );
                }
            }

            checkArtifact( aLine, getVersion( aLine ) );
        }
    }

    private String getVersion( ConfigurationLineInfo aLine )
        throws MojoExecutionException
    {
        String metadataUrl = aLine.getMetadataFileURL( repoBaseUrl );
        try ( InputStream input = new BufferedInputStream( new URL( metadataUrl ).openStream() ) )
        {
            MetadataXpp3Reader metadataReader = new MetadataXpp3Reader();
            Metadata metadata = metadataReader.read( input );

            aLine.setMetadata( metadata );

            String version;
            if ( aLine.getVersionRange() != null )
            {
                if ( aLine.getVersionRange().hasRestrictions() )
                {
                    List<ArtifactVersion> artifactVersions = new ArrayList<>();
                    for ( String versioningVersion : metadata.getVersioning().getVersions() )
                    {
                        artifactVersions.add( new DefaultArtifactVersion( versioningVersion ) );
                    }
                    version = aLine.getVersionRange().matchVersion( artifactVersions ).toString();
                }
                else
                {
                    version = aLine.getVersionRange().getRecommendedVersion().toString();
                }
                aLine.setForceVersion( version );
            }
            else
            {
                version = metadata.getVersioning().getLatest();
            }
            
            if ( getLog().isDebugEnabled() )
            {
                getLog().debug( "Checking information for artifact: " + aLine.getGroupId() + ":"
                                    + aLine.getArtifactId() + ":" + version );
                // revert sort versions (not handling alpha and
                // complex version schemes but more useful versions are displayed left side)
                Collections.sort( metadata.getVersioning().getVersions(), Collections.reverseOrder() );
                getLog().debug( metadata.getVersioning().getVersions() + " version(s) detected " + repoBaseUrl );
            }

            if ( aLine.getForcedVersion() != null )
            {
                if ( aLine.getVersionRange().hasRestrictions() )
                {
                    getLog().debug( aLine.getGroupId() + ":" + aLine.getArtifactId()
                                        + " metadata latest version value is " + metadata.getVersioning().getLatest()
                                        + " but check was restricted to " + aLine.getVersionRange()
                                        + " which selected " + aLine.getForcedVersion() );
                }
                else
                {
                    getLog().info( aLine.getGroupId() + ":" + aLine.getArtifactId()
                                   + " metadata latest version value is " + metadata.getVersioning().getLatest()
                                   + " but check was manually set to " + aLine.getForcedVersion() );
                }
            }
           
            return version;
        }
        catch ( IOException | XmlPullParserException ex )
        {
            throw new MojoExecutionException( "error while reading " + metadataUrl, ex );
        }
    }

    /**
     * add an error icon.
     *
     * @param sink doxiasink
     */
    protected void iconError( Sink sink )
    {
        icon( sink, "icon_error_sml" );
    }
    
    /**
     * add a warning icon.
     *
     * @param sink doxiasink
     */
    protected void iconWarning( Sink sink )
    {
        icon( sink, "icon_warning_sml" );
    }
    
    /**
     * add an success icon.
     *
     * @param sink doxiasink
     */
    protected void iconSuccess( Sink sink )
    {
        icon( sink, "icon_success_sml" );
    }

    /**
     * add a "remove" icon.
     *
     * @param sink doxiasink
     */
    protected void iconRemove( Sink sink )
    {
        icon( sink, "remove" );
    }

    private void icon( Sink sink, String level )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( level );
        sink.figureCaption_();
        sink.figureGraphics( "images/" + level + ".gif" );
        sink.figure_();
    }
    
    /**
     * Log and add Error line to logs.txt if not configured to ignore the artifact+version
     * 
     * @param cli
     * @param version
     * @param ignore
     * @param error 
     */
    protected void addErrorLine( ConfigurationLineInfo cli, String version, List<String> ignore, String error ) 
    {
        if ( ( ignore != null )
            && ( ignore.contains( cli.getArtifactId() + ':' + version ) || ignore.contains( cli.getArtifactId() ) ) )
        {
            getLog().warn( error );
        }
        else
        {
            getLog().error( error );

            try ( PrintWriter output = new PrintWriter( new FileWriter( getFailuresFile(), true ) ) )
            {
                output.printf( "%s%s", error, EOL );
            }
            catch ( Exception e )
            {
                getLog().error( "Cannot append to " + getFailuresFilename() );
            }
        }
    }

    private File getFailuresFile()
    {
        return new File( failuresDirectory, getFailuresFilename() );
    }
}
