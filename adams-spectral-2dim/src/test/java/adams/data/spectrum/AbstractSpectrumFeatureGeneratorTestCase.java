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
 * AbstractSpectrumFeatureGeneratorTestCase.java
 * Copyright (C) 2014 University of Waikato, Hamilton, New Zealand
 */
package adams.data.spectrum;

import adams.core.CleanUpHandler;
import adams.core.Destroyable;
import adams.core.Utils;
import adams.core.io.FileUtils;
import adams.data.io.input.AbstractSpectrumReader;
import adams.data.spreadsheet.Cell;
import adams.data.spreadsheet.Row;
import adams.test.AbstractTestHelper;
import adams.test.AdamsTestCase;
import adams.test.SpectralTestHelper;
import adams.test.TmpFile;

import java.io.File;
import java.util.List;

/**
 * Ancestor for test cases tailored for spectrum feature generators.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 7706 $
 */
public abstract class AbstractSpectrumFeatureGeneratorTestCase
  extends AdamsTestCase {

  /**
   * Constructs the test case. Called by subclasses.
   *
   * @param name 	the name of the test
   */
  public AbstractSpectrumFeatureGeneratorTestCase(String name) {
    super(name);
  }

  /**
   * Returns the test helper class to use.
   *
   * @return		the helper class instance
   */
  @Override
  protected AbstractTestHelper newTestHelper() {
    return new SpectralTestHelper(this, "adams/data/spectrum/data");
  }

  /**
   * Loads the image to process.
   *
   * @param filename	the filename to load (without path)
   * @return		the image, null in case of an error
   */
  protected Spectrum[] load(String filename, AbstractSpectrumReader reader) {
    List<Spectrum>	result;
    
    result = null;

    m_TestHelper.copyResourceToTmp(filename);
    reader.setInput(new TmpFile(filename));
    result = reader.read();
    m_TestHelper.deleteFileFromTmp(filename);

    return result.toArray(new Spectrum[result.size()]);
  }

  /**
   * Processes the input data and returns the processed data.
   *
   * @param spectrum	the spectrum to work on
   * @param scheme	the scheme to process the data with
   * @return		the generated data
   */
  protected Object[][] process(Spectrum[] spectrum, AbstractSpectrumFeatureGenerator scheme) {
    Object[][]	result;
    int		i;
    
    result = new Object[spectrum.length][];
    for (i = 0; i < spectrum.length; i++)
      result[i] = scheme.generate(spectrum[i]);
    
    return result;
  }
  
  /**
   * Converts the object to a string representation.
   * 
   * @param obj		the object to convert
   * @return		the generated string
   */
  protected String toString(Object obj) {
    StringBuilder	result;
    Row			row;
    int			i;
    Cell		cell;
    
    if (obj instanceof Row) {
      row    = (Row) obj;
      result = new StringBuilder();
      for (i = 0; i < row.getCellCount(); i++) {
	if (i > 0)
	  result.append(",");
	cell = row.getCell(i);
	if (cell.isNumeric())
	  result.append(Utils.doubleToString(cell.toDouble(), 6));
	else
	  result.append(cell.getContent());
      }
      return result.toString();
    }
    else {
      return obj.toString();
    }
  }

  /**
   * Saves the data in the tmp directory.
   *
   * @param data	the data to save
   * @param filename	the filename to save to (without path)
   * @return		true if successfully saved
   */
  protected boolean save(Object[][] data, String filename) {
    boolean	result;
    boolean	first;
    
    result = true;
    
    first = true;
    for (Object[] objects: data) {
      for (Object obj: objects) {
	result = FileUtils.writeToFile(
	    m_TestHelper.getTmpDirectory() + File.separator + filename,
	    toString(obj) + "\n",
	    first);
	if (!result)
	  break;
	first = false;
      }
    }
    
    return result;
  }

  /**
   * Returns the filenames (without path) of the input data files to use
   * in the regression test.
   *
   * @return		the filenames
   */
  protected abstract String[] getRegressionInputFiles();

  /**
   * Returns the spectrum filereaders to use in the regression test for 
   * loading the spectrum files.
   *
   * @return		the readers
   */
  protected abstract AbstractSpectrumReader[] getRegressionInputReaders();

  /**
   * Returns the setups to use in the regression test.
   *
   * @return		the setups
   */
  protected abstract AbstractSpectrumFeatureGenerator[] getRegressionSetups();

  /**
   * Creates an output filename based on the input filename.
   *
   * @param input	the input filename (no path)
   * @param no		the number of the test
   * @return		the generated output filename (no path)
   */
  protected String createOutputFilename(String input, int no) {
    String	result;
    int		index;
    String	ext;

    ext = "-out" + no;

    index = input.lastIndexOf('.');
    if (index == -1) {
      result = input + ext;
    }
    else {
      result  = input.substring(0, index);
      result += ext;
    }
    result += ".txt";

    return result;
  }

  /**
   * Compares the processed data against previously saved output data.
   */
  public void testRegression() {
    Spectrum[]			data;
    Object[][]				processed;
    boolean				ok;
    String				regression;
    int					i;
    String[]				input;
    AbstractSpectrumReader[]		readers;
    AbstractSpectrumFeatureGenerator[]	setups;
    String[]				output;
    TmpFile[]				outputFiles;

    if (m_NoRegressionTest)
      return;

    input   = getRegressionInputFiles();
    readers = getRegressionInputReaders();
    output  = new String[input.length];
    setups  = getRegressionSetups();
    assertEquals("Number of files and readers differ!", input.length, readers.length);
    assertEquals("Number of files and setups differ!", input.length, setups.length);

    // process data
    for (i = 0; i < input.length; i++) {
      data = load(input[i], readers[i]);
      assertNotNull("Could not load data for regression test from " + input[i], data);

      processed = process(data, setups[i]);
      assertNotNull("Failed to process data?", processed);

      output[i] = createOutputFilename(input[i], i);
      ok        = save(processed, output[i]);
      assertTrue("Failed to save regression data?", ok);
    }

    // test regression
    outputFiles = new TmpFile[output.length];
    for (i = 0; i < output.length; i++)
      outputFiles[i] = new TmpFile(output[i]);
    regression = m_Regression.compare(outputFiles);
    assertNull("Output differs:\n" + regression, regression);

    // remove output, clean up scheme
    for (i = 0; i < output.length; i++) {
      if (setups[i] instanceof Destroyable)
	((Destroyable) setups[i]).destroy();
      else if (setups[i] instanceof CleanUpHandler)
	((CleanUpHandler) setups[i]).cleanUp();
      m_TestHelper.deleteFileFromTmp(output[i]);
    }
  }
}
