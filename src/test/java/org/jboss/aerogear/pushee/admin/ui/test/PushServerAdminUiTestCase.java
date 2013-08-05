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

import static org.jboss.aerogear.pushee.admin.ui.utils.StringUtilities.isEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.aerogear.pushee.admin.ui.model.AbstractVariant;
import org.jboss.aerogear.pushee.admin.ui.model.PushApplication;
import org.jboss.aerogear.pushee.admin.ui.model.VariantType;
import org.jboss.aerogear.pushee.admin.ui.page.AndroidVariantEditPage;
import org.jboss.aerogear.pushee.admin.ui.page.LoginPage;
import org.jboss.aerogear.pushee.admin.ui.page.PasswordChangePage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppEditPage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppsPage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppsPage.PUSH_APP_LINK;
import org.jboss.aerogear.pushee.admin.ui.page.VariantRegistrationPage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppDetailsPage;
import org.jboss.aerogear.pushee.admin.ui.page.PushAppDetailsPage.VARIANT_LINK;
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
    private PushAppEditPage pushAppEditPage;

    @Page
    private PushAppDetailsPage pushAppDetailsPage;

    @Page
    private VariantRegistrationPage variantRegistrationPage;

    @Page
    private AndroidVariantEditPage androidVariantEditPage;

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
        pushAppEditPage.waitUntilPageIsLoaded();
        // register a push application
        pushAppEditPage.registerNewPushApp(PUSH_APP_NAME, PUSH_APP_DESC);
        // navigate to push apps page
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // there should exist one push application
        assertTrue("There should exist 1 push app", pushAppsList != null && pushAppsList.size() == 1);
        // The push app row should contain the right info name, desc, variants
        assertTrue(pushAppsList != null);
        assertEquals(PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(0, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(3)
    public void testPushAppCancellation() {
        // wait until push apps page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should exist 1 push app", pushAppsPage.countPushApps() == 1);
        pushAppsPage.pressCreateButton();
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // register a push application
        pushAppEditPage.cancel();
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
    }

    @Test
    @InSequence(4)
    public void testPushAppEdit() {
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
        // press the edit link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.EDIT);
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // the push app details should be the expected ones
        assertEquals(PUSH_APP_NAME, pushAppEditPage.getName());
        assertEquals(PUSH_APP_DESC, pushAppEditPage.getDescription());
        // update the push application name
        pushAppEditPage.updatePushApp(UPDATED_PUSH_APP_NAME, UPDATED_PUSH_APP_DESC);
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // The push app row should contain the updated info name, desc
        assertTrue(pushAppsList != null);
        assertEquals(UPDATED_PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(UPDATED_PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(0, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(5)
    public void testAndroidVariantRegistration() {
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
        // press the variants link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        pushAppDetailsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(pushAppDetailsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        assertTrue(!isEmpty(pushAppDetailsPage.getApplicationId()) && !isEmpty(pushAppDetailsPage.getMasterSecret()));
        // initially there are zero variants
        assertTrue("initially there are zero variants", pushAppDetailsPage.countVariants() == 0);
        // add a new variant
        pushAppDetailsPage.pressAddVariantButton();
        // register new android variant
        variantRegistrationPage.registerAndroidVariant(ANDROID_VARIANT_NAME, ANDROID_VARIANT_DESC, ANDROID_VARIANT_GOOGLE_KEY);
        // wait until page is loaded
        pushAppDetailsPage.waitUntilPageIsLoaded();
        // One variant should exist
        final List<AbstractVariant> variantList = pushAppDetailsPage.getVariantList();
        // one variant should exist
        assertTrue(variantList != null);
        assertEquals(ANDROID_VARIANT_NAME, variantList.get(0).getName());
        assertEquals(ANDROID_VARIANT_DESC, variantList.get(0).getDescription());
        assertEquals(VariantType.ANDROID, variantList.get(0).getVariantType());
        assertEquals(0, variantList.get(0).getInstallations());
        // go to push apps page
        pushAppDetailsPage.navigateToPushAppsPage();
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // The variant counter should be updated to 1
        assertTrue(pushAppsList != null);
        assertEquals(UPDATED_PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(UPDATED_PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(1, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(6)
    public void testAndroidVariantEdit() {
        // press the variants link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        pushAppDetailsPage.waitUntilPageIsLoaded();
        // there should exist one variant
        assertTrue("There should still exist 1 push app", pushAppDetailsPage.countVariants() == 1);
        // press the variants edit link
        pushAppDetailsPage.pressVariantLink(0, VARIANT_LINK.EDIT);
        // wait until page is loaded
        androidVariantEditPage.waitUntilPageIsLoaded();
        // the variant details should be the expected ones
        assertEquals(ANDROID_VARIANT_NAME, androidVariantEditPage.getName());
        assertEquals(ANDROID_VARIANT_DESC, androidVariantEditPage.getDescription());
        assertEquals(ANDROID_VARIANT_GOOGLE_KEY, androidVariantEditPage.getGoogleApiKey());
        // register new android variant
        androidVariantEditPage.updateVariant(UPDATED_ANDROID_VARIANT_NAME, UPDATED_ANDROID_VARIANT_DESC,
                UPDATED_ANDROID_VARIANT_GOOGLE_KEY);
        // wait until page is loaded
        pushAppDetailsPage.waitUntilPageIsLoaded();
        // one variant should exist
        final List<AbstractVariant> variantList = pushAppDetailsPage.getVariantList();
        assertTrue(variantList != null);
        assertEquals(UPDATED_ANDROID_VARIANT_NAME, variantList.get(0).getName());
        assertEquals(UPDATED_ANDROID_VARIANT_DESC, variantList.get(0).getDescription());
        assertEquals(VariantType.ANDROID, variantList.get(0).getVariantType());
        assertEquals(0, variantList.get(0).getInstallations());
        pushAppDetailsPage.pressVariantLink(0, VARIANT_LINK.EDIT);
        // wait until page is loaded
        androidVariantEditPage.waitUntilPageIsLoaded();
        // the variant details should be the expected ones
        assertEquals(UPDATED_ANDROID_VARIANT_NAME, androidVariantEditPage.getName());
        assertEquals(UPDATED_ANDROID_VARIANT_DESC, androidVariantEditPage.getDescription());
        assertEquals(UPDATED_ANDROID_VARIANT_GOOGLE_KEY, androidVariantEditPage.getGoogleApiKey());
    }

    /* -- Testing data section -- */

    private static final String ADMIN_USERNAME = "admin";

    private static final String DEFAULT_ADMIN_PASSWORD = "123";

    private static final String NEW_ADMIN_PASSWORD = "aerogear";

    private static final String PUSH_APP_NAME = "MyApp";

    private static final String PUSH_APP_DESC = "Awesome app!";

    private static final String UPDATED_PUSH_APP_NAME = "MyNewApp";

    private static final String UPDATED_PUSH_APP_DESC = "My new awesome app!";

    private static final String ANDROID_VARIANT_NAME = "MyAndroidVariant";

    private static final String ANDROID_VARIANT_DESC = "My awesome variant!";

    private static final String ANDROID_VARIANT_GOOGLE_KEY = "IDDASDASDSAQ";

    private static final String UPDATED_ANDROID_VARIANT_NAME = "MyNewAndroidVariant";

    private static final String UPDATED_ANDROID_VARIANT_DESC = "My new awesome variant!";

    private static final String UPDATED_ANDROID_VARIANT_GOOGLE_KEY = "IDDASDASDSAQ__1";

    /* -- Testing data section -- */
}