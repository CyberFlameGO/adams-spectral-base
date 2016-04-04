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
 * DisplaySpectrum.java
 * Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 *
 */

package adams.gui.menu;

import adams.core.io.PlaceholderFile;
import adams.gui.application.AbstractApplicationFrame;
import adams.gui.application.ChildFrame;
import adams.gui.application.UserMode;
import adams.gui.visualization.container.AbstractContainer;
import adams.gui.visualization.container.AbstractContainerManager;
import adams.data.io.input.AbstractSpectrumReader;
import adams.data.spectrum.Spectrum;
import adams.gui.chooser.SpectrumFileChooser;

import java.util.List;

/**
 * Loads spectra from disk and displays them.
 * <br><br>
 * If parameters are provided, the first parameter must be the reader class (and
 * optional parameters) and the second one the actual file/directory to load.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2242 $
 */
public class DisplaySpectrum
  extends AbstractParameterHandlingMenuItemDefinition {

  /** for serialization. */
  private static final long serialVersionUID = -1729389254134928064L;

  /**
   * Initializes the menu item.
   *
   * @param owner	the owning application
   */
  public DisplaySpectrum(AbstractApplicationFrame owner) {
    super(owner);
  }

  /**
   * Returns the file name of the icon.
   *
   * @return		the filename or null if no icon available
   */
  @Override
  public String getIconName() {
    return "chart.gif";
  }

  /**
   * Launches the functionality of the menu item.
   */
  @Override
  public void launch() {
    AbstractSpectrumReader reader;
    if (m_Parameters.length == 1) {
      // TODO
      // use ChooserHelper to determine readers and display dialog for user
      // to further select/refine reader
      reader = null;
    }
    else if (m_Parameters.length == 2) {
      reader = (AbstractSpectrumReader) AbstractSpectrumReader.forCommandLine(m_Parameters[0]);
      reader.setInput(new PlaceholderFile(m_Parameters[1]));
    }
    else {
      // choose spectra
      SpectrumFileChooser chooser = new SpectrumFileChooser();
      int retVal = chooser.showOpenDialog(null);
      if (retVal != SpectrumFileChooser.APPROVE_OPTION)
	return;
      reader = (AbstractSpectrumReader) chooser.getReader();
    }
    // create frame
    adams.gui.visualization.spectrum.SpectrumExplorer panel = new adams.gui.visualization.spectrum.SpectrumExplorer();
    ChildFrame frame = createChildFrame(panel, 800, 600);
    frame.setJMenuBar(null);
    // load chromatograms
    List<Spectrum> chroms = reader.read();
    AbstractContainerManager manager = panel.getContainerManager();
    manager.startUpdate();
    for (int i = 0; i < chroms.size(); i++) {
      AbstractContainer cont = manager.newContainer(chroms.get(i));
      manager.add(cont);
    }
    manager.finishUpdate();
  }

  /**
   * Returns the title of the window (and text of menuitem).
   *
   * @return 		the title
   */
  @Override
  public String getTitle() {
    return "Display Spectrum";
  }

  /**
   * Whether the panel can only be displayed once.
   *
   * @return		true if the panel can only be displayed once
   */
  @Override
  public boolean isSingleton() {
    return false;
  }

  /**
   * Returns the user mode, which determines visibility as well.
   *
   * @return		the user mode
   */
  @Override
  public UserMode getUserMode() {
    return UserMode.BASIC;
  }

  /**
   * Returns the category of the menu item in which it should appear, i.e.,
   * the name of the menu.
   *
   * @return		the category/menu name
   */
  @Override
  public String getCategory() {
     return CATEGORY_VISUALIZATION;
 }
}