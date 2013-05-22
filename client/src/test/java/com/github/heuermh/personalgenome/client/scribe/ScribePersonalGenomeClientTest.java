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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

import java.io.InputStream;

import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;

import com.github.heuermh.personalgenome.client.AbstractPersonalGenomeClientTest;
import com.github.heuermh.personalgenome.client.Ancestry;
import com.github.heuermh.personalgenome.client.AccessDeniedException;
import com.github.heuermh.personalgenome.client.Carrier;
import com.github.heuermh.personalgenome.client.DrugResponse;
import com.github.heuermh.personalgenome.client.Haplogroup;
import com.github.heuermh.personalgenome.client.InvalidClientException;
import com.github.heuermh.personalgenome.client.InvalidRequestException;
import com.github.heuermh.personalgenome.client.InvalidScopeException;
import com.github.heuermh.personalgenome.client.Genome;
import com.github.heuermh.personalgenome.client.Genotype;
import com.github.heuermh.personalgenome.client.PersonalGenomeClient;
import com.github.heuermh.personalgenome.client.PersonalGenomeClientException;
import com.github.heuermh.personalgenome.client.Relative;
import com.github.heuermh.personalgenome.client.Risk;
import com.github.heuermh.personalgenome.client.Trait;
import com.github.heuermh.personalgenome.client.User;
import com.github.heuermh.personalgenome.client.UserName;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

    private static JsonFactory jsonFactory;

    @BeforeClass
    public static void staticSetUp() {
        jsonFactory = new JsonFactory();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        scribeClient = (ScribePersonalGenomeClient) client;
    }

    @Override
    protected PersonalGenomeClient createPersonalGenomeClient() {
        return new ScribePersonalGenomeClient(accessToken, service, jsonFactory);
    }

    @Test
    public void testCreateAndSignRequest() {
        OAuthRequest request = scribeClient.createAndSignRequest("http://localhost");
        assertNotNull(request);
        assertTrue(request.getHeaders().containsKey("Authorization"));
        verify(service).signRequest(eq(accessToken), any(OAuthRequest.class));
    }

    @Test
    public void testParseAccessDeniedException() {
        InputStream inputStream = getClass().getResourceAsStream("accessDenied.json");
        PersonalGenomeClientException exception = scribeClient.parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof AccessDeniedException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidClientException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidClient.json");
        PersonalGenomeClientException exception = scribeClient.parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidClientException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidRequestException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidRequest.json");
        PersonalGenomeClientException exception = scribeClient.parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidRequestException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseInvalidScopeException() {
        InputStream inputStream = getClass().getResourceAsStream("invalidScope.json");
        PersonalGenomeClientException exception = scribeClient.parseException(inputStream);
        assertNotNull(exception);
        assertTrue(exception instanceof InvalidScopeException);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseUnknownException() {
        InputStream inputStream = getClass().getResourceAsStream("unknown.json");
        PersonalGenomeClientException exception = scribeClient.parseException(inputStream);
        assertNotNull(exception);
        assertEquals("error description", exception.getMessage());
    }

    @Test
    public void testParseUser() {
        InputStream inputStream = getClass().getResourceAsStream("user.json");
        User user = scribeClient.parseUser(inputStream);
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
        UserName userName = scribeClient.parseNames(inputStream);
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
        Haplogroup haplogroup = scribeClient.parseHaplogroups(inputStream);
        assertNotNull(haplogroup);
        assertEquals("c4480ba411939067", haplogroup.getProfileId());
        assertEquals("D2a1", haplogroup.getPaternal());
        assertEquals("D4e2", haplogroup.getMaternal());
        assertEquals(1, haplogroup.getPaternalTerminalSnps().size());
        assertEquals("i3000015", haplogroup.getPaternalTerminalSnps().get(0).getRsid());
        assertEquals("M125", haplogroup.getPaternalTerminalSnps().get(0).getSnp());
        assertEquals(2, haplogroup.getMaternalTerminalSnps().size());
        assertEquals("i3001424", haplogroup.getMaternalTerminalSnps().get(0).getRsid());
        assertEquals("15874", haplogroup.getMaternalTerminalSnps().get(0).getRcrsPosition());
        assertEquals("i5050411", haplogroup.getMaternalTerminalSnps().get(1).getRsid());
        assertEquals("15874", haplogroup.getMaternalTerminalSnps().get(1).getRcrsPosition());
    }

    @Test
    public void testParseGenotypes() {
        InputStream inputStream = getClass().getResourceAsStream("genotype.json");
        Genotype genotype = scribeClient.parseGenotypes(inputStream);
        assertNotNull(genotype);
        assertEquals("44aa40", genotype.getProfileId());
        assertEquals("AA", genotype.getValues().get("rs3094315"));
        assertEquals("TT", genotype.getValues().get("rs3737728"));
    }

    @Test
    public void testParseGenomes() {
        InputStream inputStream = getClass().getResourceAsStream("genomes.json");
        Genome genome = scribeClient.parseGenomes(inputStream);
        assertNotNull(genome);
        assertEquals("c4480ba411939067", genome.getProfileId());
        assertEquals("ACTAGTAG__TTGADDAAIICCTTDDTT", genome.getValues());
    }

    @Test
    public void testParseAncestry() {
        InputStream inputStream = getClass().getResourceAsStream("ancestry.json");
        Ancestry ancestry = scribeClient.parseAncestry(inputStream);
        assertNotNull(ancestry);
        assertEquals("7ad467ea509080fb", ancestry.getProfileId());
        assertEquals("Total", ancestry.getLabel());
        assertEquals(1.0d, ancestry.getProportion(), 0.1d);
        assertEquals(0.0d, ancestry.getUnassigned(), 0.1d);
        assertNotNull(ancestry.getSubPopulations());
        assertEquals(2, ancestry.getSubPopulations().size());

        for (Ancestry subPopulation : ancestry.getSubPopulations()) {
            if ("Sub-Saharan African".equals(subPopulation.getLabel())) {
                assertEquals(0.8827d, subPopulation.getProportion(), 0.1d);
                assertEquals(0.0d, subPopulation.getUnassigned(), 0.1d);
                assertNotNull(subPopulation.getSubPopulations());
                assertTrue(subPopulation.getSubPopulations().isEmpty());
            }
            else if ("European".equals(subPopulation.getLabel())) {
                assertEquals(0.1773d, subPopulation.getProportion(), 0.1d);
                assertEquals(0.0193d, subPopulation.getUnassigned(), 0.1d);
                assertNotNull(subPopulation.getSubPopulations());
                assertEquals(1, subPopulation.getSubPopulations().size());

                Ancestry subSubPopulation = subPopulation.getSubPopulations().get(0);
                assertEquals("Northern European", subSubPopulation.getLabel());
                assertEquals(0.1579d, subSubPopulation.getProportion(), 0.1d);
                assertEquals(0.0725d, subSubPopulation.getUnassigned(), 0.1d);
                assertNotNull(subSubPopulation.getSubPopulations());
                assertEquals(1, subSubPopulation.getSubPopulations().size());

                Ancestry subSubSubPopulation = subSubPopulation.getSubPopulations().get(0);
                assertEquals("French and German", subSubSubPopulation.getLabel());
                assertEquals(0.1579d, subSubSubPopulation.getProportion(), 0.1d);
                assertEquals(0.0725d, subSubSubPopulation.getUnassigned(), 0.1d);
                assertNotNull(subSubSubPopulation.getSubPopulations());
                assertTrue(subSubSubPopulation.getSubPopulations().isEmpty());
            }
            else {
                fail("unexpected subpopulation label");
            }
        }
    }

    @Test
    public void testParseAncestryFlat() {
        InputStream inputStream = getClass().getResourceAsStream("ancestryFlat.json");
        Ancestry ancestry = scribeClient.parseAncestry(inputStream);
        assertNotNull(ancestry);
        assertEquals("7ad467ea509080fb", ancestry.getProfileId());
        assertEquals("Total", ancestry.getLabel());
        assertEquals(1.0d, ancestry.getProportion(), 0.1d);
        assertEquals(0.0d, ancestry.getUnassigned(), 0.1d);
        assertNotNull(ancestry.getSubPopulations());
        assertTrue(ancestry.getSubPopulations().isEmpty());
    }

    @Test
    public void testParseNeanderthalProportion() {
        InputStream inputStream = getClass().getResourceAsStream("neanderthal.json");
        double neanderthalProportion = scribeClient.parseNeanderthalProportion(inputStream);
        assertEquals(0.0310d, neanderthalProportion, 0.1d);
    }

    @Ignore
    public void testParseRelatives() {
        InputStream inputStream = getClass().getResourceAsStream("relatives.json");
        List<Relative> relatives = scribeClient.parseRelatives(inputStream);
        assertNotNull(relatives);
        assertEquals(1, relatives.size());
        Relative relative = relatives.get(0);
        assertNotNull(relative);
    }

    @Test
    public void testParseRisks() {
        InputStream inputStream = getClass().getResourceAsStream("risks.json");
        List<Risk> risks = scribeClient.parseRisks(inputStream);
        assertNotNull(risks);
        assertEquals(2, risks.size());
        Risk risk0 = risks.get(0);
        assertNotNull(risk0);
        assertEquals("c4480ba411939067", risk0.getProfileId());
        assertEquals("atrialfib", risk0.getReportId());
        assertEquals("Atrial Fibrillation", risk0.getDescription());
        assertEquals(0.4164d, risk0.getRisk(), 0.1d);
        assertEquals(0.2715d, risk0.getPopulationRisk(), 0.1d);
        Risk risk1 = risks.get(1);
        assertNotNull(risk1);
        assertEquals("c4480ba411939067", risk1.getProfileId());
        assertEquals("prostate", risk1.getReportId());
        assertEquals("Prostate Cancer", risk1.getDescription());
        assertEquals(0.2585d, risk1.getRisk(), 0.1d);
        assertEquals(0.1783d, risk1.getPopulationRisk(), 0.1d);
    }

    @Test
    public void testParseCarriers() {
        InputStream inputStream = getClass().getResourceAsStream("carriers.json");
        List<Carrier> carriers = scribeClient.parseCarriers(inputStream);
        assertNotNull(carriers);
        assertEquals(2, carriers.size());
        Carrier carrier0 = carriers.get(0);
        assertNotNull(carrier0);
        assertEquals("c4480ba411939067", carrier0.getProfileId());
        assertEquals("tay_sachs", carrier0.getReportId());
        assertEquals("Tay-Sachs Disease", carrier0.getDescription());
        assertEquals(1, carrier0.getMutations());
        Carrier carrier1 = carriers.get(1);
        assertNotNull(carrier1);
        assertEquals("c4480ba411939067", carrier1.getProfileId());
        assertEquals("cf_panel", carrier1.getReportId());
        assertEquals("Cystic Fibrosis", carrier1.getDescription());
        assertEquals(2, carrier1.getMutations());
    }

    @Test
    public void testParseDrugResponses() {
        InputStream inputStream = getClass().getResourceAsStream("drugResponses.json");
        List<DrugResponse> drugResponses = scribeClient.parseDrugResponses(inputStream);
        assertNotNull(drugResponses);
        assertEquals(2, drugResponses.size());
        DrugResponse drugResponse0 = drugResponses.get(0);
        assertNotNull(drugResponse0);
        assertEquals("c4480ba411939067", drugResponse0.getProfileId());
        assertEquals("alcohol_esophageal_pgx", drugResponse0.getReportId());
        assertEquals("Alcohol Consumption, Smoking and Risk of Esophageal Cancer", drugResponse0.getDescription());
        assertEquals("typical", drugResponse0.getStatus());
        DrugResponse drugResponse1 = drugResponses.get(1);
        assertNotNull(drugResponse1);
        assertEquals("c4480ba411939067", drugResponse1.getProfileId());
        assertEquals("hepc_peginf_ribavirin", drugResponse1.getReportId());
        assertEquals("Response to Hepatitis C Treatment", drugResponse1.getDescription());
        assertEquals("reduced", drugResponse1.getStatus());
    }

    @Test
    public void testParseTraits() {
        InputStream inputStream = getClass().getResourceAsStream("traits.json");
        List<Trait> traits = scribeClient.parseTraits(inputStream);
        assertNotNull(traits);
        assertEquals(2, traits.size());
        Trait trait0 = traits.get(0);
        assertNotNull(trait0);
        assertEquals("c4480ba411939067", trait0.getProfileId());
        assertEquals("muscleperformance", trait0.getReportId());
        assertEquals("Muscle Performance", trait0.getDescription());
        assertEquals("Unlikely Sprinter", trait0.getTrait());
        assertNotNull(trait0.getPossibleTraits());
        assertEquals(2, trait0.getPossibleTraits().size());
        assertTrue(trait0.getPossibleTraits().contains("Likely Sprinter"));
        assertTrue(trait0.getPossibleTraits().contains("Unlikely Sprinter"));
        Trait trait1 = traits.get(1);
        assertNotNull(trait1);
        assertEquals("c4480ba411939067", trait1.getProfileId());
        assertEquals("hiv", trait1.getReportId());
        assertEquals("Resistance to HIV/AIDS", trait1.getDescription());
        assertEquals("Not Resistant", trait1.getTrait());
        assertNotNull(trait1.getPossibleTraits());
        assertEquals(2, trait1.getPossibleTraits().size());
        assertTrue(trait1.getPossibleTraits().contains("Not Resistant"));
        assertTrue(trait1.getPossibleTraits().contains("Partially Resistant"));
    }
}