afton
=====

TestNG and JUnit extensions to suppress test failures so long as the documented jira or github issues are still open.

### Why?
When testing against multiple versions of a release, some tests might fail because of upstream bugs in one version but not 
another.  In those case, you still want to be able to write tests that fully exercise your featureset without having
to worry overly much about those upstream bugs.  This project allows you to write your tests so that you can test against
versions you know should work while waiting on the upstream project to fix its bugs.  Once those issues are resolved and
closed, any subsequent failures in your tests will begin to be reported.

For example, a regression in mongodb 2.5.4 causes a Morphia test to fail against that server but works fine against, say,
2.4.7.  Using afton, we can test as if everything is fine while we wait for that regression to be resolved knowing that
we're at least always testing against 2.4.7 in the meantime.

JUnit
-----

To ignore tests based on open github issues, mark up your tests like this:

```java
@RunWith(AftonJUnitRunner.class)
public class GitHubTest {
  @Test
  @GitHubIgnore(repository = "evanchooly/afton", issue = 1)
  public void ignoreMe() {
    fail("Forced failure");
  }
}
```

TestNG
------

_Pending_


Jira
------

To ignore tests based on open jira issues, mark up your tests like this:

```java
@RunWith(AftonJUnitRunner.class)
public class JiraTest {
  @Test
  @JiraIgnore(server = "https://jira.example.com", issue = "DUDE-1")
  public void ignoreMe() {
    fail("Forced failure");
  }
}
```
