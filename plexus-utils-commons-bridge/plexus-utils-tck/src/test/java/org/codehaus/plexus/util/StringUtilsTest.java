package org.codehaus.plexus.util;

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

import org.apache.maven.tck.FixPlexusBugs;
import org.apache.maven.tck.ReproducesPlexusBug;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;


/**
 * Test the {@link org.codehaus.plexus.util.StringUtils} class.
 *
 * We don't need to test this
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class StringUtilsTest extends Assert
{

    @Rule
    public FixPlexusBugs fixPlexusBugs = new FixPlexusBugs();

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Test(expected = NullPointerException.class)
    public void testAbbreviate_NPE()
    {
        assertThat( StringUtils.abbreviate( null, 10 )
                , nullValue() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviate_MinLength()
    {
        assertThat( StringUtils.abbreviate( "This is a longtext", 3 )
                  , is( "T" ) );
    }

    @Test
    public void testAbbreviate()
    {
        assertThat( StringUtils.abbreviate( "This is a longtext", 10 )
                  , is( "This is..." ) );

        assertThat( StringUtils.abbreviate( "This is a longtext", 50 )
                  , is( "This is a longtext" ) );
    }

    @Test(expected = NullPointerException.class)
    public void testAbbreviate_Offset_NPE()
    {
        assertThat( StringUtils.abbreviate( null, 10, 20 )
                , nullValue() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbbreviate_Offset_MinLength()
    {
        assertThat( StringUtils.abbreviate( "This is a longtext", 10, 3 )
                  , is( "T" ) );
    }

    @Test
    public void testAbbreviate_Offset()
    {
        assertThat( StringUtils.abbreviate( "This is a longtext", 5, 10 )
                  , is( "...is a..." ) );

        assertThat( StringUtils.abbreviate( "This is a longtext", 10, 20 )
                  , is( "This is a longtext" ) );

        assertThat( StringUtils.abbreviate( "This is a longtext", 50, 20 )
                  , is( "This is a longtext" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testAddAndDeHump_NPE()
    {
        StringUtils.addAndDeHump( null );
    }

    @Test
    public void testAddAndDeHump()
    {
        assertThat( StringUtils.addAndDeHump( "lalala" )
                  , is( "lalala" ) );

        assertThat( StringUtils.addAndDeHump( "LaLaLa" )
                  , is( "la-la-la" ) );

        assertThat( StringUtils.addAndDeHump( "ALLUPPER" )
                  , is( "a-l-l-u-p-p-e-r" ) );

    }

    @Test
    public void testCapitalise()
    {
        assertThat( StringUtils.capitalise( null )
                , nullValue() );

        assertThat( StringUtils.capitalise( "startBig" )
                , is( "StartBig" ) );
    }

    @Test
    public void testCapitaliseAllWords()
    {
        assertThat( StringUtils.capitaliseAllWords( null )
                , nullValue() );

        assertThat( StringUtils.capitaliseAllWords( "start all big" )
                , is( "Start All Big" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testCapitalizeFirstLetter_NPE()
    {
        assertThat( StringUtils.capitalizeFirstLetter( null )
                , nullValue() );
    }

    @Test
    public void testCapitalizeFirstLetter()
    {
        assertThat( StringUtils.capitalizeFirstLetter( "start all big" )
                , is( "Start all big" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testCenter_NPE()
    {
        StringUtils.center( null, 20 );
    }

    @Test
    public void testCenter()
    {
        assertThat( StringUtils.center( "centerMe", 20 )
                , is( "      centerMe      " ) );

        assertThat( StringUtils.center( "centerMe", 4 )
                , is( "centerMe" ) );

        assertThat( StringUtils.center( "        centerMe", 20 )
                , is( "          centerMe  " ) );
    }

    @Test( expected = NullPointerException.class )
    public void testCenter_Delim_NPE()
    {
        StringUtils.center( null, 20, "*" );
    }

    @Test
    public void testCenter_Delim()
    {
        assertThat( StringUtils.center( "centerMe", 20, "*" )
                , is( "******centerMe******" ) );

        assertThat( StringUtils.center( "centerMe", 4, "*" )
                , is( "centerMe" ) );

        assertThat( StringUtils.center( "        centerMe", 20, "*" )
                , is( "**        centerMe**" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testChomp_NPE()
    {
        StringUtils.chomp( null );
    }

    @Test
    public void testChomp()
    {
        assertThat( StringUtils.chomp( "dings" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings\nbums" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings\nbums\ndongs" )
                , is( "dings\nbums" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testChomp_Delim_NPE()
    {
        StringUtils.chomp( null, "+" );
    }

    @Test
    public void testChomp_Delim()
    {
        assertThat( StringUtils.chomp( "dings", "+" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings+", "+" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings+bums", "+" )
                , is( "dings" ) );

        assertThat( StringUtils.chomp( "dings+bums+dongs", "+" )
                , is( "dings+bums" ) );
    }


    @Test( expected = NullPointerException.class )
    public void testChompLast_NPE()
    {
        StringUtils.chompLast( null );
    }

    @Test
    public void testChompLast()
    {
        assertThat( StringUtils.chompLast( "dings" )
                , is( "dings" ) );

        assertThat( StringUtils.chompLast( "\n" )
                , is( "" ) );

        assertThat( StringUtils.chompLast( "dings\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chompLast( "dings\nbums" )
                , is( "dings\nbums" ) );

        assertThat( StringUtils.chompLast( "dings\nbums\ndongs\n" )
                , is( "dings\nbums\ndongs" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testChompLast_Delim_NPE()
    {
        StringUtils.chompLast( null, "+" );
    }

    @Test
    public void testChompLast_Delim()
    {
        assertThat( StringUtils.chompLast( "dings", "+" )
                , is( "dings" ) );

        assertThat( StringUtils.chompLast( "+", "+" )
                , is( "" ) );

        assertThat( StringUtils.chompLast( "dings+", "+" )
                , is( "dings" ) );

        assertThat( StringUtils.chompLast( "dings+bums", "+" )
                , is( "dings+bums" ) );

        assertThat( StringUtils.chompLast( "dings+bums+dongs+", "+" )
                , is( "dings+bums+dongs" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testChop_NPE()
    {
        StringUtils.chop( null );
    }

    @Test
    public void testChop()
    {
        assertThat( StringUtils.chop( "dings" )
                , is( "ding" ) );

        assertThat( StringUtils.chop( "x" )
                , is( "" ) );

        assertThat( StringUtils.chop( "dings\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chop( "dings\r\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chop( "dings\n\r" )
                , is( "dings\n" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testChopNewline_NPE()
    {
        StringUtils.chopNewline( null );
    }

    @Test( expected = IndexOutOfBoundsException.class )
    @ReproducesPlexusBug( "IndexOutOfBounds is just plain wrong^^" )
    public void testChopNewline_IOB()
    {
        StringUtils.chopNewline( "\n" );
    }

    @Test
    public void testChopNewline()
    {
        assertThat( StringUtils.chopNewline( "dings" )
                , is( "dings" ) );

        assertThat( StringUtils.chopNewline( "x" )
                , is( "x" ) );


        assertThat( StringUtils.chopNewline( "dings\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chopNewline( "dings\r\n" )
                , is( "dings" ) );

        assertThat( StringUtils.chopNewline( "dings\n\r" )
                , is( "dings\n\r" ) );
    }


    @Test
    public void testClean()
    {
        assertThat( StringUtils.clean( null )
                  , is( "" ) );

        assertThat( StringUtils.clean( "   " )
                  , is( "" ) );

        assertThat( StringUtils.clean( "  c " )
                  , is( "c" ) );

        assertThat( StringUtils.clean( "  dings \n  " )
                  , is( "dings" ) );
    }


    @Test( expected = NullPointerException.class )
    public void testConcatenate_NPE()
    {
        StringUtils.concatenate( null );
    }

    @Test
    public void testConcatenate()
    {
        assertThat( StringUtils.concatenate( new String[0] )
                  , is( "" ) );

        assertThat( StringUtils.concatenate( new String[]{ "x" } )
                  , is( "x" ) );

        assertThat( StringUtils.concatenate( new String[]{ "x", "y", "z" } )
                  , is( "xyz" ) );
    }

    @Test
    public void testContains_String()
    {
        assertThat( StringUtils.contains( null, null )
                , is( false ) );

        assertThat( StringUtils.contains( null, "string" )
                , is( false ) );

        assertThat( StringUtils.contains( "string", null )
                , is( false ) );

        assertThat( StringUtils.contains( "string", "" )
                , is( true ) );

        assertThat( StringUtils.contains( "string", "in" )
                , is( true ) );

        assertThat( StringUtils.contains( "string", "IN" )
                , is( false ) );
    }

    @Test
    public void testContains_Char()
    {
        assertThat( StringUtils.contains( null, 'c' )
                , is( false ) );

        assertThat( StringUtils.contains( "string", "c" )
                , is( false ) );

        assertThat( StringUtils.contains( "string", "" )
                , is( true ) );

        assertThat( StringUtils.contains( "string", "r" )
                , is( true ) );

        assertThat( StringUtils.contains( "string", "R" )
                , is( false ) );

    }

    @Test( expected = NullPointerException.class )
    public void testCountMatches_NPE()
    {
         StringUtils.countMatches( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testCountMatches_NPE2()
    {
         StringUtils.countMatches( "this is it", null );
    }

    @Test
    public void testCountMatches()
    {
        assertThat( StringUtils.countMatches( null, "is" )
                  , is( 0 ) );

        assertThat( StringUtils.countMatches( "this is it", "is" )
                  , is( 2 ) );

        assertThat( StringUtils.countMatches( "this is it", "notincluded" )
                  , is( 0 ) );
    }

    @Test
    public void testDefaultString()
    {
         assertThat( StringUtils.defaultString( null )
                   , is( "" ) );

        assertThat( StringUtils.defaultString( "dings" )
                  , is( "dings" ) );
    }

    @Test
    public void testDefaultString_defaultValue()
    {
         assertThat( StringUtils.defaultString( null, "defaultValue" )
                   , is( "defaultValue" ) );

        assertThat( StringUtils.defaultString( "dings", "defaultValue" )
                  , is( "dings" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testDeleteWhitespace_NPE()
    {
        StringUtils.deleteWhitespace( null );
    }

    @Test
    public void testDeleteWhitespace()
    {
        assertThat( StringUtils.deleteWhitespace( " \t  \n" )
                  , is( "" ) );

        assertThat( StringUtils.deleteWhitespace( " \t  \b \n" )
                  , is( "\b" ) );

        assertThat( StringUtils.deleteWhitespace( "dings" )
                , is( "dings" ) );

        assertThat( StringUtils.deleteWhitespace( "\n  dings \t " )
                  , is( "dings" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testDifference_NPE()
    {
        StringUtils.difference( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testDifference_NPE2()
    {
        StringUtils.difference( null, "another" );
    }

    @Test( expected = NullPointerException.class )
    public void testDifference_NPE3()
    {
        StringUtils.difference( "this", null );
    }

    @Test
    public void testDifference()
    {
        assertThat( StringUtils.difference( "this", "another" )
                , is( "another" ) );

        assertThat( StringUtils.difference( "I am human", "I am a robot" )
                  , is( "a robot" ) );

        assertThat( StringUtils.difference( "I am human", "I AM a robot" )
                  , is( "AM a robot" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testDifferenceAt_NPE()
    {
        StringUtils.differenceAt( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testDifferenceAt_NPE2()
    {
        StringUtils.differenceAt( "test", null );
    }

    @Test( expected = NullPointerException.class )
    public void testDifferenceAt_NPE3()
    {
        StringUtils.differenceAt( null, "test" );
    }

    @Test
    public void testDifferenceAt()
    {
        assertThat( StringUtils.differenceAt( "this", "another" )
                  , is( 0 ) );

        assertThat( StringUtils.differenceAt( "I am human", "I am a robot" )
                  , is( 5 ) );

        assertThat( StringUtils.differenceAt( "I am human", "I AM a robot" )
                , is( 2 ) );
    }

    @Test
    public void testEquals()
    {
        assertThat( StringUtils.equals( null, null )
                  , is( true ) );

        assertThat( StringUtils.equals( "x", null )
                  , is( false ) );

        assertThat( StringUtils.equals( null, "x" )
                  , is( false ) );

        assertThat( StringUtils.equals( "X", "x" )
                  , is( false ) );

        assertThat( StringUtils.equals( "dings", "dings" )
                  , is( true ) );
    }

    @Test
    public void testEqualsIgnoreCase()
    {
        assertThat( StringUtils.equalsIgnoreCase( null, null )
                  , is( true ) );

        assertThat( StringUtils.equalsIgnoreCase( "x", null )
                  , is( false ) );

        assertThat( StringUtils.equalsIgnoreCase( null, "x" )
                  , is( false ) );

        assertThat( StringUtils.equalsIgnoreCase( "X", "x" )
                  , is( true ) );

        assertThat( StringUtils.equalsIgnoreCase( "dings", "dings" )
                  , is( true ) );

        assertThat( StringUtils.equalsIgnoreCase( "dings", "diNGs" )
                  , is( true ) );
    }

    @Test( expected = NullPointerException.class )
    public void testEscape_NPE()
    {
        StringUtils.escape( null );
    }

    @Test
    public void testEscape()
    {
        assertThat( StringUtils.escape( "dings" )
                  , is( "dings" ) );

        assertThat( StringUtils.escape( "dings\tbums" )
                  , is( "dings\\tbums" ) );

        assertThat( StringUtils.escape( "dings\nbums" )
                  , is( "dings\\nbums" ) );
    }


    @Test
    public void testEscape2()
    {
        assertThat( StringUtils.escape( null, null, '#' )
                  , nullValue() );

        assertThat( StringUtils.escape( "dings", new char[]{'\t','\b'}, '+' )
                  , is( "dings" ) );

        assertThat( StringUtils.escape( "dings\tbums", new char[]{'\t','\b'}, '+' )
                  , is( "dings+\tbums" ) );

        assertThat( StringUtils.escape( "dings\nbums", new char[]{'\t','\b'}, '+' )
                  , is( "dings\nbums" ) );
        assertThat( StringUtils.escape( "dings\bbums", new char[]{'\t','\b'}, '+' )
                  , is( "dings+\bbums" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testGetChomp_NPE1()
    {
        StringUtils.getChomp( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testGetChomp_NPE2()
    {
        StringUtils.getChomp( "dings", null );
    }

    @Test( expected = NullPointerException.class )
    public void testGetChomp_NPE3()
    {
        StringUtils.getChomp( null, "dings" );
    }

    @Test
    public void testGetChomp()
    {
        assertThat( StringUtils.getChomp( "dings-bums", "-" )
                  , is( "-bums" ) );

        assertThat( StringUtils.getChomp( "dings-", "-" )
                  , is( "-" ) );

        assertThat( StringUtils.getChomp( "dingsbums", "-" )
                , is( "" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testGetNestedString_NPE()
    {
        assertThat( StringUtils.getNestedString( "  +dings+ ", null )
                , nullValue() );
    }

    @Test
    public void testGetNestedString()
    {
        assertThat( StringUtils.getNestedString( null, null )
                  , nullValue() );

        assertThat( StringUtils.getNestedString( "  +dings+ ", "+" )
                  , is( "dings" ) );

        assertThat( StringUtils.getNestedString( "  +dings+ ", "not" )
                  , nullValue() );
    }

    @Test( expected = NullPointerException.class )
    public void testGetNestedString2_NPE1()
    {
        assertThat( StringUtils.getNestedString( "  +dings+ ", null, null )
                  , nullValue() );
    }

    @Test( expected = NullPointerException.class )
    public void testGetNestedString2_NPE2()
    {
        assertThat( StringUtils.getNestedString( "  +dings+ ", null, "neither" )
                  , nullValue() );
    }

    @Test
    public void testGetNestedString2()
    {
        assertThat( StringUtils.getNestedString( null, null, null )
                  , nullValue() );

        assertThat( StringUtils.getNestedString( "  +dings+ ", "not", null )
                  , nullValue() );

        assertThat( StringUtils.getNestedString( "  +dings- ", "+", "-" )
                  , is( "dings" ) );

        assertThat( StringUtils.getNestedString( "  +dings+ ", "not", "neither" )
                  , nullValue() );
    }

    @Test( expected = NullPointerException.class )
    public void testGetPrechomp_NPE1()
    {
        StringUtils.getPrechomp( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testGetPrechomp_NPE2()
    {
        StringUtils.getPrechomp( null, "bums" );
    }

    @Test
    public void testGetPrechomp()
    {
        assertThat( StringUtils.getPrechomp( "dings bums dongs", "bums" )
                  , is( "dings bums" ) );

        assertThat( StringUtils.getPrechomp( "dings bums dongs", "non" )
                , is( "" ) );
    }

    @Test
    public void testIndexOfAny()
    {
        assertThat( StringUtils.indexOfAny( null, null )
                  , is( -1 ) );

        assertThat( StringUtils.indexOfAny( "dings", null )
                  , is( -1 ) );

        assertThat( StringUtils.indexOfAny( null, new String[]{ } )
                  , is( -1 ) );

        assertThat( StringUtils.indexOfAny( "dings bums dongs", new String[]{ "knuff", "bums" } )
                , is( 6 ) );
    }

    @Test( expected = NullPointerException.class )
    public void testInterpolate_NPE()
    {
        StringUtils.interpolate( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testInterpolate_NPE2()
    {
        StringUtils.interpolate( "This ${text} will get replaced", null );
    }

    @Test
    public void testInterpolate()
    {
        Map variables = new HashMap<String,String>();
        assertThat( StringUtils.interpolate( "This ${text} will get replaced", variables )
                  , is( "This ${text} will get replaced" ) );

        variables.put( "text", "with a special content" );

        assertThat( StringUtils.interpolate( "This ${text} will get replaced", variables )
                  , is( "This with a special content will get replaced" ) );
    }

    @Test
    public void testIsAlpha()
    {
        assertThat( StringUtils.isAlpha( null )
                  , is( false ) );

        assertThat( StringUtils.isAlpha( "2" )
                  , is( false ) );

        assertThat( StringUtils.isAlpha( "asvsdfSDF" )
                  , is( true ) );

        assertThat( StringUtils.isAlpha( "asvsdfSDF \t " )
                  , is( false ) );

        assertThat( StringUtils.isAlpha( "435afsafd3!" )
                  , is( false ) );
    }

    @Test
    public void testIsAlphaSpace()
    {
        assertThat( StringUtils.isAlphaSpace( null )
                  , is( false ) );

        assertThat( StringUtils.isAlphaSpace( "2" )
                  , is( false ) );

        assertThat( StringUtils.isAlphaSpace( "asvsdfSDF" )
                  , is( true ) );

        assertThat( StringUtils.isAlphaSpace( "asvsdfSDF  " )
                  , is( true ) );

        assertThat( StringUtils.isAlphaSpace( "asvsdfSDF \t " )
                  , is( false ) );

        assertThat( StringUtils.isAlphaSpace( "435afsafd3!" )
                  , is( false ) );
    }

    @Test
    public void testIsAlphanumeric()
    {
        assertThat( StringUtils.isAlphanumeric( null )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumeric( "2" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumeric( "asvsdfSDF" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumeric( "asvsdfSDF  " )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumeric( "asvsdfSDF \t " )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumeric( "435afsafd3!" )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumeric( "435afsafd3" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumeric( "435 " )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumeric( "435" )
                  , is( true ) );
    }

    @Test
    public void testIsAlphanumericSpace()
    {
        assertThat( StringUtils.isAlphanumericSpace( null )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumericSpace( "2" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumericSpace( "asvsdfSDF" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumericSpace( "asvsdfSDF  " )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumericSpace( "asvsdfSDF \t " )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumericSpace( "435afsafd3!" )
                  , is( false ) );

        assertThat( StringUtils.isAlphanumericSpace( "435afsafd3" )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumericSpace( "435 " )
                  , is( true ) );

        assertThat( StringUtils.isAlphanumericSpace( "435" )
                  , is( true ) );
    }

    @Test
    public void testIsBlank()
    {
        assertThat( StringUtils.isBlank( null )
                  , is( true ) );

        assertThat( StringUtils.isBlank( "xx" )
                  , is( false ) );

        assertThat( StringUtils.isBlank( "xx " )
                  , is( false ) );

        assertThat( StringUtils.isBlank( "  " )
                  , is( true ) );

        assertThat( StringUtils.isBlank( "  \t " )
                  , is( true ) );

        assertThat( StringUtils.isBlank( "  \n " )
                  , is( true ) );
    }


    @Test
    public void testEmpty()
    {
        assertThat( StringUtils.isEmpty( null )
                  , is( true ) );

        assertThat( StringUtils.isEmpty( "xx" )
                  , is( false ) );

        assertThat( StringUtils.isEmpty( "xx " )
                  , is( false ) );

        assertThat( StringUtils.isEmpty( "  " )
                  , is( true ) );

        assertThat( StringUtils.isEmpty( "  \t " )
                  , is( true ) );

        assertThat( StringUtils.isEmpty( "  \n " )
                  , is( true ) );
    }

    @Test
    public void testNotBlank()
    {
        assertThat( StringUtils.isNotBlank( null )
                  , is( false ) );

        assertThat( StringUtils.isNotBlank( "xx" )
                  , is( true ) );

        assertThat( StringUtils.isNotBlank( "xx " )
                  , is( true ) );

        assertThat( StringUtils.isNotBlank( "  " )
                  , is( false ) );

        assertThat( StringUtils.isNotBlank( "  \t " )
                  , is( false ) );

        assertThat( StringUtils.isNotBlank( "  \n " )
                  , is( false ) );
    }

    @Test
    public void testNotEmpty()
    {
        assertThat( StringUtils.isNotEmpty( null )
                  , is( false ) );

        assertThat( StringUtils.isNotEmpty( "xx" )
                  , is( true ) );

        assertThat( StringUtils.isNotEmpty( "xx " )
                  , is( true ) );

        assertThat( StringUtils.isNotEmpty( "  " )
                  , is( true ) );

        assertThat( StringUtils.isNotEmpty( "" )
                  , is( false ) );

        assertThat( StringUtils.isNotEmpty( "  \t " )
                  , is( true ) );

        assertThat( StringUtils.isNotEmpty( "  \n " )
                  , is( true ) );
    }

    @Test
    public void testIsNumeric()
    {
        assertThat( StringUtils.isNumeric( null )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "2" )
                  , is( true ) );

        assertThat( StringUtils.isNumeric( "asvsdfSDF" )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "asvsdfSDF  " )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "asvsdfSDF \t " )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "435afsafd3!" )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "435afsafd3" )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "435 " )
                  , is( false ) );

        assertThat( StringUtils.isNumeric( "435" )
                  , is( true ) );
    }

    @Test
    public void testIsWhitespace()
    {
        assertThat( StringUtils.isWhitespace( null )
                  , is( false ) );

        assertThat( StringUtils.isWhitespace( "xx" )
                  , is( false ) );

        assertThat( StringUtils.isWhitespace( "xx " )
                  , is( false ) );

        assertThat( StringUtils.isWhitespace( "  " )
                  , is( true ) );

        assertThat( StringUtils.isWhitespace( "" )
                  , is( true ) );

        assertThat( StringUtils.isWhitespace( "  \t " )
                  , is( true ) );

        assertThat( StringUtils.isWhitespace( "  \n " )
                  , is( true ) );
    }

    @Test( expected = NullPointerException.class )
    public void testJoin_Array_NPE()
    {
        StringUtils.join( ( Object[] ) null, null );
    }

    @Test
    public void testJoin_Array()
    {
        assertThat( StringUtils.join( new Object[0], null )
                  , is( "" ) );

        assertThat( StringUtils.join( new Object[]{ "a", "b", "c"}, null )
                  , is( "abc" ) );

        assertThat( StringUtils.join( new Object[]{ "a", "b", "c"}, "__" )
                  , is( "a__b__c" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testJoin_Iterator_NPE()
    {
        StringUtils.join( (Iterator) null, null );
    }

    @Test
    public void testJoin_Iterator()
    {
        ArrayList list = new ArrayList();

        assertThat( StringUtils.join( list.iterator(), null )
                  , is( "" ) );

        list.add( "a" );
        list.add( "b" );
        list.add( "c" );

        assertThat( StringUtils.join( list.iterator(), null )
                  , is( "abc" ) );

        assertThat( StringUtils.join( list.iterator(), "__" )
                  , is( "a__b__c" ) );
    }

    @Test
    public void testLastIndexOfAny()
    {
        assertThat( StringUtils.lastIndexOfAny( null, null )
                  , is( -1 ) );

        assertThat( StringUtils.lastIndexOfAny( "dings", null )
                  , is( -1 ) );

        assertThat( StringUtils.lastIndexOfAny( "dings bums boms", new String[] {"ms", " b"} )
                  , is( 13 ) );

        assertThat( StringUtils.lastIndexOfAny( "dings bums boms", new String[] {"nix", "da"} )
                  , is( -1 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testLeft_IAE()
    {
        StringUtils.left( null, -1 );
    }

    @Test
    public void testLeft()
    {
        assertThat( StringUtils.left( null, 4 )
                  , nullValue() );

        assertThat( StringUtils.left( "dingsbums", 4 )
                  , is( "ding" ) );

        assertThat( StringUtils.left( "dingsbums", 40 )
                  , is( "dingsbums" ) );

        assertThat( StringUtils.left( "dingsbums", 0 )
                  , is( "" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testLeftPad1_NPE()
    {
        StringUtils.leftPad( null, 0 );
    }

    @Test
    public void testLeftPad1()
    {
        assertThat( StringUtils.leftPad( "dings", 0 )
                  , is( "dings")  );

        assertThat( StringUtils.leftPad( "dings", 2 )
                  , is( "dings")  );

        assertThat( StringUtils.leftPad( "dings", 10 )
                  , is( "     dings")  );
    }

    @Test( expected = NullPointerException.class )
    public void testLeftPad2_NPE1()
    {
        StringUtils.leftPad( null, 0, null );
    }

    @Test( expected = NullPointerException.class )
    public void testLeftPad2_NPE2()
    {
        StringUtils.leftPad( "dings", 0, null );
    }

    @Test( expected = NullPointerException.class )
    public void testLeftPad2_NPE3()
    {
        StringUtils.leftPad( null, 0, "*" );
    }

    @Test
    public void testLeftPad2()
    {
        assertThat( StringUtils.leftPad( "dings", 0, "*" )
                  , is( "dings") );

        assertThat( StringUtils.leftPad( "dings", 2, "*" )
                  , is( "dings") );

        assertThat( StringUtils.leftPad( "dings", 10, "*" )
                  , is( "*****dings") );
    }

    @Test
    public void testLowerCase()
    {
        assertThat( StringUtils.lowerCase( null )
                  , nullValue()  );

        assertThat( StringUtils.lowerCase( "dinGSbuMS" )
                  , is( "dingsbums" ) );

        assertThat( StringUtils.lowerCase( "" )
                  , is( "" ) );

    }

    @Test( expected = NullPointerException.class )
    public void testLowerCaseFirstLetter_NPE()
    {
        StringUtils.lowercaseFirstLetter( null );
    }

    @Test( expected = IndexOutOfBoundsException.class )
    @ReproducesPlexusBug( value = "Ridiculous IndexOutOfBoundsException!" )
    public void testLowerCaseFirstLetter_buggy()
    {
        assertThat( StringUtils.lowercaseFirstLetter( "" )
                  , is( "" ) );
    }

    @Test
    public void testLowerCaseFirstLetter()
    {
        assertThat( StringUtils.lowercaseFirstLetter( "Dings Bums" )
                  , is( "dings Bums" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testMid_NegativeLen()
    {
        StringUtils.mid( null, 0, -2 );
    }

    @Test( expected = IndexOutOfBoundsException.class )
    public void testMid_WrongPos()
    {
        StringUtils.mid( null, -2, 3 );
    }

    @Test
    public void testMid()
    {
        assertThat( StringUtils.mid( null, 0, 0 )
                  , nullValue() );

        assertThat( StringUtils.mid( "dings bums", 0, 0 )
                  , is( "" ) );

        assertThat( StringUtils.mid( "dings bums", 3, 4 )
                  , is( "gs b" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testOverlayString_NPE1()
    {
        StringUtils.overlayString( null, null, 0, 0 );
    }

    @Test( expected = NullPointerException.class )
    public void testOverlayString_NPE2()
    {
        StringUtils.overlayString( "dings", null, 0, 0 );
    }

    @Test( expected = NullPointerException.class )
    public void testOverlayString_NPE3()
    {
        StringUtils.overlayString( null, "bums", 0, 0 );
    }

    @Test
    public void testOverlayString()
    {
        assertThat( StringUtils.overlayString( "dings", "bums", 0, 0 )
                  , is( "bumsdings" ) );

        assertThat( StringUtils.overlayString( "dings", "bums", 2, 4 )
                  , is( "dibumss" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testPrechomp_NPE1()
    {
        StringUtils.prechomp( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testPrechomp_NPE2()
    {
        StringUtils.prechomp( "dings", null );
    }

    @Test( expected = NullPointerException.class )
    public void testPrechomp_NPE3()
    {
        StringUtils.prechomp( null, "bums" );
    }

    @Test
    public void testPrechomp()
    {
        assertThat( StringUtils.prechomp( "dings bums", " " )
                  , is( "bums" ) );

        assertThat( StringUtils.prechomp( "dings bums", "nix" )
                  , is( "dings bums" ) );
    }

    @Test
    public void testQuoteAndEscape1()
    {
        assertThat( StringUtils.quoteAndEscape( null, '+' )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+' )
                  , is( "" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"' )
                  , is( "abc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"' )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'' )
                  , is( "\'a\\'bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'' )
                  , is( "a\"bc" ) );
    }

    @Test
    public void testQuoteAndEscape2()
    {
        assertThat( StringUtils.quoteAndEscape( null, '+', new char[]{ '"' } )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+', new char[]{ '"' } )
                  , is( "" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"', new char[]{ '"' } )
                  , is( "abc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"', new char[]{ '"' } )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'', new char[]{ '"' } )
                  , is( "\'a\\'bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'' } )
                  , is( "a\"bc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'', '"' } )
                  , is( "\'a\"bc\'" ) );
    }

    @Test
    public void testQuoteAndEscape3()
    {
        assertThat( StringUtils.quoteAndEscape( null, '+', new char[]{ '"' }, '\\', false )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+', new char[]{ '"' }, '\\', false )
                  , is( "" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"', new char[]{ '"' }, '\\', false )
                  , is( "abc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"', new char[]{ '"' }, '\\', false )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'', new char[]{ '"' }, '\\', false )
                  , is( "a\'bc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'' }, '\\', false )
                  , is( "a\"bc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'', '"' }, '\\', false )
                  , is( "\'a\\\"bc\'" ) );

        // with force flag
        assertThat( StringUtils.quoteAndEscape( null, '+', new char[]{ '"' }, '\\', true )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+', new char[]{ '"' }, '\\', true )
                  , is( "++" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"', new char[]{ '"' }, '\\', true )
                  , is( "\"abc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"', new char[]{ '"' }, '\\', true )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'', new char[]{ '"' }, '\\', true )
                  , is( "\'a\'bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'' }, '\\', true )
                  , is( "\'a\"bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'', '"' }, '\\', true )
                  , is( "\'a\\\"bc\'" ) );
    }

    @Test
    public void testQuoteAndEscape4()
    {
        assertThat( StringUtils.quoteAndEscape( null, '+', new char[]{ '"' }, new char[]{ '"' }, '\\', false )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+', new char[]{ '"' },  new char[]{ '"' }, '\\', false )
                  , is( "" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"', new char[]{ '"' }, new char[]{ '"' }, '\\', false )
                  , is( "abc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"', new char[]{ '"' }, new char[]{ '"' }, '\\', false )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'', new char[]{ '"' }, new char[]{ '"' }, '\\', false )
                  , is( "a\'bc" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'' }, new char[]{ '"' }, '\\', false )
                  , is( "\'a\"bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "\'a\"bc\'", '\'', new char[]{ '\'', '"' }, new char[]{ '"' }, '\\', false )
                  , is( "\'a\"bc\'" ) );

        // with force flag
        assertThat( StringUtils.quoteAndEscape( null, '+', new char[]{ '"' }, new char[]{ '"' }, '\\', true )
                  , nullValue() );

        assertThat( StringUtils.quoteAndEscape( "", '+', new char[]{ '"' }, new char[]{ '"' }, '\\', true )
                  , is( "++" ) );

        assertThat( StringUtils.quoteAndEscape( "abc", '"', new char[]{ '"' }, new char[]{ '"' }, '\\', true )
                  , is( "\"abc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '"', new char[]{ '"' }, new char[]{ '"' }, '\\', true )
                  , is( "\"a\\\"bc\"" ) );

        assertThat( StringUtils.quoteAndEscape( "a\'bc", '\'', new char[]{ '"' }, new char[]{ '"' }, '\\', true )
                  , is( "\'a\'bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'' }, new char[]{ '"' }, '\\', true )
                  , is( "\'a\"bc\'" ) );

        assertThat( StringUtils.quoteAndEscape( "a\"bc", '\'', new char[]{ '\'', '"' }, new char[]{ '"' }, '\\', true )
                  , is( "\'a\\\"bc\'" ) );
    }

    @Test( expected = NullPointerException.class )
    public void testRemoveAndHump_NPE1()
    {
        StringUtils.removeAndHump( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testRemoveAndHump_NPE2()
    {
        StringUtils.removeAndHump( "dings", null );
    }

    @Test( expected = NullPointerException.class )
    public void testRemoveAndHump_NPE3()
    {
        StringUtils.removeAndHump( null, "bums" );
    }

    @Test
    public void testRemoveAndHump()
    {
        assertThat( StringUtils.removeAndHump( "dings", "bums" )
                  , is( "Ding" ) );

        assertThat( StringUtils.removeAndHump( "this-is-it", "-" )
                  , is( "ThisIsIt" ) );

        assertThat( StringUtils.removeAndHump( "THIS-IS-IT", "-" )
                  , is( "THISISIT" ) );

    }

    @Test( expected = NullPointerException.class )
    public void testRemoveDuplicateWhitespace_NPE()
    {
        StringUtils.removeDuplicateWhitespace( null );
    }

    @Test
    public void testRemoveDuplicateWhitespace()
    {
        assertThat( StringUtils.removeDuplicateWhitespace( "dings" )
                  , is( "dings" ) );

        assertThat( StringUtils.removeDuplicateWhitespace( "dings bums" )
                  , is( "dings bums" ) );

        assertThat( StringUtils.removeDuplicateWhitespace( "dings  bums" )
                  , is( "dings bums" ) );

        assertThat( StringUtils.removeDuplicateWhitespace( "dings \t bums" )
                  , is( "dings bums" ) );

    }

    @Test( expected = NullPointerException.class )
    public void testRepeat_NPE()
    {
        StringUtils.repeat( null, 0 );
    }

    @Test( expected = NegativeArraySizeException.class )
    public void testRepeat_NegativeAmount()
    {
        StringUtils.repeat( "dings", -1 );
    }


    @Test
    public void testRepeat()
    {
        assertThat( StringUtils.repeat( "dings", 0 )
                  , is( "" ) );

        assertThat( StringUtils.repeat( "dings", 1 )
                  , is( "dings" ) );

        assertThat( StringUtils.repeat( "dings", 3 )
                  , is( "dingsdingsdings" ) );
    }


    @Test
    public void testReplace_char()
    {
        assertThat( StringUtils.replace(  null, 'i', 'o' )
                  , nullValue() );

        assertThat( StringUtils.replace(  "dings", 'i', 'o' )
                  , is( "dongs" ) );

        assertThat( StringUtils.replace(  "dingsbims", 'i', 'o' )
                  , is( "dongsboms" ) );

        assertThat( StringUtils.replace(  "dings", 'x', 'o' )
                  , is( "dings" ) );
    }

    @Test
    public void testReplace2_char_max()
    {
        assertThat( StringUtils.replace(  null, 'i', 'o', 0 )
                  , nullValue() );

        assertThat( StringUtils.replace(  "dingsibumsi", 'i', 'o', 3 )
                  , is( "dongsobumso" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", 'i', 'o', 2 )
                  , is( "dongsobumsi" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", 'i', 'o', 0 )
                  , is( "dongsobumso" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", 'i', 'o', -2 )
                  , is( "dongsobumso" ) );

        assertThat( StringUtils.replace(  "dings", 'x', 'o', 2 )
                  , is( "dings" ) );
    }

    @Test
    public void testReplace_string()
    {
        assertThat( StringUtils.replace(  null, "in", "ox" )
                  , nullValue() );

        assertThat( StringUtils.replace(  "dings", "in", "ox" )
                  , is( "doxgs" ) );

        assertThat( StringUtils.replace(  "dingsbins", "in", "ox" )
                  , is( "doxgsboxs" ) );

        assertThat( StringUtils.replace(  "dings", "nin", "ox" )
                  , is( "dings" ) );
    }


    @Test
    public void testReplace2_string_max()
    {
        assertThat( StringUtils.replace(  null, "in", "ox", 0 )
                  , nullValue() );

        assertThat( StringUtils.replace(  "dingsibumsi", "si", "xo", 3 )
                  , is( "dingxobumxo" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", "si", "xo", 2 )
                  , is( "dingxobumxo" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", "si", "xo", 1 )
                  , is( "dingxobumsi" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", "si", "xo", 0 )
                  , is( "dingxobumxo" ) );

        assertThat( StringUtils.replace(  "dingsibumsi", "si", "xo", -2 )
                  , is( "dingxobumxo" ) );

        assertThat( StringUtils.replace(  "dings", "si", "xo", 2 )
                  , is( "dings" ) );
    }

    @Test
    public void testReplaceOnce_char()
    {
        assertThat( StringUtils.replaceOnce(  null, 'i', 'o' )
                  , nullValue() );

        assertThat( StringUtils.replaceOnce(  "dingsibumsi", 'i', 'o' )
                  , is( "dongsibumsi" ) );

        assertThat( StringUtils.replaceOnce(  "dings", 'x', 'o' )
                  , is( "dings" ) );
    }

    @Test
    public void testReplaceOnce_string()
    {
        assertThat( StringUtils.replaceOnce(  null, "in", "ox" )
                  , nullValue() );

        assertThat( StringUtils.replaceOnce(  "dingsibumsi", "si", "xo" )
                  , is( "dingxobumsi" ) );

        assertThat( StringUtils.replaceOnce(  "dings", "si", "xo" )
                  , is( "dings" ) );
    }

    
    @Test
    public void testReverse()
    {
        assertThat( StringUtils.reverse( null )
                  , nullValue() );

        assertThat( StringUtils.reverse( "" )
                  , is( "" ) );

        assertThat( StringUtils.reverse( "dings" )
                  , is( "sgnid" ) );

        assertThat( StringUtils.reverse( "  dings " )
                  , is( " sgnid  " ) );
    }

    @Test( expected = NullPointerException.class )
    public void testReverseDelimitedString_NPE1()
    {
        StringUtils.reverseDelimitedString( null, null );
    }

    @Test( expected = NullPointerException.class )
    public void testReverseDelimitedString_NPE2()
    {
        StringUtils.reverseDelimitedString( null, " " );
    }

    @Test
    public void testReverseDelimitedString()
    {
        assertThat( StringUtils.reverseDelimitedString( "dings", null )
                  , is( "dings" ) );

        assertThat( StringUtils.reverseDelimitedString( "", " " )
                  , is( "" ) );

        assertThat( StringUtils.reverseDelimitedString( "dings", " " )
                  , is( "dings" ) );

        assertThat( StringUtils.reverseDelimitedString( "  dings ", " " )
                  , is( "dings" ) );

        assertThat( StringUtils.reverseDelimitedString( "dings bums", " " )
                  , is( "bums dings" ) );
    }

    @Test
    public void testRight()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testRightPad1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testRightPad2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSplit1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSplit2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSplit3()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStrip1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStrip2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStripAll1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStripAll2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStripEnd()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testStripStart()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSubstring1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSubstring2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testSwapCase()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testTrim()
    {
        assertThat( StringUtils.trim( null )
                , nullValue() );

        assertThat( StringUtils.trim( "   " )
                  , is( "" ) );

        assertThat( StringUtils.trim( "  c " )
                  , is( "c" ) );

        assertThat( StringUtils.trim( "  dings \n  " )
                  , is( "dings" ) );
    }

    @Test
    public void testUncapitalise()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testUncapitaliseAllWords()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testUnifyLineSeparators1()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testUnifyLineSeparators2()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }

    @Test
    public void testUppercase()
    {
        System.out.println( "TODO IMPLEMENT TEST!" );
    }


}
