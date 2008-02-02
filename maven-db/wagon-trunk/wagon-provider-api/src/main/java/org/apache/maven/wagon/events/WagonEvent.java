package org.apache.maven.wagon.events;

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

import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.repository.Repository;

import java.util.EventObject;

/**
 * Base class for all events emitted by <code>Wagon</code> objects.
 *
 * @author <a href="michal.maczka@dimatics.com">Michal Maczka</a>
 * @version $Id$
 */
public class WagonEvent
    extends EventObject
{
    /**
     * Repository to which the Wagon
     * object which emitted this event is connected
     */
    protected Repository repository;

    /**
     * The time when event occured
     */
    protected long timestamp;

    /**
     * @param source The Wagon object on which the WagonEvent initially occurred
     */
    public WagonEvent( final Wagon source )
    {
        super( source );
    }

    /**
     * Returns The Wagon object on which the WagonEvent initially occurred
     *
     * @return The Wagon object on which the WagonEvent initially occurred
     */
    public Wagon getWagon()
    {
        return (Wagon) getSource();
    }

    /**
     * Returns the timestamp which indicated the time when this event has occured
     *
     * @return Returns the timestamp.
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Sets the timestamp which indicated the time when this event has occured
     *
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp( final long timestamp )
    {
        this.timestamp = timestamp;
    }

}
