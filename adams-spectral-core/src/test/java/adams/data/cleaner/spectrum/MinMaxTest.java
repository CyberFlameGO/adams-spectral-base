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
 * MinMaxTest.java
 * Copyright (C) 2010 University of Waikato, Hamilton, New Zealand
 */
package adams.data.cleaner.spectrum;

import adams.data.report.DataType;
import adams.data.report.Field;
import adams.env.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for the MinMax filter. Run from the command line with: <br><br>
 * java adams.data.cleaner.spectrum.MinMaxTest
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2242 $
 */
public class MinMaxTest
  extends AbstractCleanerTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public MinMaxTest(String name) {
    super(name);
  }

  /**
   * Returns the filenames (without path) of the input data files to use
   * in the regression test.
   *
   * @return		the filenames
   */
  protected String[][] getRegressionInputFiles() {
    return new String[][]{
	{
	  "871553-nir.spec",
	  "871562-nir.spec",
	  "871563-nir.spec",
	  "871598-nir.spec",
	  "871609-nir.spec"
	},
	{
	  "871553-nir.spec",
	  "871562-nir.spec",
	  "871563-nir.spec",
	  "871598-nir.spec",
	  "871609-nir.spec"
	},
	{
	  "871553-nir.spec",
	  "871562-nir.spec",
	  "871563-nir.spec",
	  "871598-nir.spec",
	  "871609-nir.spec"
	},
	{
	  "871553-nir.spec",
	  "871562-nir.spec",
	  "871563-nir.spec",
	  "871598-nir.spec",
	  "871609-nir.spec"
	}
    };
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  protected AbstractCleaner[] getRegressionSetups() {
    MinMax[]	result;
    Field	field;

    result = new MinMax[4];
    field  = new Field("Clay", DataType.NUMERIC);

    result[0] = new MinMax();
    result[0].setField(field);

    result[1] = new MinMax();
    result[1].setMinimum(100.0);
    result[1].setField(field);

    result[2] = new MinMax();
    result[2].setMaximum(200.0);
    result[2].setField(field);

    result[3] = new MinMax();
    result[3].setMinimum(100.0);
    result[3].setMaximum(200.0);
    result[3].setField(field);

    return result;
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(MinMaxTest.class);
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
