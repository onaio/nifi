/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.oauth;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestOAuth2ClientCredentialsGrantControllerService {
    private static final String SERVICE_ID = "OAuth2ClientCredentialsGrant";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestOAuth2ClientCredentialsGrantControllerService.class);
    private TestRunner testRunner;

    public static class SampleProcessor extends AbstractProcessor {
        @Override
        public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {

        }
    }

    @Before
    public void init() throws Exception {
        testRunner = TestRunners.newTestRunner(SampleProcessor.class);
    }

    @Test
    @Ignore
    public void testOnEnabled() throws InitializationException {
        OAuth2ClientCredentialsGrantControllerService service = new OAuth2ClientCredentialsGrantControllerService();
        testRunner.addControllerService(SERVICE_ID, service);
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.AUTH_SERVER_URL, "https://api.twitter.com/oauth2/token");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.CLIENT_ID, "dsfds");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.CLIENT_SECRET, "fdsfds");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.RESPONSE_ACCESS_TOKEN_FIELD_NAME, "access_token");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.RESPONSE_EXPIRE_IN_FIELD_NAME, "expire_in");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.RESPONSE_EXPIRE_TIME_FIELD_NAME, "expire_time");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.RESPONSE_SCOPE_FIELD_NAME, "scope");
        testRunner.setProperty(service, OAuth2ClientCredentialsGrantControllerService.RESPONSE_TOKEN_TYPE_FIELD_NAME, "token_type");
        try {
            testRunner.enableControllerService(service);
        } catch (AssertionError e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testHttpsValidator() {
        
    }

    public void testAuthenticate() {

    }

    public void testIsOAuthTokenExpired() {

    }

    public void testPropertyFields() {

    }
}
