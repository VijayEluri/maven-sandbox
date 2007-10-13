package org.apache.maven.project;

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

import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.project.validation.ModelValidationResult;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class InvalidProjectModelException
    extends ProjectBuildingException
{
    private ModelValidationResult validationResult;

    public InvalidProjectModelException( String projectId,
                                         String pomLocation,
                                         String message,
                                         ModelInterpolationException cause )
    {
        super( projectId, message, pomLocation, cause );
    }

    public InvalidProjectModelException( String projectId,
                                         String pomLocation,
                                         String message,
                                         InvalidRepositoryException cause )
    {
        super( projectId, message, pomLocation, cause );
    }

    public InvalidProjectModelException( String projectId,
                                         String pomLocation,
                                         String message,
                                         ModelValidationResult validationResult )
    {
        super( projectId, message, pomLocation );

        this.validationResult = validationResult;
    }

    public InvalidProjectModelException( String projectId,
                                         String pomLocation,
                                         String message )
    {
        super( projectId, message, pomLocation );
    }

    public InvalidProjectModelException( String projectId,
                                         String pomLocation,
                                         String message,
                                         XmlPullParserException cause )
    {
        super( projectId, message, pomLocation, cause );
    }

    public final ModelValidationResult getValidationResult()
    {
        return validationResult;
    }

}