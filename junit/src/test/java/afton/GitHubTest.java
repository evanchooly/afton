package afton;

import com.antwerkz.afton.AftonJUnitRunner;
import com.antwerkz.afton.GitHubIgnore;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;

@RunWith(AftonJUnitRunner.class)
public class GitHubTest {
  @org.junit.Test
  @GitHubIgnore(repository = "evanchooly/afton", issue = 1)
  public void ignoreMe() {
    fail("Forced failure");
  }
}
