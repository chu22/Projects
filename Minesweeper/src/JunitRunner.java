import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JunitRunner {
	
	 public static void main(String[] args) {
		  Class[] c = {CellTest.class, HighScoresTest.class};
		  Result result = JUnitCore.runClasses(c);
		  for (Failure fail : result.getFailures()) {
			  System.out.println(fail.toString());
		  }
		  if (result.wasSuccessful()) {
			  System.out.println("All tests finished successfully...");
		  }
	 }
}