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

    private GitHubIgnore github;

    public AftonRunNotifier(final FrameworkMethod method, final RunNotifier notifier) {
      this.method = method;
      this.notifier = notifier;
      github = method.getAnnotation(GitHubIgnore.class);
      if (github != null) {
        url = format("https://github.com/%s/issue/%d", github.repository(), github.issue());
      }
      testName = format("%s#%s", getTestClass().getJavaClass().getSimpleName(), method.getName());
    }

    @Override
    public void fireTestFailure(final Failure failure) {
      System.out.println("AftonJUnitRunner.fireTestFailure");
      if (github != null) {
        report(failure);
      } else {
        notifier.fireTestFailure(failure);
      }
    }

    private void report(final Failure failure) {
      if (Ignorant.ignore(github) == Status.CLOSED) {
        System.out.printf("%s is still monitoring %s even though it has been closed.%n", testName, url);
        notifier.fireTestFailure(failure);
      } else if (Ignorant.ignore(github) == Status.NONEXISTANT) {
        System.out.printf("%s is monitoring %s even though it doesn't exist.%n", testName, url);
        notifier.fireTestFailure(failure);
      } else {
        System.out.printf("%s has been ignored because %s is still open.%n", testName, url);
        notifier.fireTestIgnored(describeChild(method));
      }
    }

    @Override
    public void fireTestRunFinished(final Result result) {
      System.out.println("result = [" + result + "]");
      notifier.fireTestRunFinished(result);
    }

    @Override
    public void fireTestFinished(final Description description) {
      System.out.println("AftonJUnitRunner$AftonRunNotifier.fireTestFinished");
      System.out.println("description = [" + description + "]");
      notifier.fireTestFinished(description);
    }

    @Override
    public void addFirstListener(final RunListener listener) {
      notifier.addFirstListener(listener);
    }

    @Override
    public void addListener(final RunListener listener) {
      notifier.addListener(listener);
    }

    @Override
    public void fireTestAssumptionFailed(final Failure failure) {
      notifier.fireTestAssumptionFailed(failure);
    }

    @Override
    public void fireTestIgnored(final Description description) {
      notifier.fireTestIgnored(description);
    }

    @Override
    public void fireTestRunStarted(final Description description) {
      notifier.fireTestRunStarted(description);
    }

    @Override
    public void fireTestStarted(final Description description) throws StoppedByUserException {
      notifier.fireTestStarted(description);
    }

    @Override
    public void pleaseStop() {
      notifier.pleaseStop();
    }

    @Override
    public void removeListener(final RunListener listener) {
      notifier.removeListener(listener);
    }

  }
}
