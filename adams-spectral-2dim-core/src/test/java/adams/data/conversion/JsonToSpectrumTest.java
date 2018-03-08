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

/*
 * JsonToSpectrumTest.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.Utils;
import adams.data.spectrum.Spectrum;
import adams.env.Environment;
import adams.test.TmpFile;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.nio.file.Files;
import java.util.List;

/**
 * Tests the JsonToSpectrum conversion.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JsonToSpectrumTest
  extends AbstractSpectralConversionTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name the name of the test
   */
  public JsonToSpectrumTest(String name) {
    super(name);
  }

  /**
   * Turns the data object into a useful string representation.
   *
   * @param data	the object to convert
   * @return		the string representation
   */
  @Override
  protected String toString(Object data) {
    return ((Spectrum) data).toSpreadSheet().toString();
  }

  /**
   * Returns the input data to use in the regression test.
   *
   * @return		the objects
   */
  @Override
  protected Object[] getRegressionInput() {
    Object[]		result;
    List<String>	lines;

    try {
      m_TestHelper.copyResourceToTmp("spectrum.json");
      lines = Files.readAllLines(new TmpFile("spectrum.json").toPath());
      result = new Object[]{Utils.flatten(lines, "\n")};
    }
    catch (Exception e) {
      result = new Object[0];
    }

    return result;
  }

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected Conversion[] getRegressionSetups() {
    return new JsonToSpectrum[]{
      new JsonToSpectrum(),
    };
  }

  /**
   * Returns the ignored line indices to use in the regression test.
   *
   * @return		the setups
   */
  @Override
  protected int[] getRegressionIgnoredLineIndices() {
    return new int[0];
  }

  /**
   * Returns the test suite.
   *
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(JsonToSpectrumTest.class);
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
