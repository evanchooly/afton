package com.antwerkz.afton;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class AftonJUnitRunner extends org.junit.runners.BlockJUnit4ClassRunner {
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
    super.runChild(method, new RunNotifier() {
      @Override
      public void fireTestFailure(final Failure failure) {
        GitHubIgnore github = method.getAnnotation(GitHubIgnore.class);
        if (github != null) {
          String message = Ignorant.ignore(github);
          if (message == null) {
            notifier.fireTestFailure(failure);
          } else {
            System.out.printf(message, getTestClass().getJavaClass().getSimpleName(), method.getName());
            notifier.fireTestIgnored(describeChild(method));
          }
        }
      }
    });
  }
}
