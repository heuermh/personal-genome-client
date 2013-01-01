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

import com.fasterxml.jackson.core.JsonFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.google.inject.name.Named;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

/**
 * Scribe module.
 */
public final class ScribeModule extends AbstractModule {

    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    JsonFactory createJsonFactory() {
        return new JsonFactory();
    }

    @Provides @Singleton
    OAuthService createOAuthService(@Named("apiKey") final String apiKey,
                                    @Named("apiSecret") final String apiSecret,
                                    @Named("callback") final String callback,
                                    @Named("scope") final String scope) {
        return new ServiceBuilder()
            .provider(PersonalGenomeApi.class)
            .apiKey(apiKey)
            .apiSecret(apiSecret)
            .callback(callback)
            .scope(scope)
            .build();
    }
}