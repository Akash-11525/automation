package verify;

import java.util.List;

/* package access only */
public class TestMethodErrorBuffer 
{

	// thread safe while running tests in parallel
	private static ThreadLocal<List<Throwable>> testErrorBuffer = new ThreadLocal<List<Throwable>>();

	public static List<Throwable> get()
	{
		return testErrorBuffer.get();
	}

	public static void set(List<Throwable> errorBuffer)
	{
		testErrorBuffer.set(errorBuffer);
	}

	public static void remove()
	{
		testErrorBuffer.remove();
	}

}
