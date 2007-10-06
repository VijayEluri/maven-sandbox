package org.apache.maven.archetype.test;

import org.apache.maven.archetype.ArchetypeGenerationRequest;
import org.apache.maven.archetype.ArchetypeGenerationResult;
import org.apache.maven.archetype.Archetyper;
import org.apache.maven.archetype.catalog.Archetype;
import org.apache.maven.archetype.common.ArchetypeRegistryManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.codehaus.plexus.PlexusTestCase;

import java.io.File;
import java.util.List;

/** @author Jason van Zyl */
public class ArchetypeGenerationTest
    extends PlexusTestCase
{       
    public void testProjectGenerationFromAnArchetype()
        throws Exception
    {
        Archetyper archetype = (Archetyper) lookup( Archetyper.ROLE );

        // In the embedder the localRepository will be retrieved from the embedder itself and users won't
        // have to go through this muck.

        ArchetypeRegistryManager registryManager = (ArchetypeRegistryManager) lookup( ArchetypeRegistryManager.ROLE );

        ArtifactRepository localRepository = registryManager.createRepository(
            new File( getBasedir(), "target/local-repo" ).toURI().toURL().toExternalForm(), "local-repo" );

        List archetypes = archetype.getAvailableArchetypes();

        // Here I am just grabbing a Archetype but in a UI you would take the Archetype objects and present
        // them to the user.

        Archetype selection = (Archetype) archetypes.get( 14 );

        // Now you will present a dialog, or whatever, and grab the following values.

        String groupId = "com.mycompany";

        String artifactId = "app";

        String version = "1.0.0";

        String packageName = "org.mycompany.app";

        // With the selected Archetype and the parameters you can create a generation request as follows:

        ArchetypeGenerationRequest agr = new ArchetypeGenerationRequest( selection )
            .setOutputDirectory( new File( getBasedir(), "target/output" ).getAbsolutePath() )
            .setLocalRepository( localRepository )
            .setGroupId( groupId )
            .setArtifactId( artifactId )
            .setVersion( version )
            .setPackage( packageName );

        // Then generate away!

        ArchetypeGenerationResult result = archetype.generateProjectFromArchetype( agr );

        if ( result.getCause() != null )
        {
            fail( result.getCause().getMessage() );
        }
    }
}