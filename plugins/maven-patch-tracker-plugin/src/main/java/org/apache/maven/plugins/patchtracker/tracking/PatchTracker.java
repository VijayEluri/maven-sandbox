package org.apache.maven.plugins.patchtracker.tracking;

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

import org.apache.maven.plugin.logging.Log;

/**
 * provide some services around a patch tracker.
 * A patch tracker can be an issue tracker (jira), a patch reviewer (reviewboar) or Jenkins to test the patch
 *
 * @author Olivier Lamy
 */
public interface PatchTracker
{

    /**
     * create a new entry in the patch tracker
     *
     * @param patchTrackerRequest
     * @return
     * @throws PatchTrackerException
     */
    PatchTrackerResult createPatch( PatchTrackerRequest patchTrackerRequest, Log log )
        throws PatchTrackerException;

    /**
     * update a patch entry in the patch tracker
     * <b>patchTrackerRequest.patchId is mandatory!</b>
     *
     * @param patchTrackerRequest
     * @return
     * @throws PatchTrackerException
     */
    PatchTrackerResult updatePatch( PatchTrackerRequest patchTrackerRequest, Log log )
        throws PatchTrackerException;
}
