package org.apache.maven.plugins.patchtracker;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.patchtracker.tracking.PatchTrackerRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Goal which create a diff/patch file from the current project and create an issue in the project
 * with attaching the created patch file
 *
 * @goal create
 * @aggregator
 */
public class CreatePatchMojo
    extends AbstractMojo
{
    /**
     * The Maven Project Object.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @parameter default-value="${basedir}"
     * @required
     * @readonly
     */
    protected File baseDir;

    /**
     * @component
     */
    protected ScmManager scmManager;

    /**
     * @parameter expression="${scm.providerType}" default-value=""
     */
    protected String providerType = "";

    /**
     * @parameter default-value="${settings}"
     * @required
     * @readonly
     */
    protected Settings settings;


    /**
     * if user/password are stored in your settings.xml in a server
     *
     * @parameter expression="${patch.serverId}" default-value=""
     */
    protected String serverId;

    /**
     * if path tracker url is not stored in the pom.
     * <b>For jira, url must include project key!: http://jira.codehaus.org/browse/MNG</b>
     *
     * @parameter expression="${patch.serverUrl}" default-value=""
     */
    protected String serverUrl;

    /**
     * @parameter expression="${patch.user}" default-value=""
     */
    protected String user;

    /**
     * @parameter expression="${patch.password}" default-value=""
     */
    protected String password;

    /**
     * @parameter expression="${patch.system}" default-value=""
     */
    protected String system;

    /**
     * @parameter expression="${patch.summary}" default-value=""
     */
    protected String summary;

    /**
     * @parameter expression="${patch.description}" default-value=""
     */
    protected String description;

    /**
     * Component used to prompt for input.
     *
     * @component
     */
    private Prompter prompter;

    public void execute()
        throws MojoExecutionException
    {
        // TODO do a status before and complains if some files in to be added status ?

        String patchContent = getPatchContent();

        PatchTrackerRequest patchTrackerRequest = buidPatchTrackerRequest();

        patchTrackerRequest.setPatchContent( patchContent );

        getLog().debug( patchTrackerRequest.toString() );

    }


    protected String getPatchContent()
        throws MojoExecutionException
    {
        try
        {
            ScmRepository scmRepository = scmManager.makeScmRepository( project.getScm().getConnection() );

            ScmProvider provider = scmManager.getProviderByType( scmRepository.getProvider() );

            getLog().debug( "scm.providerType:" + providerType );
            if ( StringUtils.isNotEmpty( providerType ) )
            {
                provider = scmManager.getProviderByType( providerType );
            }

            DiffScmResult diffScmResult = provider.diff( scmRepository, new ScmFileSet( baseDir ), "", "" );
            getLog().debug( diffScmResult.getPatch() );

            return diffScmResult.getPatch();

        }
        catch ( ScmRepositoryException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( NoSuchScmProviderException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( ScmException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    protected PatchTrackerRequest buidPatchTrackerRequest()
        throws MojoExecutionException
    {
        try
        {
            return new PatchTrackerRequest().setUrl( getPatchTrackerUrl() ).setUserName(
                getPatchTrackerUsername() ).setPassword( getPatchTrackerPassword() ).setSummary(
                getPatchTrackerSummary() ).setDescription( getPatchTrackerDescription() );
        }
        catch ( PrompterException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }


    protected String getPatchTrackerUrl()
        throws PrompterException, MojoExecutionException
    {
        String value = project.getIssueManagement() == null ? "" : project.getIssueManagement().getUrl();

        // cli must win !
        if ( StringUtils.isNotEmpty( serverUrl ) )
        {
            value = serverUrl;
        }

        return getValue( value, "path tracker url ? (http://jira.codehaus.org/browse/MNG)", null, true,
                         "you must configure a patch system or at least use interactive mode", value );
    }

    protected String getPatchTrackerSummary()
        throws PrompterException, MojoExecutionException
    {
        String value = summary;

        return getValue( value, "patch summary ? (wonderfull patch to fix ....) ", Collections.<String>emptyList(),
                         true, "you must configure a patch summary or at least use interactive mode", null );
    }

    protected String getPatchTrackerDescription()
        throws PrompterException, MojoExecutionException
    {
        String value = description;

        return getValue( value, "patch description ?(this patch fix this very annoying issue ....) ", null, false,
                         "you must configure a patch summary or at least use interactive mode", null );
    }

    protected String getPatchTrackerUsername()
        throws PrompterException, MojoExecutionException
    {
        String value = null;

        if ( StringUtils.isNotEmpty( serverId ) )
        {
            Server server = getServer( serverId );
            if ( server == null )
            {
                getLog().warn( "no server found in your settings.xml with id:" + serverId );
            }
            else
            {
                value = server.getUsername();
            }

        }

        // cli must win !
        if ( StringUtils.isNotEmpty( user ) )
        {
            value = user;
        }

        return getValue( value, "patch tracker username ?", null, true,
                         "you must configure a user for your patch tracker or at least use interactive mode", value );
    }

    protected String getPatchTrackerPassword()
        throws PrompterException, MojoExecutionException
    {
        String value = null;

        if ( StringUtils.isNotEmpty( serverId ) )
        {
            Server server = getServer( serverId );
            if ( server == null )
            {
                getLog().warn( "no server found in your settings.xml with id:" + serverId );
            }
            else
            {
                value = server.getPassword();
            }

        }

        // cli must win !
        if ( StringUtils.isNotEmpty( password ) )
        {
            value = password;
        }

        return getValue( value, "patch tracker password ?", null, true,
                         "you must configure a password for your patch tracker or at least use interactive mode",
                         value );
    }

    protected String getPatchTrackerSystem()
        throws PrompterException, MojoExecutionException
    {
        String value = project.getIssueManagement() == null ? "" : project.getIssueManagement().getSystem();

        // cli must win !
        if ( StringUtils.isNotEmpty( system ) )
        {
            value = system;
        }

        return getValue( value, "path tracker system id ?", Arrays.asList( "jira" ), true,
                         "you must configure a patch system or at least use interactive mode", "jira" );
    }

    protected String getValue( String currentValue, String message, List<String> possibleValues, boolean mandatory,
                               String errorMessage, String defaultValue )
        throws PrompterException, MojoExecutionException
    {
        boolean loadFromPrompt = false;
        String value = currentValue;
        if ( mandatory && StringUtils.isEmpty( value ) )
        {
            if ( settings.isInteractiveMode() )
            {

                getLog().debug( "1st prompt message " + message + ", defaultValue " + defaultValue + ", possibleValues"
                                    + possibleValues );
                value = ( possibleValues == null || possibleValues.isEmpty() )
                    ? prompter.prompt( message, defaultValue )
                    : prompter.prompt( message, possibleValues, defaultValue );
                loadFromPrompt = true;
            }
            else
            {
                throw new MojoExecutionException( errorMessage );
            }
            if ( StringUtils.isEmpty( value ) )
            {
                throw new MojoExecutionException( errorMessage );
            }
        }

        if ( settings.isInteractiveMode() && !loadFromPrompt )
        {
            getLog().debug( "1st prompt message " + message + ", defaultValue " + defaultValue + ", possibleValues"
                                + possibleValues );
            value = ( possibleValues == null || possibleValues.isEmpty() ) ? ( StringUtils.isEmpty( defaultValue )
                ? prompter.prompt( message )
                : prompter.prompt( message, defaultValue ) )
                : ( StringUtils.isEmpty( defaultValue )
                    ? prompter.prompt( message, possibleValues )
                    : prompter.prompt( message, possibleValues, defaultValue ) );
        }
        return value;
    }


    protected Server getServer( String id )
    {
        if ( StringUtils.isEmpty( id ) )
        {
            return null;
        }
        return settings.getServer( id );
    }
}
