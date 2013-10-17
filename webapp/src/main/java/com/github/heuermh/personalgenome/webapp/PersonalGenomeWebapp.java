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
package com.github.heuermh.personalgenome.webapp;

import static spark.Spark.get;
import static spark.Spark.setPort;

import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeConverter;
import com.github.heuermh.personalgenome.client.Profile;
import com.github.heuermh.personalgenome.client.User;

import com.github.heuermh.personalgenome.client.scribe.ScribeModule;
import com.github.heuermh.personalgenome.client.scribe.ScribePersonalGenomeClient;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.nnsoft.guice.rocoto.configuration.ConfigurationModule;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Demo webapp for the 23andMe Personal Genome API.
 */
public final class PersonalGenomeWebapp {
    private static final Token EMPTY_TOKEN = null;
    private static final Logger logger = LoggerFactory.getLogger(PersonalGenomeWebapp.class);

    /**
     * Main.
     *
     * @param args command line arguments, ignored
     */
    public static void main(final String[] args) {
        setPort(8080);

        logger.info("initialization start...");

        final Injector injector = Guice.createInjector(new ParameterModule(), new ScribeModule());
        final OAuthService service = injector.getInstance(OAuthService.class);
        final PersonalGenomeConverter converter = injector.getInstance(PersonalGenomeConverter.class);

        get(new Route("/") {
                @Override
                public Object handle(final Request request, final Response response) {
                    logger.info("get /");

                    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
                    logger.info("received authorization url " + authorizationUrl);

                    return "<html><p>Click <a href=\"" + authorizationUrl + "\">" + authorizationUrl + "</a> to authorize.</p></html>";
                }
            });

        get(new Route("/after-auth-landing/") {
                @Override
                public Object handle(final Request request, final Response response) {
                    logger.info("get /after-auth-landing/");

                    String authorizationCode = request.queryParams("code");
                    logger.info("received authorization code " + authorizationCode);

                    Verifier verifier = new Verifier(authorizationCode);
                    Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
                    logger.info("received access token " + accessToken);

                    PersonalGenomeClient client = new ScribePersonalGenomeClient(accessToken, service, converter);

                    User user = client.user();
                    logger.info("retrieved user id " + user.getId());
                    /*
                    return "<html><p><b>Authorization code:</b> " + authorizationCode + "</p><p><b>Access token:</b> " + accessToken + "</p><p><b>User:</b> " + user.getId() + "</p></html>";
                    */

                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    for (Profile profile : user.getProfiles())
                    {
                        sb.append("<p>");
                        sb.append("<b>Profile id:</b> " + profile.getId());

                        Genotype genotype = client.genotypes(profile.getId(), "rs41362547");
                        logger.info("retrieved genotypes " + genotype);
                        sb.append(" <b>Genotype:</b> " + genotype.getValues());
                        sb.append("</p>");
                    }
                    sb.append("</html>");
                    return sb.toString();
                }
            });

        logger.info("initialization complete");
    }

    /**
     * Parameter module.
     */
    private static final class ParameterModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            // expect apiKey, apiSecret, callback, scope in system properties
            bindSystemProperties();
        }
    }
}