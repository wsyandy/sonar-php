/*
 * PHP :: Integration Tests
 * Copyright (C) 2011 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.sonar.it.php;

import org.junit.ClassRule;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueClient;
import org.sonar.wsclient.issue.IssueQuery;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class CustomRulesTest {

  @ClassRule
  public static Orchestrator orchestrator = Tests.ORCHESTRATOR;

  private static IssueClient issueClient;

  @BeforeClass
  public static void prepare() throws InterruptedException {
    orchestrator.resetData();
    SonarRunner build = SonarRunner.create()
      .setProjectDir(Tests.projectDirectoryFor("custom_rules"))
      .setProjectKey("custom-rules")
      .setProjectName("Custom Rules")
      .setProjectVersion("1.0")
      .setSourceDirs("src");
    orchestrator.getServer().provisionProject("custom-rules", "Custom Rules");
    orchestrator.getServer().associateProjectToQualityProfile("custom-rules", "php", "php-custom-rules-profile");
    orchestrator.executeBuild(build);

    issueClient = orchestrator.getServer().wsClient().issueClient();
  }

  @Test
  public void base_tree_visitor_check() {
    List<Issue> issues = issueClient.find(IssueQuery.create().rules("php-custom-rules:visitor")).list();

    assertThat(issues).hasSize(1);

    Issue issue = issues.get(0);
    assertThat(issue.line()).isEqualTo(5);
    assertThat(issue.message()).isEqualTo("Function expression.");
    assertThat(issue.debt()).isEqualTo("5min");
  }

  @Test
  public void subscription_base_visitor_check() {
    List<Issue> issues = issueClient.find(IssueQuery.create().rules("php-custom-rules:subscription")).list();

    assertThat(issues).hasSize(1);

    Issue issue = issues.get(0);
    assertThat(issue.line()).isEqualTo(8);
    assertThat(issue.message()).isEqualTo("For statement.");
    assertThat(issue.debt()).isEqualTo("10min");
  }

}
