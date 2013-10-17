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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Joiner;

import com.github.heuermh.personalgenome.client.Ancestry;
import com.github.heuermh.personalgenome.client.Carrier;
import com.github.heuermh.personalgenome.client.DrugResponse;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeConverter;
import com.github.heuermh.personalgenome.client.Relative;
import com.github.heuermh.personalgenome.client.Risk;
import com.github.heuermh.personalgenome.client.Trait;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of PersonalGenomeClient based on Scribe.
 *
 * See <a href="https://github.com/fernandezpablo85/scribe-java">https://github.com/fernandezpablo85/scribe-java</a>
 */
public final class ScribePersonalGenomeClient implements PersonalGenomeClient {
    private final Token accessToken;
    private final OAuthService service;
    private final PersonalGenomeConverter converter;
    private final Logger logger = LoggerFactory.getLogger(ScribePersonalGenomeClient.class);
    private static final String USER_URL = "https://api.23andme.com/1/user";
    private static final String NAMES_URL = "https://api.23andme.com/1/names/%s/";
    private static final String HAPLOGROUPS_URL = "https://api.23andme.com/1/haplogroups/%s/";
    private static final String GENOTYPE_URL = "https://api.23andme.com/1/genotype/%s/?locations=%s";
    private static final String GENOMES_URL = "https://api.23andme.com/1/genomes/%s/";
    private static final String ANCESTRY_URL = "https://api.23andme.com/1/ancestry/%s/?threshold=%f";
    private static final String NEANDERTHAL_URL = "https://api.23andme.com/1/neanderthal/%s/";
    private static final String RELATIVES_URL = "https://api.23andme.com/1/relatives/%s/?limit=%f&offset=%f";
    private static final String RISKS_URL = "https://api.23andme.com/1/risks/%s/";
    private static final String CARRIERS_URL = "https://api.23andme.com/1/carriers/%s/";
    private static final String DRUG_RESPONSES_URL = "https://api.23andme.com/1/drug_responses/%s/";
    private static final String TRAITS_URL = "https://api.23andme.com/1/traits/%s/";

    //@Inject
    public ScribePersonalGenomeClient(final Token accessToken, final OAuthService service, final PersonalGenomeConverter converter) {
        checkNotNull(accessToken);
        checkNotNull(service);
        checkNotNull(converter);
        this.accessToken = accessToken;
        this.service = service;
        this.converter = converter;
    }

    @Override
    public User user() {
        OAuthRequest request = createAndSignRequest(USER_URL);
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("user call ok");
            return converter.parseUser(response.getStream());
        }
        logger.warn("could not call user, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public UserName names(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(NAMES_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("names call ok");
            return converter.parseNames(response.getStream());
        }
        logger.warn("could not call names, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public Haplogroup haplogroups(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(HAPLOGROUPS_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("haplogroups call ok");
            return converter.parseHaplogroups(response.getStream());
        }
        logger.warn("could not call haplogroups, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public Genotype genotypes(final String profileId, final String... locations) {
        checkNotNull(profileId);
        checkNotNull(locations);
        return genotypesWithScope(profileId, Joiner.on(" ").join(locations));
    }

    @Override
    public Genotype genotypes(final String profileId, final Iterable<String> locations) {
        checkNotNull(profileId);
        checkNotNull(locations);
        return genotypesWithScope(profileId, Joiner.on(" ").join(locations));
    }

    Genotype genotypesWithScope(final String profileId, final String scope) {
        OAuthRequest request = createAndSignRequest(String.format(GENOTYPE_URL, profileId, scope));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("genotype call ok");
            return converter.parseGenotypes(response.getStream());
        }
        logger.warn("could not call genotype, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public Genome genome(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(GENOMES_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("genomes call ok");
            return converter.parseGenomes(response.getStream());
        }
        logger.warn("could not call genomes, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public Ancestry ancestry(final String profileId, final double threshold) {
        checkNotNull(profileId);
        if (threshold <= 0.5 || threshold >= 1.0) {
            throw new IllegalArgumentException("threshold must be in the range (0.5, 1.0), exclusive");
        }
        OAuthRequest request = createAndSignRequest(String.format(ANCESTRY_URL, profileId, threshold));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("ancestry call ok");
            return converter.parseAncestry(response.getStream());
        }
        logger.warn("could not call ancestry, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public double neanderthalProportion(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(NEANDERTHAL_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("neanderthal proportion call ok");
            return converter.parseNeanderthalProportion(response.getStream());
        }
        logger.warn("could not call neanderthal proportion, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public Iterator<Relative> relatives(final String profileId) {
        checkNotNull(profileId);
        // todo: used chunked iterator
        OAuthRequest request = createAndSignRequest(String.format(RELATIVES_URL, profileId, 0, 100));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("relatives call ok");
            return converter.parseRelatives(response.getStream()).iterator();
        }
        logger.warn("could not call relatives, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public List<Relative> relatives(final String profileId, final int offset, final int limit) {
        checkNotNull(profileId);
        // note limit and offset are in a different order here
        OAuthRequest request = createAndSignRequest(String.format(RELATIVES_URL, profileId, limit, offset));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("relatives call ok");
            return converter.parseRelatives(response.getStream());
        }
        logger.warn("could not call relatives, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public List<Risk> risks(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(RISKS_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("risks call ok");
            return converter.parseRisks(response.getStream());
        }
        logger.warn("could not call risks, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public List<Carrier> carriers(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(CARRIERS_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("carriers call ok");
            return converter.parseCarriers(response.getStream());
        }
        logger.warn("could not call carriers, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public List<DrugResponse> drugResponses(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(DRUG_RESPONSES_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("drug responses call ok");
            return converter.parseDrugResponses(response.getStream());
        }
        logger.warn("could not call drug responses, response code " + code);
        throw converter.parseException(response.getStream());
    }

    @Override
    public List<Trait> traits(final String profileId) {
        checkNotNull(profileId);
        OAuthRequest request = createAndSignRequest(String.format(TRAITS_URL, profileId));
        Response response = request.send();
        int code = response.getCode();

        if (code == 200) {
            logger.trace("traits call ok");
            return converter.parseTraits(response.getStream());
        }
        logger.warn("could not call traits, response code " + code);
        throw converter.parseException(response.getStream());
    }

    OAuthRequest createAndSignRequest(final String url) {
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        request.addHeader("Authorization", String.format("Bearer %s", accessToken.getToken()));
        service.signRequest(accessToken, request);
        return request;
    }
}