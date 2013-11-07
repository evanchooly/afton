package com.antwerkz.afton;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ignorant {
  private static final Logger LOG = LoggerFactory.getLogger(Ignorant.class);

  public static Status ignore(GitHubIgnore annotation) {
    try {
      if(annotation == null) {
        return Status.NOT_TRACKING;
      }
      GitHub gitHub = GitHub.connect();
      GHRepository repository = gitHub.getRepository(annotation.repository());
      GHIssue issue = repository.getIssue(annotation.issue());
      if(issue != null && issue.getState() == GHIssueState.OPEN) {
        return Status.OPEN;
      } else {
        return Status.CLOSED;
      }
    } catch (FileNotFoundException e) {
      return Status.NONEXISTENT;
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
