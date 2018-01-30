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
 * JsonToSpectrum.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package adams.data.conversion;

import adams.core.io.FileUtils;
import adams.data.spectrum.Spectrum;
import adams.data.spectrum.SpectrumJsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 <!-- globalinfo-start -->
 * Turns a JSON string into a spectrum.<br>
 * Input format:<br>
 * {<br>
 *   "id": "someid",<br>
 *   "format": "NIR",<br>
 *   "data": [<br>
 *     {"wave": 1.0, "ampl": 1.1},<br>
 *     {"wave": 2.0, "ampl": 2.1}<br>
 *   ],<br>
 *   "report": {<br>
 *     "Sample ID": "someid",<br>
 *     "GLV2": 1.123,<br>
 *     "valid": true<br>
 *   }<br>
 * }<br>
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JsonToSpectrum
  extends AbstractConversion {

  private static final long serialVersionUID = 2957342595369694174L;

  /**
   * Returns a string describing the object.
   *
   * @return a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Turns a JSON string into a spectrum.\n"
      + "Input format:\n"
      + SpectrumJsonUtils.example();
  }

  /**
   * Returns the class that is accepted as input.
   *
   * @return the class
   */
  @Override
  public Class accepts() {
    return String.class;
  }

  /**
   * Returns the class that is generated as output.
   *
   * @return the class
   */
  @Override
  public Class generates() {
    return Spectrum.class;
  }

  /**
   * Performs the actual conversion.
   *
   * @throws Exception if something goes wrong with the conversion
   * @return the converted data
   */
  @Override
  protected Object doConvert() throws Exception {
    Spectrum 		result;
    String 		input;
    BufferedReader 	breader;
    JsonParser 		jp;
    JsonElement 	je;

    input   = (String) m_Input;
    breader = null;
    try {
      breader = new BufferedReader(new StringReader(input));
      jp = new JsonParser();
      je = jp.parse(breader);
      result = SpectrumJsonUtils.fromJson(je.getAsJsonObject());
    }
    catch (Exception e) {
      result = null;
    }
    finally {
      FileUtils.closeQuietly(breader);
    }

    return result;
  }
}
