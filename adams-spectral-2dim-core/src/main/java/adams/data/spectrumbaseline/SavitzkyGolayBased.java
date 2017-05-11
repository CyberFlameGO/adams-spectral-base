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
 * SavitzkyGolayBased.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */
package adams.data.spectrumbaseline;

import adams.data.baseline.AbstractSavitzkyGolayBased;
import adams.data.container.DataPoint;
import adams.data.filter.AbstractSavitzkyGolay;
import adams.data.spectrumfilter.SavitzkyGolay;
import adams.data.spectrum.Spectrum;
import adams.data.spectrum.SpectrumPoint;
import adams.data.spectrum.SpectrumUtils;

/**
 <!-- globalinfo-start -->
 * A baseline correction scheme that uses SavitzkyGolay smoothing to determine the baseline.<br>
 * <br>
 * For more information see:<br>
 * <br>
 * A. Savitzky, Marcel J.E. Golay (1964). Smoothing and Differentiation of Data by Simplified Least Squares Procedures. Analytical Chemistry. 36:1627-1639.<br>
 * <br>
 * William H. Press, Saul A. Teukolsky, William T. Vetterling, Brian P. Flannery (1992). Savitzky-Golay Smoothing Filters.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-polynomial &lt;int&gt; (property: polynomialOrder)
 * &nbsp;&nbsp;&nbsp;The polynomial order to use, must be at least 2.
 * &nbsp;&nbsp;&nbsp;default: 2
 * &nbsp;&nbsp;&nbsp;minimum: 2
 * </pre>
 * 
 * <pre>-left &lt;int&gt; (property: numPointsLeft)
 * &nbsp;&nbsp;&nbsp;The number of points left of a data point, &gt;= 0.
 * &nbsp;&nbsp;&nbsp;default: 3
 * &nbsp;&nbsp;&nbsp;minimum: 0
 * </pre>
 * 
 * <pre>-right &lt;int&gt; (property: numPointsRight)
 * &nbsp;&nbsp;&nbsp;The number of points right of a data point, &gt;= 0.
 * &nbsp;&nbsp;&nbsp;default: 3
 * &nbsp;&nbsp;&nbsp;minimum: 0
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2242 $
 */
public class SavitzkyGolayBased
  extends AbstractSavitzkyGolayBased<Spectrum> {

  /** for serialization. */
  private static final long serialVersionUID = 5481294269070387913L;

  /**
   * Returns a new instance of a SavitzkyGolay filter.
   * 
   * @return		the filter
   */
  @Override
  protected AbstractSavitzkyGolay getFilter() {
    SavitzkyGolay	result;
    
    result = new SavitzkyGolay();
    result.setDerivativeOrder(0);
    
    return result;
  }

  /**
   * Retrieves the data point from the original signal that corresponds to
   * the provided new one.
   * 
   * @param newPoint	the point to obtain the corresponding one for
   * @param original	the original signal
   * @return		the corresponding data point, null if not found
   */
  @Override
  protected DataPoint getOriginalPoint(DataPoint newPoint, Spectrum original) {
    int		index;
    
    index = SpectrumUtils.findClosestWaveNumber(original.toList(), ((SpectrumPoint) newPoint).getWaveNumber());
    if (index < 0)
      return null;
    else
      return (DataPoint) original.toList().get(index);
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
