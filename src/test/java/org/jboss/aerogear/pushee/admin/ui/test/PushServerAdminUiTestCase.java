/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.pushee.admin.ui.test;

import static org.junit.Assert.assertTrue;

import org.jboss.aerogear.pushee.admin.ui.page.LoginPage;
import org.jboss.aerogear.pushee.admin.ui.page.PasswordChangePage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppsEditPage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppsPage;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

public class PushServerAdminUiTestCase extends AbstractPushServerAdminUiTest {

    @Override
    protected String getPagePath() {
        return "";
    }

    @Page
    private LoginPage loginPage;

    @Page
    private PasswordChangePage passwordChangePage;

    @Page
    private PushAppsPage pushAppsPage;

    @Page
    private PushAppsEditPage pushAppsEditPage;

    @Test
    @InSequence(1)
    public void login() {
        // initialize page
        initializePageUrl();
        // wait until login page is loaded
        loginPage.waitUntilPageIsLoaded();
        // perform login
        loginPage.login(ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
        // wait until password change page is loaded
        passwordChangePage.waitUntilPageIsLoaded();
        // change password
        passwordChangePage.changePassword(NEW_ADMIN_PASSWORD);
    }

    @Test
    @InSequence(2)
    public void testPushAppRegistration() {
        // wait until push apps page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // initially there shouldn't exist any push applications
        assertTrue("Initially there are 0 push apps", pushAppsPage.countPushApps() == 0);
        // register a new push application
        pushAppsPage.pressCreateButton();
        // wait until edit page is loaded
        pushAppsEditPage.waitUntilPageIsLoaded();
        // register a push application
        pushAppsEditPage.registerNewPushApp(PUSH_APP_NAME, PUSH_APP_DESC);
        // navigate to push apps page
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should exist 1 push app", pushAppsPage.countPushApps() == 1);
    }

    private static final String ADMIN_USERNAME = "admin";

    private static final String DEFAULT_ADMIN_PASSWORD = "123";

    private static final String NEW_ADMIN_PASSWORD = "aerogear";

    private static final String PUSH_APP_NAME = "MyApp";

    private static final String PUSH_APP_DESC = "Awesome app!";
}