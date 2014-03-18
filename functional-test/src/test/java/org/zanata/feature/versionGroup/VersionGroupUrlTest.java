/*
 * Copyright 2014, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.zanata.feature.versionGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.zanata.feature.DetailedTest;
import org.zanata.page.groups.VersionGroupPage;
import org.zanata.page.utility.DashboardPage;
import org.zanata.util.SampleProjectRule;
import org.zanata.util.ZanataRestCaller;
import org.zanata.workflow.LoginWorkFlow;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

/**
 * Tests relating to custom urls handled in the page.
 *
 * The urls mapped for a version group are of the form:
 * <zanata>/version-group/view/<slug>/languages/<locale_code>
 * <zanata>/version-group/view/<slug>/projects/<project_slug>[<version_slug>]
 * <zanata>/version-group/view/<slug>/maintainers
 * <zanata>/version-group/view/<slug>/settings
 * <zanata>/version-group/view/<slug>/settings/languages etc...
 *
 * @author Carlos Munoz <a
 *         href="mailto:camunoz@redhat.com">camunoz@redhat.com</a>
 */
@Category(DetailedTest.class)
public class VersionGroupUrlTest {

    @Rule
    public SampleProjectRule sampleProjectRule = new SampleProjectRule();

    private DashboardPage dashboardPage;

    private ZanataRestCaller restCaller;

    @Before
    public void setUp() {
        dashboardPage = new LoginWorkFlow().signIn("admin", "admin");
    }

    @Test
    public void testUrlChangeUpdatesActiveElements() {
        VersionGroupPage versionGroupPage = createVersionGroup();
        testBasicGroupUrl(versionGroupPage);
    }

    private void testBasicGroupUrl(VersionGroupPage versionGroupPage) {
        assertThat(versionGroupPage.isLanguagesTabActive(), is(true));
    }

    @Test
    public void testTabClicksChangeUrl() {
        VersionGroupPage versionGroupPage = createVersionGroup();

        testLanguageTabClick(versionGroupPage);
        testProjectTabClick(versionGroupPage);
        testMaintainersTabClick(versionGroupPage);
        testSettingsTabClick(versionGroupPage);
    }

    private void testLanguageTabClick(VersionGroupPage versionGroupPage) {
        versionGroupPage.clickLanguagesTab();
        assertThat(versionGroupPage.getUrl(),
                endsWith("/version-group/view/test-group/languages"));
    }

    private void testProjectTabClick(VersionGroupPage versionGroupPage) {
        versionGroupPage.clickProjectsTab();
        assertThat(versionGroupPage.getUrl(),
                endsWith("/version-group/view/test-group/projects"));
    }

    private void testMaintainersTabClick(VersionGroupPage versionGroupPage) {
        versionGroupPage.clickMaintainersTab();
        assertThat(versionGroupPage.getUrl(),
                endsWith("/version-group/view/test-group/maintainers"));
    }

    private void testSettingsTabClick(VersionGroupPage versionGroupPage) {
        versionGroupPage.clickSettingsTab();
        assertThat(versionGroupPage.getUrl(),
                endsWith("/version-group/view/test-group/settings"));
    }

    private VersionGroupPage createVersionGroup() {
        String groupID = "test-group";
        String groupName = "A Test group";
        return dashboardPage
                .goToGroups()
                .createNewGroup()
                .inputGroupId(groupID)
                .inputGroupName(groupName)
                .saveGroup()
                .goToGroup(groupName);
    }

    private void createProject() {
        restCaller = new ZanataRestCaller();
        // create another project and version
        String projectSlug = "base";
        String iterationSlug = "master";
        String projectType = "gettext";
        restCaller.createProjectAndVersion(projectSlug, iterationSlug,
                projectType);
    }
}
