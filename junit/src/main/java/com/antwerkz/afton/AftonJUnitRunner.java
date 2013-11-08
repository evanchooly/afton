package com.antwerkz.afton;

import static java.lang.String.format;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class AftonJUnitRunner extends BlockJUnit4ClassRunner {
  private boolean trace = false;

  private Status issueStatus;

  /**
   * Creates a BlockJUnit4ClassRunner to run {@code klass}
   *
   * @throws InitializationError if the test class is malformed.
   */
  public AftonJUnitRunner(final Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
    super.runChild(method, new AftonRunNotifier(method, notifier));
  }

  private class AftonRunNotifier extends RunNotifier {
    private final FrameworkMethod method;

    private final RunNotifier notifier;

    private String url;

    private final String testName;

    public AftonRunNotifier(final FrameworkMethod method, final RunNotifier notifier) {
      trace("*** AftonJUnitRunner$AftonRunNotifier.AftonRunNotifier");
      trace("method = [" + method.getName() + "], notifier = [" + notifier + "]");
      this.method = method;
      this.notifier = notifier;
      github(method);
      jira(method);
      testName = format("%s#%s", getTestClass().getJavaClass().getSimpleName(), method.getName());
    }

    private void github(final FrameworkMethod method) {
      GitHubIgnore github = method.getAnnotation(GitHubIgnore.class);
      issueStatus = Ignorant.ignore(github);
      if (issueStatus != Status.NOT_TRACKING) {
        url = format("https://github.com/%s/issues/%d", github.repository(), github.issue());
      }
    }

    private void jira(final FrameworkMethod method) {
      JiraIgnore jira = method.getAnnotation(JiraIgnore.class);
      issueStatus = Ignorant.ignore(jira);
      if (jira != null) {
        url = format("%s/browse/%s", jira.server(), jira.issue());
      }
    }

    @Override
    public void fireTestFailure(final Failure failure) {
      trace("AftonJUnitRunner.fireTestFailure");
      trace("failure = [" + failure + "]");
      switch (issueStatus) {
        case CLOSED:
          System.out.printf("%s is still monitoring %s even though it has been closed.%n", testName, url);
          notifier.fireTestFailure(failure);
          break;
        case NONEXISTENT:
          System.out.printf("%s is monitoring %s even though it doesn't exist.%n", testName, url);
          notifier.fireTestFailure(failure);
          break;
        case OPEN:
          System.out.printf("%s has been ignored because %s is still open.%n", testName, url);
          break;
        case NOT_TRACKING:
          break;
      }
    }

    @Override
    public void fireTestRunFinished(final Result result) {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestRunFinished");
      trace("result = [" + result + "]");
      notifier.fireTestRunFinished(result);
    }

    @Override
    public void fireTestFinished(final Description description) {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestFinished");
      trace("description = [" + description + "]");
      switch (issueStatus) {
        case CLOSED:
          System.out.printf("%s is still monitoring %s even though it has been closed.%n", testName, url);
          break;
        case NONEXISTENT:
          System.out.printf("%s is monitoring %s even though it doesn't exist.%n", testName, url);
          break;
      }
      notifier.fireTestFinished(description);
    }

    @Override
    public void addFirstListener(final RunListener listener) {
      trace("AftonJUnitRunner$AftonRunNotifier.addFirstListener");
      trace("listener = [" + listener + "]");
      notifier.addFirstListener(listener);
    }

    @Override
    public void addListener(final RunListener listener) {
      trace("AftonJUnitRunner$AftonRunNotifier.addListener");
      trace("listener = [" + listener + "]");
      notifier.addListener(listener);
    }

    @Override
    public void fireTestAssumptionFailed(final Failure failure) {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestAssumptionFailed");
      trace("failure = [" + failure + "]");
      notifier.fireTestAssumptionFailed(failure);
    }

    @Override
    public void fireTestIgnored(final Description description) {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestIgnored");
      trace("description = [" + description + "]");
      notifier.fireTestIgnored(description);
    }

    @Override
    public void fireTestRunStarted(final Description description) {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestRunStarted");
      trace("description = [" + description + "]");
      notifier.fireTestRunStarted(description);
    }

    @Override
    public void fireTestStarted(final Description description) throws StoppedByUserException {
      trace("AftonJUnitRunner$AftonRunNotifier.fireTestStarted");
      trace("description = [" + description + "]");
      notifier.fireTestStarted(description);
    }

    @Override
    public void pleaseStop() {
      trace("AftonJUnitRunner$AftonRunNotifier.pleaseStop");
      notifier.pleaseStop();
    }

    @Override
    public void removeListener(final RunListener listener) {
      trace("AftonJUnitRunner$AftonRunNotifier.removeListener");
      trace("listener = [" + listener + "]");
      notifier.removeListener(listener);
    }
  }

  private void trace(final String message) {
    if (trace) {
      System.out.println(message);
    }
  }
}