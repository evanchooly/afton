package com.antwerkz.afton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ignorant {
  private static final Logger LOG = LoggerFactory.getLogger(Ignorant.class);

  private static String userName;

  private static String password;

  public static Status ignore(GitHubIgnore annotation) {
    try {
      if (annotation == null) {
        return Status.NOT_TRACKING;
      }
      GitHub gitHub = GitHub.connect();
      GHRepository repository = gitHub.getRepository(annotation.repository());
      GHIssue issue = repository.getIssue(annotation.issue());
      if (issue != null && issue.getState() == GHIssueState.OPEN) {
        return Status.OPEN;
      } else {
        return Status.CLOSED;
      }
    } catch (FileNotFoundException e) {
      return Status.NONEXISTENT;
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public static Status ignore(final JiraIgnore annotation) {
    if (annotation == null) {
      return Status.NOT_TRACKING;
    }
    try {
      JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
      JiraRestClient restClient = factory
          .createWithBasicHttpAuthentication(new URI(annotation.server()), getUserName(), getPassword());
      try {
        Issue issue = restClient.getIssueClient().getIssue(annotation.issue()).get();
        if (!issue.getStatus().getName().equalsIgnoreCase("CLOSED")) {
          return Status.OPEN;
        } else {
          return Status.CLOSED;
        }
      } catch (ExecutionException e) {
        return Status.NONEXISTENT;
      }
    } catch (URISyntaxException | InterruptedException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static String getPassword() {
    if (password == null) {
      password = System.getProperty("jira.password");
    }
    return password;
  }

  private static String getUserName() {
    if (userName == null) {
      userName = System.getProperty("jira.userName");
    }
    return userName;
  }
}
