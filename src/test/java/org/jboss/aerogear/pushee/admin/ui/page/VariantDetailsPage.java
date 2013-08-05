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

import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class VariantDetailsPage extends PushServerAdminUiPage {

    @FindBy(jquery = "div.content section h2")
    private WebElement HEADER_TITLE;

    @FindBy(jquery = "div.content section span.rcue-code:eq(0)")
    private WebElement VARIANT_ID;

    @FindBy(jquery = "div.content section span.rcue-code:eq(1)")
    private WebElement SECRET;

    @FindBy(id = "mobile-application-variant-table")
    private WebElement MOBILE_INSTALLATIONS_TABLE;
    
    @FindBy(jquery = "div.content div a[href=\"#/mobileApps\"]")
    private WebElement BREADCRUMB_PUSH_APPS_LINK;
    
    public void navigateToPushAppsPage() {
        guardXhr(BREADCRUMB_PUSH_APPS_LINK).click();
    }

    public String getHeaderTitle() {
        return HEADER_TITLE.getText();
    }

    public String getVariantId() {
        return VARIANT_ID.getText();
    }

    public String getSecret() {
        return SECRET.getText();
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(MOBILE_INSTALLATIONS_TABLE).is().visible();
    }
}
