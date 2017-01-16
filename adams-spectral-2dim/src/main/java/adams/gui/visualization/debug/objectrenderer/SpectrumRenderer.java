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
 * SpectrumRenderer.java
 * Copyright (C) 2015 University of Waikato, Hamilton, NZ
 */

package adams.gui.visualization.debug.objectrenderer;

import adams.core.ClassLocator;
import adams.data.spectrum.Spectrum;
import adams.gui.visualization.spectrum.SpectrumContainer;
import adams.gui.visualization.spectrum.SpectrumPanel;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Renders Spectrum objects.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class SpectrumRenderer
  extends AbstractObjectRenderer {

  private static final long serialVersionUID = -3528006886476495175L;

  /**
   * Checks whether the renderer can handle the specified class.
   *
   * @param cls		the class to check
   * @return		true if the renderer can handle this type of object
   */
  @Override
  public boolean handles(Class cls) {
    return ClassLocator.isSubclass(Spectrum.class, cls);
  }

  /**
   * Performs the actual rendering.
   *
   * @param obj		the object to render
   * @param panel	the panel to render into
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doRender(Object obj, JPanel panel) {
    Spectrum 		data;
    SpectrumPanel spPanel;
    SpectrumContainer cont;

    data    = (Spectrum) obj;
    spPanel = new SpectrumPanel();
    cont    = spPanel.getContainerManager().newContainer(data);
    spPanel.getContainerManager().add(cont);
    panel.add(spPanel, BorderLayout.CENTER);

    return null;
  }
}
