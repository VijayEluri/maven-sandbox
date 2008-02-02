package org.apache.maven.wagon;

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

import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.resource.Resource;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base class for wagon which provide stream based API.
 *
 * @author <a href="mailto:michal@codehaus.org">Michal Maczka</a>
 * @version $Id$
 */
public abstract class StreamWagon
    extends AbstractWagon
{
    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    public abstract void fillInputData( InputData inputData )
        throws TransferFailedException, ResourceDoesNotExistException;

    public abstract void fillOutputData( OutputData outputData )
        throws TransferFailedException;

    public abstract void openConnection()
        throws ConnectionException, AuthenticationException;

    public abstract void closeConnection()
        throws ConnectionException;

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    public void get( String resourceName, File destination )
        throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        InputData inputData = new InputData();

        Resource resource = new Resource( resourceName );

        fireGetInitiated( resource, destination );

        inputData.setResource( resource );

        fillInputData( inputData );

        InputStream is = inputData.getInputStream();

        if ( is == null )
        {
            throw new TransferFailedException(
                getRepository().getUrl() + " - Could not open input stream for resource: '" + resource + "'" );
        }

        createParentDirectories( destination );

        getTransfer( inputData.getResource(), destination, is );
    }

    public boolean getIfNewer( String resourceName, File destination, long timestamp )
        throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        boolean retValue = false;

        InputData inputData = new InputData();

        Resource resource = new Resource( resourceName );

        inputData.setResource( resource );

        fillInputData( inputData );

        InputStream is = inputData.getInputStream();

        if ( resource.getLastModified() > timestamp )
        {
            retValue = true;

            if ( is == null )
            {
                throw new TransferFailedException(
                    getRepository().getUrl() + " - Could not open input stream for resource: '" + resource + "'" );
            }

            createParentDirectories( destination );

            getTransfer( inputData.getResource(), destination, is );
        }
        else
        {
            IOUtil.close( is );
        }

        return retValue;
    }


    // source doesn't exist exception
    public void put( File source, String resourceName )
        throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        OutputData outputData = new OutputData();

        Resource resource = new Resource( resourceName );

        firePutInitiated( resource, source );

        outputData.setResource( resource );

        fillOutputData( outputData );

        OutputStream os = outputData.getOutputStream();

        if ( os == null )
        {
            throw new TransferFailedException(
                getRepository().getUrl() + " - Could not open output stream for resource: '" + resource + "'" );
        }

        putTransfer( outputData.getResource(), source, os, true );
    }
}
