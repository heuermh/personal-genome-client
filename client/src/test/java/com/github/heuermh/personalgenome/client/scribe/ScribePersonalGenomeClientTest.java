/*

    personal-genome-client  Java client for the 23andMe Personal Genome API.
    Copyright (c) 2012 held jointly by the individual authors.

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package com.github.heuermh.personalgenome.client.scribe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

import java.io.InputStream;

import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;

import com.github.heuermh.personalgenome.client.AbstractPersonalGenomeClientTest;
import com.github.heuermh.personalgenome.client.AccessDeniedException;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.InvalidClientException;
import com.github.heuermh.personalgenome.client.InvalidRequestException;
import com.github.heuermh.personalgenome.client.InvalidScopeException;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeClientException;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.scribe.oauth.OAuthService;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;

/**
 * Unit test for ScribePersonalGenomeClient.
 */
public final class ScribePersonalGenomeClientTest extends AbstractPersonalGenomeClientTest {
    @Mock
    private OAuthService service;
    @Mock
    private Token accessToken;

    private static JsonFactory jsonFactory;

    @BeforeClass
    public static void staticSetUp() {
        jsonFactory = new JsonFactory();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }

    @Override
    protected PersonalGenomeClient createPersonalGenomeClient() {
        return new ScribePersonalGenomeClient(accessToken, service, jsonFactory);
    }

    @Test
    public void testCreateAndSignRequest() {
        OAuthRequest request = ((ScribePersonalGenomeClient) client).createAndSignRequest("http://localhost");
        assertNotNull(request);
        assertTrue(request.getHeaders().containsKey("Authorization"));
        verify(service).signRequest(eq(accessToken), any(OAuthRequest.class));
    }

    @Test
    public void testParseAccessDeniedException() {
        InputStream inputStream = getClass().getResourceAsStream("accessDenied.json");
        PersonalGenomeClientException exception = ((ScribePersonalGenomeClient) client).parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof AccessDeniedException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidClientException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidClient.json");
        PersonalGenomeClientException exception = ((ScribePersonalGenomeClient) client).parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidClientException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidRequestException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidRequest.json");
        PersonalGenomeClientException exception = ((ScribePersonalGenomeClient) client).parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidRequestException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidScopeException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidScope.json");
        PersonalGenomeClientException exception = ((ScribePersonalGenomeClient) client).parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidScopeException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseUnknownException() {
        InputStream inputStream = getClass().getResourceAsStream("unknown.json");
        PersonalGenomeClientException exception = ((ScribePersonalGenomeClient) client).parseException(inputStream);
        assertNotNull(exception);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseUser() {
        InputStream inputStream = getClass().getResourceAsStream("user.json");
        User user = ((ScribePersonalGenomeClient) client).parseUser(inputStream);
        assertNotNull(user);
        assertEquals("c3a110", user.getId());
        assertEquals(2, user.getProfiles().size());
        assertEquals("83a112", user.getProfiles().get(0).getId());
        assertTrue(user.getProfiles().get(0).getGenotyped());
        assertEquals("c92839", user.getProfiles().get(1).getId());
        assertTrue(user.getProfiles().get(1).getGenotyped());
    }

    @Test
    public void testParseNames() {
        InputStream inputStream = getClass().getResourceAsStream("names.json");
        UserName userName = ((ScribePersonalGenomeClient) client).parseNames(inputStream);
        assertNotNull(userName);
        assertEquals("c3a110", userName.getId());
        assertEquals("Gene", userName.getFirstName());
        assertEquals("Mendel", userName.getLastName());
        assertEquals(2, userName.getProfileNames().size());
        assertEquals("83a112", userName.getProfileNames().get(0).getId());
        assertEquals("Chip", userName.getProfileNames().get(0).getFirstName());
        assertEquals("Mendel", userName.getProfileNames().get(0).getLastName());
        assertEquals("c92839", userName.getProfileNames().get(1).getId());
        assertEquals("Gene", userName.getProfileNames().get(1).getFirstName());
        assertEquals("Mendel", userName.getProfileNames().get(1).getLastName());
    }

    @Test
    public void testParseHaplogroups() {
        InputStream inputStream = getClass().getResourceAsStream("haplogroups.json");
        List<Haplogroup> haplogroups = ((ScribePersonalGenomeClient) client).parseHaplogroups(inputStream);
        assertNotNull(haplogroups);
        assertEquals(1, haplogroups.size());
        assertEquals("c4480ba411939067", haplogroups.get(0).getProfileId());
        assertEquals("D2a1", haplogroups.get(0).getPaternal());
        assertEquals("D4e2", haplogroups.get(0).getMaternal());
        assertEquals(1, haplogroups.get(0).getPaternalTerminalSnps().size());
        assertEquals("i3000015", haplogroups.get(0).getPaternalTerminalSnps().get(0).getRsid());
        assertEquals("M125", haplogroups.get(0).getPaternalTerminalSnps().get(0).getSnp());
        assertEquals(2, haplogroups.get(0).getMaternalTerminalSnps().size());
        assertEquals("i3001424", haplogroups.get(0).getMaternalTerminalSnps().get(0).getRsid());
        assertEquals("15874", haplogroups.get(0).getMaternalTerminalSnps().get(0).getRcrsPosition());
        assertEquals("i5050411", haplogroups.get(0).getMaternalTerminalSnps().get(1).getRsid());
        assertEquals("15874", haplogroups.get(0).getMaternalTerminalSnps().get(1).getRcrsPosition());
    }

    @Test
    public void testParseGenotypes() {
        InputStream inputStream = getClass().getResourceAsStream("genotype.json");
        List<Genotype> genotypes = ((ScribePersonalGenomeClient) client).parseGenotypes(inputStream);
        assertNotNull(genotypes);
        assertEquals(2, genotypes.size());
        assertEquals("44aa40", genotypes.get(0).getProfileId());
        assertEquals("AA", genotypes.get(0).getValues().get("rs3094315"));
        assertEquals("TT", genotypes.get(0).getValues().get("rs3737728"));
        assertEquals("d37b1d", genotypes.get(1).getProfileId());
        assertEquals("AA", genotypes.get(1).getValues().get("rs3094315"));
        assertEquals("TT", genotypes.get(1).getValues().get("rs3737728"));
    }

    @Test
    public void testParseGenomes() {
        InputStream inputStream = getClass().getResourceAsStream("genomes.json");
        Genome genome = ((ScribePersonalGenomeClient) client).parseGenomes(inputStream);
        assertNotNull(genome);
        assertEquals("c4480ba411939067", genome.getProfileId());
        assertEquals("ACTAGTAG__TTGADDAAIICCTTDDTT", genome.getValues());
    }
}