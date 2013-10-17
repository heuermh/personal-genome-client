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

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.google.inject.name.Names;

/**
 * Unit test for ScribeModule.
 */
public final class ScribeModuleTest {
    private ScribeModule scribeModule;

    @Before
    public void setUp() {
        scribeModule = new ScribeModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(scribeModule);
    }

    @Test
    public void testScribeModule() {
        Injector injector = Guice.createInjector(new ClientModule(), scribeModule);
        assertNotNull(injector);
    }

    /**
     * Test module that provides @Named constant values.
     */
    private static class ClientModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Names.named("apiKey")).toInstance("apiKey");
            bind(String.class).annotatedWith(Names.named("apiSecret")).toInstance("apiSecret");
            bind(String.class).annotatedWith(Names.named("callback")).toInstance("callback");
            bind(String.class).annotatedWith(Names.named("scope")).toInstance("scope");
        }
    }
}