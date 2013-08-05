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
package org.jboss.aerogear.pushee.admin.ui.page;

import static org.jboss.aerogear.pushee.admin.ui.utils.WebElementUtils.clearNfill;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.io.File;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class VariantRegistrationPage extends PushServerAdminUiPage {

    @FindBy(jquery = "div.rcue-dialog-inner form p:eq(0) input[name=\"name\"]")
    private WebElement VARIANT_NAME;

    @FindBy(jquery = "div.rcue-dialog-inner form p:eq(1) textarea[name=\"description\"]")
    private WebElement VARIANT_DESC;

    @FindBy(jquery = "div.rcue-dialog-inner form section:eq(0) input[type=\"radio\"][name=\"platform\"][value=\"android\"]]")
    private WebElement RADIO_BUTTON_ANDROID;

    @FindBy(jquery = "div.rcue-dialog-inner form section:eq(0) input[type=\"text\"]")
    private WebElement GOOGLE_API_KEY_INPUT_FIELD;

    @FindBy(jquery = "div.rcue-dialog-inner form section:eq(1) input[type=\"radio\"][name=\"platform\"][value=\"iOS\"]]")
    private WebElement RADIO_BUTTON_APPLE;

    @FindBy(jquery = "div.rcue-dialog-inner form input[type=\"submit\"]")
    private WebElement SUBMIT_BUTTON;

    @FindBy(jquery = "div.rcue-dialog-inner form section:eq(1) input[type=\"file\"]")
    private WebElement APPLE_CERTIFICATE_INPUT_FILE;

    @FindBy(jquery = "div.rcue-dialog-inner form section:eq(1) input[type=\"password\"]")
    private WebElement APPLE_PASSPHRASE_INPUT_FIELD;

    public void registerAndroidVariant(String name, String desc, String googleApiKey) {
        fillVariantDetails(name, desc);
        selectPlatform(PLATFORM.ANDROID);
        clearNfill(GOOGLE_API_KEY_INPUT_FIELD, googleApiKey);
        submitForm();
    }

    public void registeriOSVariant(String name, String desc, String appleCertPath, String passphrase) {
        File cert = new File(appleCertPath);
        System.out.println("###" + cert.getAbsolutePath());
        APPLE_CERTIFICATE_INPUT_FILE.sendKeys(cert.getAbsolutePath());
        selectPlatform(PLATFORM.IOS);
        fillVariantDetails(name, desc);
        clearNfill(APPLE_PASSPHRASE_INPUT_FIELD, passphrase);
        submitForm();
    }

    private void submitForm() {
        guardXhr(SUBMIT_BUTTON).click();
    }

    private static enum PLATFORM {
        ANDROID, IOS
    }

    private void selectPlatform(PLATFORM platform) {
        switch (platform) {
            case ANDROID:
                RADIO_BUTTON_ANDROID.click();
                break;
            case IOS:
                RADIO_BUTTON_APPLE.click();
                break;
            default:
                break;
        }
    }

    private void fillVariantDetails(String name, String desc) {
        clearNfill(VARIANT_NAME, name);
        clearNfill(VARIANT_DESC, desc);
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(SUBMIT_BUTTON).is().visible();
    }
}
