/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * SampleDataTest.java
 * Copyright (C) 2010 University of Waikato, Hamilton, New Zealand
 */
package adams.data.sampledata;

import adams.env.Environment;
import adams.test.AdamsTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for the SampleData class. Run from the command line with: <br><br>
 * java adams.data.sampledata.SampleDataTest
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2242 $
 */
public class SampleDataTest
  extends AdamsTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public SampleDataTest(String name) {
    super(name);
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(SampleDataTest.class);
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(Environment.class);
    runTest(suite());
  }
}
