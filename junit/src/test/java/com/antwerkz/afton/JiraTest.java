package com.antwerkz.afton;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AftonJUnitRunner.class)
public class JiraTest {

  @Test
  @JiraIgnore(server = "https://jira.mongodb.org", issue = "SERVER-10416")
  public void testIgnore() {
    fail("Forced failure");
  }

  @Test
  @JiraIgnore(server = "https://jira.mongodb.org", issue = "JAVA-538")
  public void testClosedIssue() {
    Assert.assertTrue(true);
  }

  @Test
  @JiraIgnore(server = "https://jira.mongodb.org", issue = "FLIBBERTY-538")
  public void nonExistentIssue() {
    Assert.assertTrue(true);
  }
}
