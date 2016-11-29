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
 * LOWESSBased.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */
package adams.data.spectrumbaseline;

import adams.data.baseline.AbstractLOWESSBased;
import adams.data.container.DataPoint;
import adams.data.filter.AbstractLOWESS;
import adams.data.spectrumfilter.LOWESS;
import adams.data.spectrum.Spectrum;
import adams.data.spectrum.SpectrumPoint;

/**
 <!-- globalinfo-start -->
 * A baseline correction scheme that uses LOWESS smoothing to determine the baseline.<br>
 * <br>
 * For more information see:<br>
 * <br>
 * WikiPedia. Local Regression. URL http:&#47;&#47;en.wikipedia.org&#47;wiki&#47;Lowess.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-window-size &lt;int&gt; (property: windowSize)
 * &nbsp;&nbsp;&nbsp;The window size to use, must be at least 1.
 * &nbsp;&nbsp;&nbsp;default: 20
 * &nbsp;&nbsp;&nbsp;minimum: 1
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2242 $
 */
public class LOWESSBased
  extends AbstractLOWESSBased<Spectrum> {

  /** for serialization. */
  private static final long serialVersionUID = -2273739395862247537L;

  /**
   * Returns a new instance of a LOWESS filter.
   * 
   * @return		the filter
   */
  @Override
  protected AbstractLOWESS getFilter() {
    return new LOWESS();
  }

  /**
   * Subtracts the baseline from the old data point and creates a new
   * data point.
   * 
   * @param old		the old data point to subtract the baseline from
   * @param baseline	the baseline value to subtract
   * @return		the new corrected data point
   */
  @Override
  protected DataPoint subtract(DataPoint old, DataPoint baseline) {
    SpectrumPoint	result;
    SpectrumPoint	oldP;
    SpectrumPoint	baseP;

    oldP  = (SpectrumPoint) old;
    baseP = (SpectrumPoint) baseline;
    result = new SpectrumPoint(
	oldP.getWaveNumber(),
	oldP.getAmplitude() - baseP.getAmplitude());
    
    return result;
  }

}
