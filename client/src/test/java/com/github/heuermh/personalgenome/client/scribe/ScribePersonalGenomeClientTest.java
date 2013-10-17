/*

    personal-genome-client  Java client for the 23andMe Personal Genome API.
    Copyright (c) 2012-2013 held jointly by the individual authors.

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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

import com.github.heuermh.personalgenome.client.AbstractPersonalGenomeClientTest;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeConverter;

import org.junit.Before;
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
    private ScribePersonalGenomeClient scribeClient;

    @Mock
    private OAuthService service;
    @Mock
    private Token accessToken;
    @Mock
    private PersonalGenomeConverter converter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        scribeClient = (ScribePersonalGenomeClient) client;
    }

    @Override
    protected PersonalGenomeClient createPersonalGenomeClient() {
        return new ScribePersonalGenomeClient(accessToken, service, converter);
    }

    @Test
    public void testCreateAndSignRequest() {
        OAuthRequest request = scribeClient.createAndSignRequest("http://localhost");
        assertNotNull(request);
        assertTrue(request.getHeaders().containsKey("Authorization"));
        verify(service).signRequest(eq(accessToken), any(OAuthRequest.class));
    }
}