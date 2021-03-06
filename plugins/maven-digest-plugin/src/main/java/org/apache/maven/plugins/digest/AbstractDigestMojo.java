/*
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

package org.apache.maven.plugins.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

public abstract class AbstractDigestMojo
    extends AbstractMojo
{

    /**
     * The empty string array.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    // Mojo components
    // ----------------------------------------------------------------------

    @Component
    private MavenProject project;

    // ----------------------------------------------------------------------
    // Mojo parameters
    // ----------------------------------------------------------------------

    /**
     * List of files to include, default none. Standard Maven wildcard patterns apply. Patterns are assumed to be
     * relative to the project base directory.
     */
    @Parameter
    private Set<String> includes;

    /**
     * List of files to exclude, default none. Standard Maven wildcard patterns apply. Patterns are assumed to be
     * relative to the project base directory.
     */
    @Parameter
    private Set<String> excludes;

    /**
     * List of files to include, comma-separated (intended for command-line usage). Overrides includes and excludes;
     * uses same syntax as for {@code <include>} Patterns are assumed to be relative to the project base directory.
     */
    @Parameter( property = "maven.digest.files" )
    private String files;

    /**
     * List of digests (algorithms) to create (or check), comma-separated (intended for command-line usage). 
     * Overrides algorithms; uses same syntax
     */
    @Parameter( property = "maven.digest.digests" )
    private String digests;

    /**
     * The list of algorithm names with which to create (or check) digests. If none specified, the default is {@code MD5} and
     * {@code SHA1}. By default the file extension is assumed to be the algorithm name converted to lower-case, and any
     * "-" characters removed. The extension name can be provided by suffixing the algorithm name with ">" followed by
     * the extension, for example: "SHA-1>sha".
     */
    @Parameter
    private Set<String> algorithms;

    // ----------------------------------------------------------------------
    // Mojo options
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Public methods
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Protected methods
    // ----------------------------------------------------------------------

    protected final boolean process()
        throws Exception
    {
        final String files[] = scanForSources();
        final Log log = getLog();
        boolean success = false;
        if ( files.length == 0 )
        {
            log.warn( "No files found. Please configure at least one <include> item or use -Dmaven.digest.files" );
        }
        else
        {
            if ( digests != null && digests.length() > 0 )
            {
                String[] digest = digests.split( "," );
                algorithms = new HashSet<String>( digest.length );
                for ( String d : digest )
                {
                    algorithms.add( d );
                }
            }
            if ( algorithms == null || algorithms.size() == 0 )
            {
                algorithms = new HashSet<String>( 2 );
                algorithms.add( "MD5" );
                algorithms.add( "SHA1" );
            }
            success = true;
            for ( String file : files )
            {
                for ( String algorithm : algorithms )
                {
                    String[] parts = algorithm.split( ">" );
                    String extension;
                    if ( parts.length == 2 )
                    {
                        algorithm = parts[0];
                        extension = parts[1];
                    }
                    else
                    {
                        extension = getExtension( algorithm );
                    }
                    success &= processFile (algorithm, extension, file);
                }
            }
        }
        return success;
    }

    protected final String createDigest( String algorithm, String extension, String file )
                    throws Exception
                {
                    // Unfortunately DigestUtils.getDigest is not public
                    // Do this before opening file in case not found
                    MessageDigest digest = MessageDigest.getInstance( algorithm );
                    FileInputStream is = new FileInputStream( file );
                    String hexDigest = digestHex( digest, is );
                    is.close();
                    return hexDigest;
                }

    protected abstract boolean processFile( String algorithm, String extension, String file)
        throws Exception;

    /**
     * Override this method to change the default includes.
     * @return this implementation returns {@link #EMPTY_STRING_ARRAY}
     */
    protected String[] getDefaultIncludes() {
        return EMPTY_STRING_ARRAY;
    }

    // ----------------------------------------------------------------------
    // Private methods
    // ----------------------------------------------------------------------

    /**
     * Creates the extension from the algortithm name, by converting to lower-case and dropping any "-" characters. The
     * result is prefixed with ".".
     * 
     * @param algorithm the algorithm name
     * @return the extension, e.g. ".md5", ".sha1"
     */
    private String getExtension( String algorithm )
    {
        return "." + algorithm.toLowerCase( Locale.ENGLISH ).replace( "-", "" );
    }

    private String digestHex( MessageDigest digest, InputStream data )
        throws IOException
    {
        return Hex.encodeHexString( digest( digest, data ) );
    }

    // Unfortunately, the Codec version is private
    private byte[] digest( MessageDigest digest, InputStream data )
        throws IOException
    {
        byte[] buffer = new byte[1024];
        int read = data.read( buffer );
        while ( read > -1 )
        {
            digest.update( buffer, 0, read );
            read = data.read( buffer );
        }
        return digest.digest();
    }

    private String[] scanForSources()
    {
        DirectoryScanner ds = new DirectoryScanner();
        ds.setFollowSymlinks( true );
        File basedir = project.getBasedir();
        if (basedir == null) {
            basedir = new File("."); // current directory
        }
        ds.setBasedir( basedir ); // Cannot be omitted; implies that includes/excludes are relative
        String[] inc;
        if ( files != null )
        { // Overrides includes / excludes
            getLog().debug( "files=" + files );
            inc = files.split( "," );
        }
        else
        {
            if ( includes == null || includes.isEmpty() )
            {
                inc = getDefaultIncludes(); // overrides default of **
            }
            else
            {
                inc = includes.toArray( new String[includes.size()] );
            }
            if ( excludes != null )
            {
                String[] excl = excludes.toArray( new String[excludes.size()] );
                ds.setExcludes( excl );
            }
        }
        ds.setIncludes( inc );
        ds.addDefaultExcludes(); // TODO should this be optional?
        ds.scan();
        return ds.getIncludedFiles();
    }

    // ----------------------------------------------------------------------
    // Static methods
    // ----------------------------------------------------------------------
}
