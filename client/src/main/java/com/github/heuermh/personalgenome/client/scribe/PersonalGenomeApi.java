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

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.utils.OAuthEncoder;

/**
 * Personal genome API.
 */
public final class PersonalGenomeApi extends DefaultApi20 {
    private static final String ACCESS_TOKEN_URL = "https://api.23andme.com/token";
    private static final String AUTHORIZE_URL = "https://api.23andme.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&scope=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_URL;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAuthorizationUrl(final OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), config.getScope());
    }

    @Override
    public OAuthService createService(final OAuthConfig config) {
        return new OAuth20ServiceImpl(this, config) {
            @Override
            public Token getAccessToken(final Token requestToken, final Verifier verifier)
            {
                OAuthRequest request = new OAuthRequest(PersonalGenomeApi.this.getAccessTokenVerb(), PersonalGenomeApi.this.getAccessTokenEndpoint());
                request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
                request.addQuerystringParameter("grant_type", "authorization_code"); // todo:  move this to OAuth20ServiceImpl.java ?
                request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
                request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
                if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());

                System.out.println("request=" + request.toString());
                System.out.println("headers=" + request.getHeaders().toString());
                System.out.println("request queryStringParameters=" + request.getQueryStringParams().asOauthBaseString());
                System.out.println("request queryStringParameters=" + request.getQueryStringParams().asFormUrlEncodedString());
                System.out.println("request oauthParameters=" + request.getOauthParameters().toString());

                Response response = request.send();
                return PersonalGenomeApi.this.getAccessTokenExtractor().extract(response.getBody());
            }
        };
    }
}