package org.apache.maven.mercury.metadata;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.maven.mercury.repository.LocalRepository;
import org.apache.maven.mercury.repository.RemoteRepository;

/**
 * This is the new entry point into Artifact resolution process.
 * It implements 3-phase 
 * 
 * @author Oleg Gusakov
 */
public class MetadataTree
{
  private MetadataSource mdSource;
  private Set<MetadataTreeArtifactFilter> filters;
  private List<MetadataTreeArtifactFilter> sorters;
  private LocalRepository localRepository;
  private Set<RemoteRepository> remoteRepositories;
  
  public MetadataTree(
        MetadataSource mdSource
      , Set<MetadataTreeArtifactFilter> filters
      , List<MetadataTreeArtifactFilter> sorters
      , LocalRepository localRepository
      , Set<RemoteRepository> remoteRepositories
                        )
  {
    this.mdSource = mdSource;
    this.filters = filters;
    this.sorters = sorters;
    this.localRepository = localRepository;
    this.remoteRepositories = remoteRepositories;
  }
  //-----------------------------------------------------
  public MetadataTreeNode buildTree( ArtifactMetadata startMD )
  throws MetadataTreeException
  {
    if( startMD == null )
      throw new MetadataTreeException( "null start point" );
    
    if( mdSource == null )
      throw new MetadataTreeException( "null metadata source" );
    
    if( localRepository == null )
      throw new MetadataTreeException( "null local repo" );
    
    MetadataTreeNode root = createNode( startMD, null, startMD );
    return root;
  }
  //-----------------------------------------------------
  private MetadataTreeNode createNode( ArtifactMetadata nodeMD, MetadataTreeNode parent, ArtifactMetadata nodeQuery )
  throws MetadataTreeException
  {
    checkForCircularDependency( nodeMD, parent );

    MetadataResolution mr;
    
    try
    {
      mr = mdSource.retrieve( nodeMD, localRepository, remoteRepositories );

      if( mr == null || mr.getArtifactMetadata() == null )
        throw new MetadataTreeException( "no result found for " + nodeMD );
      
      MetadataTreeNode node = new MetadataTreeNode( mr.getArtifactMetadata(), parent, nodeQuery );
  
      Collection<ArtifactMetadata> dependencies = mr.getArtifactMetadata().getDependencies();
      
      if( dependencies == null || dependencies.size() < 1 )
        return node;
      
      for( ArtifactMetadata md : dependencies )
      {
        Collection<ArtifactMetadata> versions = mdSource.expand( md, localRepository, remoteRepositories );
        if( versions == null || versions.size() < 1 )
        {
          if( md.isOptional() )
            continue;
          throw new MetadataTreeException( "did not find non-optional artifact for " + md );
        }
        
        boolean noGoodVersions = true;
        for( ArtifactMetadata ver : versions )
        {
          if( veto( ver, filters) )
            continue;
          
          MetadataTreeNode kid = createNode( ver, node, md );
          node.addChild( kid );
          
          noGoodVersions = false;
        }
        
        if( noGoodVersions )
        {
          if( md.isOptional() )
            continue;
          throw new MetadataTreeException( "did not find non-optional artifact for " + md );
        }
        
        node.addQuery(md);
      }
    
      return node;
    }
    catch (MetadataRetrievalException e)
    {
      throw new MetadataTreeException( e );
    }
  }
  //-----------------------------------------------------
  private void checkForCircularDependency( ArtifactMetadata md, MetadataTreeNode parent )
  throws MetadataTreeException
  {
    MetadataTreeNode p = parent;
    while( p != null )
    {
      if( md.sameGA(p.md) )
        throw new MetadataTreeException("circular dependency for " + md );
    }
  }
  //-----------------------------------------------------
  private boolean veto(ArtifactMetadata md, Set<MetadataTreeArtifactFilter> filters )
  {
    if( filters != null && filters.size() > 1)
      for( MetadataTreeArtifactFilter filter : filters )
        if( filter.veto(md) )
          return true;
    return false;
  }
  //-----------------------------------------------------
  // TODO
  public MetadataTreeNode resolveConflicts( MetadataTreeNode root )
  throws MetadataTreeException
  {
    return null;
  }
  //-----------------------------------------------------
  //-----------------------------------------------------
}