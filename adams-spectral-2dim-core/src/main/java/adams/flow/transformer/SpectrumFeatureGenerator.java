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
 * SpectrumFeatureGenerator.java
 * Copyright (C) 2014-2018 University of Waikato, Hamilton, New Zealand
 */
package adams.flow.transformer;

import adams.core.QuickInfoHelper;
import adams.data.spectrum.AbstractSpectrumFeatureGenerator;
import adams.data.spectrum.Amplitudes;
import adams.data.spectrum.Spectrum;
import adams.flow.core.Token;
import adams.flow.provenance.ActorType;
import adams.flow.provenance.Provenance;
import adams.flow.provenance.ProvenanceContainer;
import adams.flow.provenance.ProvenanceInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 <!-- globalinfo-start -->
 * Applies a spectrum feature generator to the incoming spectrum and outputs the generated features.
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- flow-summary-start -->
 * Input&#47;output:<br>
 * - accepts:<br>
 * &nbsp;&nbsp;&nbsp;knir.data.spectrum.Spectrum<br>
 * - generates:<br>
 * &nbsp;&nbsp;&nbsp;adams.data.spreadsheet.Row<br>
 * <br><br>
 <!-- flow-summary-end -->
 *
 <!-- options-start -->
 * <pre>-logging-level &lt;OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST&gt; (property: loggingLevel)
 * &nbsp;&nbsp;&nbsp;The logging level for outputting errors and debugging output.
 * &nbsp;&nbsp;&nbsp;default: WARNING
 * </pre>
 * 
 * <pre>-name &lt;java.lang.String&gt; (property: name)
 * &nbsp;&nbsp;&nbsp;The name of the actor.
 * &nbsp;&nbsp;&nbsp;default: SpectrumFeatureGenerator
 * </pre>
 * 
 * <pre>-annotation &lt;adams.core.base.BaseAnnotation&gt; (property: annotations)
 * &nbsp;&nbsp;&nbsp;The annotations to attach to this actor.
 * &nbsp;&nbsp;&nbsp;default: 
 * </pre>
 * 
 * <pre>-skip &lt;boolean&gt; (property: skip)
 * &nbsp;&nbsp;&nbsp;If set to true, transformation is skipped and the input token is just forwarded 
 * &nbsp;&nbsp;&nbsp;as it is.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-stop-flow-on-error &lt;boolean&gt; (property: stopFlowOnError)
 * &nbsp;&nbsp;&nbsp;If set to true, the flow gets stopped in case this actor encounters an error;
 * &nbsp;&nbsp;&nbsp; useful for critical actors.
 * &nbsp;&nbsp;&nbsp;default: false
 * </pre>
 * 
 * <pre>-algorithm &lt;knir.data.spectrum.AbstractSpectrumFeatureGenerator&gt; (property: algorithm)
 * &nbsp;&nbsp;&nbsp;The feature generation algorithm to use.
 * &nbsp;&nbsp;&nbsp;default: knir.data.spectrum.Amplitudes -converter \"adams.data.featureconverter.SpreadSheetFeatureConverter -data-row-type adams.data.spreadsheet.DenseDataRow -spreadsheet-type adams.data.spreadsheet.SpreadSheet\"
 * </pre>
 * 
 <!-- options-end -->
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class SpectrumFeatureGenerator
  extends AbstractTransformer
  implements FeatureGenerator<AbstractSpectrumFeatureGenerator> {

  /** for serialization. */
  private static final long serialVersionUID = -7637423921443102660L;

  /** the key for storing the current objects in the backup. */
  public final static String BACKUP_QUEUE = "queue";

  /** the feature generator to use. */
  protected AbstractSpectrumFeatureGenerator m_Algorithm;

  /** the generated objects. */
  protected ArrayList m_Queue;

  /**
   * Returns a string describing the object.
   *
   * @return 			a description suitable for displaying in the gui
   */
  @Override
  public String globalInfo() {
    return
        "Applies a spectrum feature generator to the incoming spectrum and outputs "
      + "the generated features.";
  }

  /**
   * Adds options to the internal list of options.
   */
  @Override
  public void defineOptions() {
    super.defineOptions();

    m_OptionManager.add(
	    "algorithm", "algorithm",
	    new Amplitudes());
  }

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();
    
    m_Queue = new ArrayList();
  }
  
  /**
   * Resets the scheme.
   */
  @Override
  protected void reset() {
    super.reset();
    
    m_Queue.clear();
  }

  /**
   * Sets the algorithm to use.
   *
   * @param value	the algorithm
   */
  public void setAlgorithm(AbstractSpectrumFeatureGenerator value) {
    m_Algorithm = value;
    reset();
  }

  /**
   * Returns the algorithm in use.
   *
   * @return		the algorithm
   */
  public AbstractSpectrumFeatureGenerator getAlgorithm() {
    return m_Algorithm;
  }

  /**
   * Returns the tip text for this property.
   *
   * @return 		tip text for this property suitable for
   * 			displaying in the GUI or for listing the options.
   */
  public String algorithmTipText() {
    return "The feature generation algorithm to use.";
  }

  /**
   * Returns a quick info about the actor, which will be displayed in the GUI.
   *
   * @return		null if no info available, otherwise short string
   */
  @Override
  public String getQuickInfo() {
    return QuickInfoHelper.toString(this, "algorithm", m_Algorithm);
  }

  /**
   * Removes entries from the backup.
   */
  @Override
  protected void pruneBackup() {
    super.pruneBackup();

    pruneBackup(BACKUP_QUEUE);
  }

  /**
   * Backs up the current state of the actor before update the variables.
   *
   * @return		the backup
   */
  @Override
  protected Hashtable<String,Object> backupState() {
    Hashtable<String,Object>	result;

    result = super.backupState();

    result.put(BACKUP_QUEUE, m_Queue);

    return result;
  }

  /**
   * Restores the state of the actor before the variables got updated.
   *
   * @param state	the backup of the state to restore from
   */
  @Override
  protected void restoreState(Hashtable<String,Object> state) {
    if (state.containsKey(BACKUP_QUEUE)) {
      m_Queue = (ArrayList) state.get(BACKUP_QUEUE);
      state.remove(BACKUP_QUEUE);
    }

    super.restoreState(state);
  }

  /**
   * Returns the class that the consumer accepts.
   *
   * @return		the Class of objects that can be processed
   */
  public Class[] accepts() {
    return new Class[]{Spectrum.class};
  }

  /**
   * Returns the class of objects that it generates.
   *
   * @return		the output type
   */
  public Class[] generates() {
    if (m_Algorithm == null)
      return new Class[]{Object.class};
    else
      return new Class[]{m_Algorithm.getRowFormat()};
  }

  /**
   * Executes the flow item.
   *
   * @return		null if everything is fine, otherwise error message
   */
  @Override
  protected String doExecute() {
    String	result;

    result = null;

    m_Queue.clear();
    try {
      m_Queue.addAll(Arrays.asList(m_Algorithm.generate((Spectrum) m_InputToken.getPayload())));
    }
    catch (Exception e) {
      result = handleException("Failed to generate features: ", e);
    }

    return result;
  }

  /**
   * Updates the provenance information in the provided container.
   *
   * @param cont	the provenance container to update
   */
  public void updateProvenance(ProvenanceContainer cont) {
    if (Provenance.getSingleton().isEnabled()) {
      if (m_InputToken.hasProvenance())
	cont.setProvenance(m_InputToken.getProvenance().getClone());
      cont.addProvenance(new ProvenanceInformation(ActorType.PREPROCESSOR, m_InputToken.getPayload().getClass(), this, ((Token) cont).getPayload().getClass()));
    }
  }

  /**
   * Checks whether there is pending output to be collected after
   * executing the flow item.
   *
   * @return		true if there is pending output
   */
  @Override
  public boolean hasPendingOutput() {
    return (m_Queue.size() > 0);
  }

  /**
   * Returns the generated token.
   *
   * @return		the generated token
   */
  @Override
  public Token output() {
    Token	result;

    result = new Token(m_Queue.get(0));
    m_Queue.remove(0);

    updateProvenance(result);

    return result;
  }

  /**
   * Cleans up after the execution has finished.
   */
  @Override
  public void wrapUp() {
    m_Queue.clear();

    super.wrapUp();
  }
}
