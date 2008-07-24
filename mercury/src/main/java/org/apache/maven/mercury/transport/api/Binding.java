/**
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

package org.apache.maven.mercury.transport.api;

import java.net.URI;

import org.apache.maven.mercury.repository.api.RepositoryException;

/**
 * Binding
 * <p/>
 * A Binding represents a remote uri whose contents are to be downloaded
 * and stored in a locally, or a local resource whose contents are to be
 * uploaded to the remote uri.
 */
public class Binding
{
    protected URI     remoteResource;
    protected URI     localResource;
    protected boolean lenientChecksum = true;
    
    protected RepositoryException error;
       
    public Binding()
    {        
    }
    
    public Binding( URI remoteUrl, URI localFile, boolean lenientChecksum )
    {
        this.remoteResource = remoteUrl;
        this.localResource = localFile;
        this.lenientChecksum = lenientChecksum;
    }

    public boolean isLenientChecksum()
    {
        return lenientChecksum;
    }

    public void setLenientChecksum( boolean leniantChecksum )
    {
        this.lenientChecksum = leniantChecksum;
    }

    public URI getRemoteResource()
    {
      return remoteResource;
    }

    public void setRemoteResource(
        URI remoteResource )
    {
      this.remoteResource = remoteResource;
    }

    public URI getLocalResource()
    {
      return localResource;
    }

    public void setLocalResource(
        URI localResource )
    {
      this.localResource = localResource;
    }

    public RepositoryException getError()
    {
      return error;
    }

    public void setError(
        RepositoryException error )
    {
      this.error = error;
    }

    public String toString()
    {
        return "[" + remoteResource + "," + localResource + "," + lenientChecksum + "]";
    }
    
    
}
