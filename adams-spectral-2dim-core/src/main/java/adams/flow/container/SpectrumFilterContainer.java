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
 * SpectrumFilterContainer.java
 * Copyright (C) 2017 University of Waikato, Hamilton, NZ
 */

package adams.flow.container;

import adams.core.Utils;
import adams.data.filter.Filter;
import adams.data.spectrum.Spectrum;

/**
 * Container for spectrum filters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class SpectrumFilterContainer
  extends AbstractFilterContainer<Filter, Spectrum> {

  private static final long serialVersionUID = -7791501313124716613L;

  /**
   * Initializes the container.
   * <br><br>
   * Only used for generating help information.
   */
  public SpectrumFilterContainer() {
    super(null, null, null);
  }

  /**
   * Initializes the container with the filter and the associated data.
   *
   * @param filter	the filter
   * @param data	the dataset, can be null
   */
  public SpectrumFilterContainer(Object input, Filter filter, Spectrum data) {
    super(input, filter, data);
  }

  /**
   * Initializes help strings specific to the filter.
   */
  protected void initFilterHelp() {
    addHelp(VALUE_INPUT, "input object; " + Utils.classesToString(new Class[]{Spectrum.class, Spectrum[].class}));
    addHelp(VALUE_FILTER, "filter object; " + adams.data.filter.Filter.class.getName());
    addHelp(VALUE_DATA, "data object; " + Utils.classesToString(new Class[]{Spectrum.class, Spectrum[].class}));
  }
}
