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

import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aerogear.pushee.admin.ui.model.AbstractVariant;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebElement;

public class VariantsPage extends PushServerAdminUiPage {

    @FindBy(jquery = "table.rcue-table thead div.topcoat-button a")
    private WebElement ADD_VARIANT_BUTTON;

    @FindBy(jquery = "div.content section h2")
    private WebElement HEADER_TITLE;

    @FindBy(jquery = "div.content section span.rcue-code:eq(0)")
    private WebElement APPLICATION_ID;

    @FindBy(jquery = "div.content section span.rcue-code:eq(1)")
    private WebElement MASTER_SECRET;

    @FindBy(jquery = "table.rcue-table tbody tr")
    private List<WebElement> VARIANTS_LIST;

    @FindBy(jquery = "div.content a[href=\"#/mobileApps\"]")
    private WebElement BACK_TO_PUSH_APPS_LINK;

    public void navigateToPushAppsPage() {
        guardXhr(BACK_TO_PUSH_APPS_LINK).click();
    }

    public void addVariant() {
        guardNoRequest(ADD_VARIANT_BUTTON).click();
    }

    public String getHeaderTitle() {
        return HEADER_TITLE.getText();
    }

    public String getApplicationId() {
        return APPLICATION_ID.getText();
    }

    public String getMasterSecret() {
        return MASTER_SECRET.getText();
    }

    public int countVariants() {
        return filterVariantRows().size();
    }

    public static enum VARIANT_LINK {
        EDIT, REMOVE, DETAILS_PAGE
    }

    public void pressVariantLink(int rowNum, VARIANT_LINK link) {
        final List<WebElement> anchors = VARIANTS_LIST.get(rowNum).findElements(ByTagName.tagName("a"));
        switch (link) {
            case DETAILS_PAGE:
                guardXhr(anchors.get(0)).click();
                break;
            case EDIT:
                guardNoRequest(anchors.get(1)).click();
                break;
            case REMOVE:
                guardNoRequest(anchors.get(2)).click();
                break;
            default:
                break;
        }
    }

    public List<AbstractVariant> getVariantList() {
        final List<AbstractVariant> variantList = new ArrayList<AbstractVariant>();
        for (WebElement row : VARIANTS_LIST) {
            final List<WebElement> tableDataList = row.findElements(ByTagName.tagName("td"));
            if (tableDataList.size() == 6) {
                final String name = tableDataList.get(0).getText();
                final String desc = tableDataList.get(1).getText();
                final String type = tableDataList.get(2).getText();
                final int installations = Integer.valueOf(tableDataList.get(3).getText());
                // System.out.println("name: " + name + " desc: " + desc + " vars " + vars);
                variantList.add(new AbstractVariant(name, desc, type, installations));
            }
        }
        return variantList;
    }

    public int findVariantRow(String name) {
        final List<AbstractVariant> variantList = getVariantList();
        if (name != null && variantList != null && !variantList.isEmpty()) {
            for (int i = 0; i < variantList.size(); i++) {
                if (name.equals(variantList.get(i).getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<WebElement> filterVariantRows() {
        final List<WebElement> rowList = new ArrayList<WebElement>();
        for (WebElement row : VARIANTS_LIST) {
            if (row.findElements(ByJQuery.jquerySelector("td")).size() == 6) {
                rowList.add(row);
            }
        }
        return rowList;
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(ADD_VARIANT_BUTTON).is().visible();
    }
}
