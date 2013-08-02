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

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class PushAppsPage extends PushServerAdminUiPage {

    @FindBy(jquery = "table.rcue-table")
    private WebElement PUSH_APPLICATION_TABLE;

    @FindBy(jquery = "table.rcue-table thead div.topcoat-button a")
    private WebElement CREATE_BUTTON;

    @FindBy(jquery = "table.rcue-table tbody tr")
    private List<WebElement> PUSH_APPLICATION_LIST;

    private final static String PAGE_URL = "#/mobileApps";

    public String getPageURL() {
        return PAGE_URL;
    }

    public int countPushApps() {
        return filterPushApplicationRows().size();
    }
    
    public void pressCreateButton() {
        CREATE_BUTTON.click();
    }

    private List<WebElement> filterPushApplicationRows() {
        final List<WebElement> rowList = new ArrayList<WebElement>();
        for (WebElement row : PUSH_APPLICATION_LIST) {
            if (row.findElements(ByJQuery.jquerySelector("td")).size() == 5) {
                rowList.add(row);
            }
        }
        return rowList;
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(PUSH_APPLICATION_TABLE).is().visible();
    }
}
