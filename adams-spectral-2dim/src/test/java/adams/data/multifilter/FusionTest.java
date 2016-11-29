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
 * FusionTest.java
 * Copyright (C) 2015 University of Waikato, Hamilton, New Zealand
 */
package adams.data.multifilter;

import adams.core.base.BaseFloat;
import adams.core.base.BaseString;
import adams.env.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for the Fusion multi-spectrum filter. Run from the command line with: <br><br>
 * java adams.data.multifilter.FusionTest
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1286 $
 */
public class FusionTest
  extends AbstractMultiSpectrumFilterTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public FusionTest(String name) {
    super(name);
  }

  /**
   * Returns the configured filter.
   *
   * @return		the filter
   */
  public AbstractMultiSpectrumFilter getFilter() {
    return new Fusion();
  }

  /**
   * Returns the filenames (without path) of the input data files to use
   * in the regression test.
   *
   * @return		the filenames
   */
  @Override
  protected String[] getRegressionInputFiles() {
    return new String[]{
	"simple2.spec",
	"simple2.spec",
	"simple2.spec",
    };
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected AbstractMultiSpectrumFilter[] getRegressionSetups() {
    Fusion[]	result;

    result = new Fusion[3];

    result[0] = new Fusion();
    result[0].setFormats(new BaseString[]{
      new BaseString("NIR"),
    });

    result[1] = new Fusion();
    result[1].setFormats(new BaseString[]{
      new BaseString("NIR"),
      new BaseString("MIR"),
      new BaseString("XRF"),
    });

    result[2] = new Fusion();
    result[2].setFormats(new BaseString[]{
      new BaseString("NIR"),
      new BaseString("MIR"),
      new BaseString("XRF"),
    });
    result[2].setWaveNumberStepSizes(new BaseFloat[]{
      new BaseFloat("1.0"),
      new BaseFloat("2.0"),
      new BaseFloat("10.0"),
    });

    return result;
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(FusionTest.class);
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
