package com.antwerkz.afton;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AftonJUnitRunner.class)
public class GitHubTest {

  @Test
  @GitHubIgnore(repository = "evanchooly/afton", issue = 1)
  public void testIgnore() {
    fail("Forced failure");
  }

  @Test
  @GitHubIgnore(repository = "evanchooly/afton", issue = 2)
  public void testClosedIssue() {
    Assert.assertTrue(true);
  }

  @Test
  @GitHubIgnore(repository = "evanchooly/afton", issue = -1)
  public void nonExistentIssue() {
    Assert.assertTrue(true);
  }
}
