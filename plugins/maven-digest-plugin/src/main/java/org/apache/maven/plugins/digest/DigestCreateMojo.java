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

import java.io.PrintWriter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Creates digests (MD5 and SHA1 by default) for files specified by the configured includes and excludes. Also allows
 * specification of a list of files on the command line.
 */
@Mojo( name = "create", requiresProject=false )
public class DigestCreateMojo
    extends AbstractDigestMojo
{

    // ----------------------------------------------------------------------
    // Mojo components
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Mojo parameters
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Mojo options
    // ----------------------------------------------------------------------

    /**
     * Whether to append ' *filename' to the hash in the generated file, default {@code false}
     */
    @Parameter( property = "maven.digest.appendFilename", defaultValue = "false" )
    private boolean appendFilename;

    // ----------------------------------------------------------------------
    // Public methods
    // ----------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            if (!super.process()) {
                throw new MojoExecutionException( "Failed to create digests");
            }
        }
        catch ( Exception ex )
        {
            throw new MojoExecutionException( "Failed to create digest", ex );
        }
    }

    // ----------------------------------------------------------------------
    // Protected methods
    // ----------------------------------------------------------------------

    @Override
    protected boolean processFile( String algorithm, String extension, String file )
        throws Exception
    {
        final String digest = createDigest( algorithm, extension, file );
        final PrintWriter pw = new PrintWriter( file + extension, "UTF-8" );
        pw.print( digest );
        if ( appendFilename )
        {
            pw.println( " *" + file );
        }
        else
        {
            pw.println();
        }
        pw.close();
        return true;
    }

    // ----------------------------------------------------------------------
    // Private methods
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Static methods
    // ----------------------------------------------------------------------
}
