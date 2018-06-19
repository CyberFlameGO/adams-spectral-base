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
 * MultiThreeWayDataFeatureGenerator.java
 * Copyright (C) 2018 University of Waikato, Hamilton, New Zealand
 */
package adams.data.threewayfeatures;

import adams.core.Utils;
import adams.data.featureconverter.HeaderDefinition;
import adams.data.threeway.ThreeWayData;

import java.util.ArrayList;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Applies multiple generators to the same 3-way data and merges the generated a feature vectors side-by-side. If one of the generators should create fewer a feature vectors, missing values are used in that case.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 *
 * <pre>-converter &lt;adams.data.featureconverter.AbstractFeatureConverter&gt; (property: converter)
 * &nbsp;&nbsp;&nbsp;The feature converter to use to produce the output data.
 * &nbsp;&nbsp;&nbsp;default: adams.data.featureconverter.SpreadSheet -data-row-type adams.data.spreadsheet.DenseDataRow -spreadsheet-type adams.data.spreadsheet.DefaultSpreadSheet
 * </pre>
 *
 * <pre>-prefix &lt;java.lang.String&gt; (property: prefix)
 * &nbsp;&nbsp;&nbsp;The (optional) prefix to use for the feature names.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-field &lt;adams.data.report.Field&gt; [-field ...] (property: fields)
 * &nbsp;&nbsp;&nbsp;The fields to add to the output.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-notes &lt;adams.core.base.BaseString&gt; [-notes ...] (property: notes)
 * &nbsp;&nbsp;&nbsp;The notes to add as attributes to the generated data, eg 'PROCESS INFORMATION'
 * &nbsp;&nbsp;&nbsp;.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 * <pre>-sub-generator &lt;adams.data.threewayfeatures.AbstractThreeWayDataFeatureGenerator&gt; [-sub-generator ...] (property: subGenerators)
 * &nbsp;&nbsp;&nbsp;The generators to apply to the data.
 * &nbsp;&nbsp;&nbsp;default:
 * </pre>
 *
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class MultiThreeWayDataFeatureGenerator
  extends AbstractThreeWayDataFeatureGenerator {

  /** for serialization. */
  private static final long serialVersionUID = -4136037171201268286L;

  /** the generators to use. */
  protected AbstractThreeWayDataFeatureGenerator[] m_SubGenerators;

  /** the sub-headers. */
  protected HeaderDefinition[] m_SubHeaders;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
      "Applies multiple generators to the same 3-way data and merges the "
        + "generated a feature vectors side-by-side. If one of the "
        + "generators should create fewer a feature vectors, missing "
        + "values are used in that case.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
      "sub-generator", "subGenerators",
      new AbstractThreeWayDataFeatureGenerator[0]);
  }

  /**
   * Sets the generators to use.
   *
   * @param value 	the generators
   */
  public void setSubGenerators(AbstractThreeWayDataFeatureGenerator[] value) {
    m_SubGenerators = value;
    reset();
  }

  /**
   * Returns the generators to use.
   *
   * @return 		the generators
   */
  public AbstractThreeWayDataFeatureGenerator[] getSubGenerators() {
    return m_SubGenerators;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String subGeneratorsTipText() {
    return "The generators to apply to the data.";
  }

  /**
   * Generates the prefix for the specified flattener.
   *
   * @param index	the index of the flattener
   * @return		the prefix
   */
  protected String createPrefix(int index) {
    String	result;

    result = m_Prefix;
    result = result.replace("#", "" + (index + 1));

    return result;
  }

  /**
   * Creates the header from template data.
   *
   * @param data	the data to act as a template
   * @return		the generated header
   */
  @Override
  public HeaderDefinition createHeader(ThreeWayData data) {
    HeaderDefinition	result;
    int			i;
    int			n;
    String		name;

    m_SubHeaders = new HeaderDefinition[m_SubGenerators.length];
    for (i = 0; i < m_SubHeaders.length; i++) {
      m_SubHeaders[i] = m_SubGenerators[i].createHeader(data);
      m_SubHeaders[i] = m_SubGenerators[i].postProcessHeader(m_SubHeaders[i]);
    }

    // disambiguate the attribute names
    if (!m_Prefix.isEmpty()) {
      for (i = 0; i < m_SubHeaders.length; i++) {
        for (n = 0; n < m_SubHeaders[i].size(); n++) {
          name = createPrefix(i) + m_SubHeaders[i].getName(n);
          m_SubHeaders[i].rename(n, name);
        }
      }
    }

    result = new HeaderDefinition();
    if (m_SubHeaders.length > 0) {
      for (i = 0; i < m_SubHeaders.length; i++)
        result.add(m_SubHeaders[i]);
    }

    return result;
  }

  /**
   * Performs the actual feature generation.
   *
   * @param data	the data to process
   * @return		the generated features
   */
  @Override
  public List<Object>[] generateRows(ThreeWayData data) {
    List<Object>[]	result;
    List<Object>[][]	sub;
    int			i;
    int			n;
    int			max;
    int			subMax;
    List<Object>	filler;

    // generate features
    sub = new List[m_SubGenerators.length][];
    for (i = 0; i < m_SubGenerators.length; i++)
      sub[i] = m_SubGenerators[i].postProcessRows(data, m_SubGenerators[i].generateRows(data));

    // fill with missing values, if necessary
    max = 0;
    for (i = 0; i < sub.length; i++)
      max = Math.max(max, sub[i].length);
    for (i = 0; i < sub.length; i++) {
      subMax = 0;
      for (n = 0; n < sub[i].length; n++)
        subMax = Math.max(subMax, sub[i][n].size());
      filler = new ArrayList<>();
      for (n = 0; n < subMax; n++)
        filler.add(null);
      sub[i] = (List<Object>[]) Utils.adjustArray(sub[i], max, filler);
    }

    // merge datasets
    if (sub.length > 0) {
      result = new List[max];
      for (n = 0; n < max; n++) {
        result[n] = new ArrayList<>();
        for (i = 0; i < sub.length; i++)
          result[n].addAll(sub[i][n]);
      }
    }
    else {
      result    = new List[1];
      result[0] = new ArrayList<>();
    }

    return result;
  }
}
