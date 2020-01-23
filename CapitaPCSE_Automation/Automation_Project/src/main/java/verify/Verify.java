package verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import browsersetup.BaseClass;
import reporting.ListenerClass;

/**
 * Verification tool class. Wrapper class around TestNG Assert.
 */
public class Verify extends ListenerClass
{

	protected Verify() {
		// hide constructor
	}

	static public void verifyTrue(boolean condition, String message) {		  
		try{
			Assert.assertTrue(condition, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}		  
	}

	static public void verifyTrue(boolean condition) {
		try{
			Assert.assertTrue(condition);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyFalse(boolean condition, String message) {
		try{
			Assert.assertFalse(condition, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyFalse(boolean condition) {
		try{
			Assert.assertFalse(condition);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void fail(String message, Throwable realCause) {
		try{
			Assert.fail(message, realCause);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void fail(String message) {
		try{
			Assert.fail(message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void fail() {
		try{
			Assert.fail();
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Object actual, Object expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Object actual, Object expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(String actual, String expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(String actual, String expected, String assertPassMessage, int verificationNumber) {
		try
		{
			BaseClass baseclass = new BaseClass();
			if(actual.equals(expected) && verificationNumber == 1)
			{
				baseclass.setAssertMessage(assertPassMessage, verificationNumber);
			}
			else if(actual.equals(expected) && verificationNumber != 1)	
				baseclass.setAssertMessage(assertPassMessage, verificationNumber);
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(double actual, double expected, double delta, String message) {
		try{
			Assert.assertEquals(actual, expected, delta, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(double actual, double expected, double delta) {
		try{
			Assert.assertEquals(actual, expected, delta);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(float actual, float expected, float delta, String message) {
		try{
			Assert.assertEquals(actual, expected, delta, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(float actual, float expected, float delta) {
		try{
			Assert.assertEquals(actual, expected, delta);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(long actual, long expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(long actual, long expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(boolean actual, boolean expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(boolean actual, boolean expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(byte actual, byte expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(byte actual, byte expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(char actual, char expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(char actual, char expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(short actual, short expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(short actual, short expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(int actual,  int expected, String message) {
		try{
			Assert.assertEquals(actual, expected, message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(int actual, int expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}
	
	static public void verifyEquals(List<ArrayList<String>> actual,  List<ArrayList<String>> expected, String message) {
		try{
			 if (actual.size() != expected.size()) {
		           System.out.println(message);
		           Assert.assertEquals(actual.size(),expected.size());
		        }
			 else
			 {
			 
			 for (int i=0; i<actual.size();i++)
			 {
				/* if(actual.get(i).size()!= expected.get(i).size())
				 {
					 System.out.println("The list size is not matching with actual" +actual.get(i)+ "and expected is: " +expected.get(i));
					 Assert.assertEquals(actual.get(i), expected.get(i));
				 }*/
				 
				 for (int k=0; k<actual.get(i).size(); k++)
				 {
					 if(!actual.get(i).get(k).equals(expected.get(i).get(k))) {
					 System.out.println("The actual list content: " +actual.get(i).get(k)+ "expecated list content: " +expected.get(i).get(k));
					 }
					 //System.out.println("The list content does not match" +actual.get(i).get(k));
					 Assert.assertEquals(actual.get(i).get(k), expected.get(i).get(k));
			 }
			 
				 
			//Assert.assertEquals(actual, expected, message);
		}	
			      
		}
		}
			 catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(List<ArrayList<String>> actual,  List<ArrayList<String>> expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}
	
	static public void verifyEquals(ArrayList<String> actual,  ArrayList<String> expected, String message) {
		try{
					
			Assert.assertEquals(actual, expected, message);
					
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(ArrayList<String> actual,  ArrayList<String> expected) {
		try{
			Assert.assertEquals(actual, expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotNull(Object object) {
		try{
			Assert.assertNotNull(object);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotNull(Object object, String message) {
		try{
			Assert.assertNotNull(object,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNull(Object object) {
		try{
			Assert.assertNull(object);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNull(Object object, String message) {
		try{
			Assert.assertNull(object,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifySame(Object actual, Object expected, String message) {
		try{
			Assert.assertSame(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifySame(Object actual, Object expected) {
		try{
			Assert.assertSame(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotSame(Object actual, Object expected, String message) {
		try{
			Assert.assertNotSame(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotSame(Object actual, Object expected) {
		try{
			Assert.assertNotSame(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Collection<?> actual, Collection<?> expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Collection<?> actual, Collection<?> expected, String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Iterator<?> actual, Iterator<?> expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Iterator<?> actual, Iterator<?> expected, String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Iterable<?> actual, Iterable<?> expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Iterable<?> actual, Iterable<?> expected, String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Object[] actual, Object[] expected, String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEqualsNoOrder(Object[] actual, Object[] expected, String message) {
		try{
			Assert.assertEqualsNoOrder(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Object[] actual, Object[] expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEqualsNoOrder(Object[] actual, Object[] expected) {
		try{
			Assert.assertEqualsNoOrder(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(final byte[] actual, final byte[] expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(final byte[] actual, final byte[] expected, final String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Set<?> actual, Set<?> expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Set<?> actual, Set<?> expected, String message) {
		try{
			Assert.assertEquals(actual,expected,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyEquals(Map<?, ?> actual, Map<?, ?> expected) {
		try{
			Assert.assertEquals(actual,expected);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	public static void verifyNotEquals(Object actual1, Object actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	public static void verifyNotEquals(Object actual1, Object actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(String actual1, String actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(String actual1, String actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(long actual1, long actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(long actual1, long actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(boolean actual1, boolean actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(boolean actual1, boolean actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(byte actual1, byte actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(byte actual1, byte actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(char actual1, char actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(char actual1, char actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(short actual1, short actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(short actual1, short actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(int actual1, int actual2, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static void verifyNotEquals(int actual1, int actual2) {
		try{
			Assert.assertNotEquals(actual1,actual2);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotEquals(float actual1, float actual2, float delta, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,delta,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotEquals(float actual1, float actual2, float delta) {
		try{
			Assert.assertNotEquals(actual1,actual2,delta);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotEquals(double actual1, double actual2, double delta, String message) {
		try{
			Assert.assertNotEquals(actual1,actual2,delta,message);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public void verifyNotEquals(double actual1, double actual2, double delta) {
		try{
			Assert.assertNotEquals(actual1,actual2,delta);
		}catch(AssertionError e){
			addToErrorBuffer(e);
		}
	}

	static public boolean verifyFailure()
	{
		boolean failure = false;
/*		SoftAssert softAssertion= new SoftAssert();
		softAssertion.assertAll();*/
		List<Throwable> lThrowable = TestMethodErrorBuffer.get();
		//if there are verification failures 
		if(lThrowable != null)
		{
			if(lThrowable.size() > 0)
			{
				//set failure flag = True 
				failure = true;
			}
		}
		return failure;
	}

	private static void addToErrorBuffer(AssertionError e){	  

		try
		{				

			VerificationError verificationError = new VerificationError(e.getMessage());

			verificationError.setStackTrace(e.getStackTrace());
			List<Throwable> errorBuffer = new ArrayList<Throwable>();
			errorBuffer.add(verificationError);
			try
			{
				TestMethodErrorBuffer.get().add(verificationError);
			}
			catch(NullPointerException excep)
			{
				TestMethodErrorBuffer.set(errorBuffer);
				//TestMethodErrorBuffer.get().add(verificationError);
			}
		}
		catch(NullPointerException ex)
		{
			throw new RuntimeException("There was a Null Pointer Exception for Verify Assertion while adding Error to the Error Buffer." + ListenerClass.class.getName());
		}

	}
}
