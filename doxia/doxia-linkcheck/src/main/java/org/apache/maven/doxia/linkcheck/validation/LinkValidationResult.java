package org.apache.maven.doxia.linkcheck.validation;

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

import java.io.Serializable;

/**
 * <p>
 * This class is used to return status responses from the validation handlers. A persistent result means that it can be
 * stored in the persistent cache and used across runs.
 * </p>
 * 
 * @author <a href="mailto:bwalding@apache.org">Ben Walding</a>
 * @author <a href="mailto:aheritier@apache.org">Arnaud Heritier</a>
 * @version $Id$
 */
public class LinkValidationResult implements Serializable
{
    /** serialVersionUID. */
    private static final long serialVersionUID = -8346824125135406813L;

    /** Validation result: not mine. */
    public static final int NOTMINE = 0;

    /** Validation result: error. */
    public static final int ERROR = 1;

    /** Validation result: valid. */
    public static final int VALID = 2;

    /** Validation result: unknown. */
    public static final int UNKNOWN = 3;

    /** Validation result: warning. */
    public static final int WARNING = 4;

    /** The persistent property. */
    private final boolean persistent;

    /** The status. */
    private final int status;

    /** The error message. */
    private final String errorMessage;

    /**
     * Returns the persistent property.
     *
     * @return boolean
     */
    public boolean isPersistent()
    {
        return this.persistent;
    }

    /**
     * Returns the status.
     *
     * @return int
     */
    public int getStatus()
    {
        return this.status;
    }

    /**
     * Returns the errorMessage.
     *
     * @return the errorMessage.
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    /**
     * Constructor: initializes status, persistent and errorMessage.
     *
     * @param stat The status.
     * @param pers The persistent.
     * @param message The errorMessage.
     */
    public LinkValidationResult( int stat, boolean pers, String message )
    {
        this.status = stat;

        this.persistent = pers;

        this.errorMessage = message;
    }

    /** {@inheritDoc} */
    public String toString()
    {
        return this.persistent + "/" + this.status + "/" + this.errorMessage;
    }

}
