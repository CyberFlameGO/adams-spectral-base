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
 * SpectrumZoomOverviewPaintlet.java
 * Copyright (C) 2012 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.visualization.spectrum;

import adams.gui.visualization.container.AbstractDataContainerZoomOverviewPaintlet;
import adams.gui.visualization.container.AbstractDataContainerZoomOverviewPanel;

/**
 * Highlights the current zoom in the spectrum panel.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1279 $
 */
public class SpectrumZoomOverviewPaintlet
  extends AbstractDataContainerZoomOverviewPaintlet<SpectrumPanel> {

  /** for serialization. */
  private static final long serialVersionUID = -8561195451397229941L;

  /**
   * Returns the panel to obtain plot and containers from.
   * 
   * @return		the panel
   */
  protected SpectrumPanel getContainerPanel() {
    return (SpectrumPanel) ((AbstractDataContainerZoomOverviewPanel) getPanel()).getDataContainerPanel();
  }
}
