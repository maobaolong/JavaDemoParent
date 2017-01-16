package net.mbl.demo;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple FixedThreadPoolDemo.
 */
public class FixedThreadPoolDemoTest
    extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public FixedThreadPoolDemoTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(FixedThreadPoolDemoTest.class);
  }

  /**
   * Rigourous Test :-)
   */
  public void testApp() {
    assertTrue(true);
  }
}
