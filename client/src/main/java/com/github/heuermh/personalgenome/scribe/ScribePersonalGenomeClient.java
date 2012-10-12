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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.inject.Inject;

import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Implementation of PersonalGenomeClient based on Scribe.
 *
 * See <a href="https://github.com/fernandezpablo85/scribe-java">https://github.com/fernandezpablo85/scribe-java</a>
 */
final class ScribePersonalGenomeClient implements PersonalGenomeClient {
    private final OAuthService service;
    private static final String USER_URL = "https://api.23andme.com/1/user";
    private static final String NAMES_URL = "https://api.23andme.com/1/names";
    private static final String HAPLOGROUPS_URL = "https://api.23andme.com/1/haplogroups";
    private static final String GENOTYPE_URL = "https://api.23andme.com/1/genotype";

    @Inject
    ScribePersonalGenomeClient(final OAuthService service) {
        checkNotNull(service);
        this.service = service;
    }

    @Override
    public User user() {
        Token requestToken = service.getRequestToken();
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        Verifier verifier = new Verifier("");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, USER_URL);
        request.addHeader("Authorization", String.format("Bearer %s", accessToken));
        service.signRequest(accessToken, request);
        Response response = request.send();
        int code = response.getCode();
        String body = response.getBody();

        return null;
    }

    @Override
    public UserName names() {
        return null;
    }

    @Override
    public List<Haplogroup> haplogroups() {
        return null;
    }

    @Override
    public List<Genotype> genotypes() {
        return null;
    }
}