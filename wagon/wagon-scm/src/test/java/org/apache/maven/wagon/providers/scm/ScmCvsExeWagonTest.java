package org.apache.maven.wagon.providers.scm;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
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

import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.provider.cvslib.cvsexe.CvsExeScmProvider;

/**
 * Test for ScmWagon using CVS Exe as underlying SCM
 *
 * @author <a href="carlos@apache.org">Carlos Sanchez</a>
 * @version $Id$
 */
public class ScmCvsExeWagonTest
    extends AbstractScmCvsWagonTest
{

    protected ScmProvider getScmProvider()
    {
        return new CvsExeScmProvider();
    }

    public void testFailedGet()
        throws Exception
    {
        // Not ready yet
    }

    public void testWagon()
        throws Exception
    {
        // Not ready yet
    }

    public void testWagonPutDirectory()
        throws Exception
    {
        // Not ready yet
    }

    public void testWagonPutDirectoryWhenDirectoryAlreadyExists()
        throws Exception
    {
        // Not ready yet
    }

}