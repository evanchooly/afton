afton
=====

TestNG and JUnit extensions to suppress test failures so long as the documented jira or github issues are still open.

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

_Pending_