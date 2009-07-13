/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.ruby.core.domain;

import java.io.Serializable;

import org.obiba.onyx.core.data.IDataSource;
import org.obiba.onyx.core.domain.participant.Participant;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.util.data.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;

public class ConditionalMessage implements MessageSourceResolvable, Serializable {
  //
  // Constants
  //

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(ConditionalMessage.class);

  //
  // Instance Variables
  //

  private String code;

  private Object[] arguments;

  private IDataSource condition;

  //
  // Constructors
  //

  public ConditionalMessage() {
    super();
  }

  //
  // MessageSourceResolvable Methods
  //

  public Object[] getArguments() {
    return arguments;
  }

  public String[] getCodes() {
    return new String[] { code };
  }

  public String getDefaultMessage() {
    return code;
  }

  //
  // Methods
  //

  public void setCode(String code) {
    this.code = code;
  }

  public void setCondition(IDataSource condition) {
    this.condition = condition;
  }

  public IDataSource getCondition() {
    return condition;
  }

  /**
   * Indicates whether the message should be displayed, based on the message's condition.
   * 
   * @param participant the currently interview participant
   * @return <code>true</code> if the message should be displayed (i.e., if either there is no condition or the
   * condition is satisfied)
   */
  public boolean shouldDisplay(Participant participant) {
    if(condition == null) return true;

    Data conditionData = condition.getData(participant);

    if(conditionData != null) {
      if(conditionData.getType().equals(DataType.BOOLEAN)) {
        return (Boolean) conditionData.getValue();
      } else {
        log.error("Condition data is of type {}, expected BOOLEAN", conditionData.getType());
      }
    } else {
      log.info("Condition data is null, treating as false");
    }

    return false;
  }
}