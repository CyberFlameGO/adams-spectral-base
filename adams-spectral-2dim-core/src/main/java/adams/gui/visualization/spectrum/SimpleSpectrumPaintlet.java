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
 * SimpleSpectrumPaintlet.java
 * Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.visualization.spectrum;

import adams.data.spectrum.Spectrum;
import adams.data.spectrum.SpectrumPoint;
import adams.data.spectrum.SpectrumUtils;
import adams.gui.core.AntiAliasingSupporter;
import adams.gui.core.GUIHelper;
import adams.gui.event.PaintEvent.PaintMoment;
import adams.gui.visualization.container.AbstractContainer;
import adams.gui.visualization.container.ColorContainer;
import adams.gui.visualization.core.AxisPanel;
import adams.gui.visualization.core.plot.Axis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

/**
 <!-- globalinfo-start -->
 * Paintlet for painting the spectral graph, without any markers.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-stroke-thickness &lt;float&gt; (property: strokeThickness)
 * &nbsp;&nbsp;&nbsp;The thickness of the stroke.
 * &nbsp;&nbsp;&nbsp;default: 1.0
 * &nbsp;&nbsp;&nbsp;minimum: 0.01
 * </pre>
 * 
 * <pre>-anti-aliasing-enabled &lt;boolean&gt; (property: antiAliasingEnabled)
 * &nbsp;&nbsp;&nbsp;If enabled, uses anti-aliasing for drawing lines.
 * &nbsp;&nbsp;&nbsp;default: true
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 2383 $
 */
public class SimpleSpectrumPaintlet
  extends AbstractSpectrumPaintlet
  implements AntiAliasingSupporter {

  /** for serialization. */
  private static final long serialVersionUID = -6475036298238205843L;

  /** whether anti-aliasing is enabled. */
  protected boolean m_AntiAliasingEnabled;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return "Paintlet for painting the spectral graph, without any markers.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "anti-aliasing-enabled", "antiAliasingEnabled",
	    GUIHelper.getBoolean(getClass(), "antiAliasingEnabled", true));
  }

  /**
   * Returns when this paintlet is to be executed.
   *
   * @return		when this paintlet is to be executed
   */
  @Override
  public PaintMoment getPaintMoment() {
    return PaintMoment.PAINT;
  }

  /**
   * Returns the currently set total ion count panel, can be null.
   *
   * @return		the panel
   */
  public SpectrumPanel getSpectrumPanel() {
    return (SpectrumPanel) getPanel();
  }

  /**
   * Returns the color for the data with the given index.
   *
   * @param index	the index of the spectrum
   * @return		the color for the spectrum
   */
  public Color getColor(int index) {
    Color	result;
    AbstractContainer	cont;
    
    result = Color.BLUE;
    cont   = getDataContainerPanel().getContainerManager().get(index);
    if (cont instanceof ColorContainer)
      result = ((ColorContainer) cont).getColor();
      
    return result;
  }

  /**
   * Sets whether to use anti-aliasing.
   *
   * @param value	if true then anti-aliasing is used
   */
  public void setAntiAliasingEnabled(boolean value) {
    m_AntiAliasingEnabled = value;
    memberChanged();
  }

  /**
   * Returns whether anti-aliasing is used.
   *
   * @return		true if anti-aliasing is used
   */
  public boolean isAntiAliasingEnabled() {
    return m_AntiAliasingEnabled;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String antiAliasingEnabledTipText() {
    return "If enabled, uses anti-aliasing for drawing lines.";
  }

  /**
   * Draws the data with the given color.
   *
   * @param g		the graphics context
   * @param data	the data to draw
   * @param color	the color to draw in
   */
  protected void drawData(Graphics g, Spectrum data, Color color) {
    List<SpectrumPoint>	points;
    SpectrumPoint	curr;
    int			currX;
    int			currY;
    int			prevX;
    int			prevY;
    AxisPanel		axisX;
    AxisPanel		axisY;
    int			i;
    int			start;
    int			end;

    if (data.size() == 0)
      return;

    points = data.toList();
    axisX  = getPanel().getPlot().getAxis(Axis.BOTTOM);
    axisY  = getPanel().getPlot().getAxis(Axis.LEFT);

    g.setColor(color);
    GUIHelper.configureAntiAliasing(g, m_AntiAliasingEnabled);

    // find the start and end points for painting
    start = SpectrumUtils.findClosestWaveNumber(points, (float) Math.floor(axisX.getMinimum()));
    if (start > 0)
      start--;
    end = SpectrumUtils.findClosestWaveNumber(points, (float) Math.ceil(axisX.getMaximum()));
    if (end < data.size() - 1)
      end++;

    currX = Integer.MIN_VALUE;
    currY = Integer.MIN_VALUE;
    prevX = axisX.valueToPos(points.get(start).getWaveNumber());
    prevY = axisY.valueToPos(points.get(start).getAmplitude());

    for (i = start; i <= end; i++) {
      curr = points.get(i);

      // determine coordinates
      currX = axisX.valueToPos(SpectrumPoint.toDouble(curr.getWaveNumber()));
      if ((i != start) && (i != end) && (currX == prevX))
	continue;
      currY = axisY.valueToPos(SpectrumPoint.toDouble(curr.getAmplitude()));

      // draw line
      g.drawLine(prevX, prevY, currX, currY);

      prevX = currX;
      prevY = currY;
    }
  }

  /**
   * The paint routine of the paintlet.
   *
   * @param g		the graphics context to use for painting
   * @param moment	what {@link PaintMoment} is currently being painted
   */
  @Override
  protected void doPerformPaint(Graphics g, PaintMoment moment) {
    int				i;
    Spectrum			data;
    SpectrumContainerManager	manager;
    SpectrumContainer		cont;

    // paint all points
    manager = (SpectrumContainerManager) getDataContainerPanel().getContainerManager();
    for (i = 0; i < manager.count(); i++) {
      cont = manager.get(i);
      if (!cont.isVisible())
	continue;
      if (manager.isFiltered() && !manager.isFiltered(i))
	continue;
      data = (Spectrum) cont.getPayload();
      drawData(g, data, getColor(i));
    }
  }
}
