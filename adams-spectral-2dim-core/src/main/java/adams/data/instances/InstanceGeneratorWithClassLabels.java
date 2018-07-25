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
 * InstanceGeneratorWithSampleID.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package adams.data.instances;

import adams.core.base.BaseString;

/**
 * Instance generator with a support for class labels.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface InstanceGeneratorWithClassLabels {

  /**
   * Sets the labels for a nominal class attribute.
   *
   * @param value	the labels
   */
  public void setClassLabels(BaseString[] value);

  /**
   * Returns the labels for a nominal class attribute.
   *
   * @return		the labels
   */
  public BaseString[] getClassLabels();

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String classLabelsTipText();
}
