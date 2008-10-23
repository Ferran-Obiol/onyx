/***********************************************************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 **********************************************************************************************************************/
/**
 * State interrupted for Quartz module
 * 
 * @author acarey
 * 
 * Coming from states: inProgress Possible forward states/actions/transitions: cancel, resume, notApplicable? On cancel
 * and notApplicable transitions, current questionnaireParticipant is deleted from database (with its dependencies)
 */
package org.obiba.onyx.quartz.engine.state;

import org.obiba.onyx.engine.Action;
import org.obiba.onyx.engine.ActionDefinitionBuilder;
import org.obiba.onyx.engine.ActionType;
import org.obiba.onyx.engine.state.TransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class QuartzInterruptedState extends AbstractQuartzStageState implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(QuartzInterruptedState.class);

  public void afterPropertiesSet() throws Exception {
    addAction(ActionDefinitionBuilder.create(ActionType.STOP, "Cancel").setDescription("You may explain why you are cancelling this stage.").setAskParticipantId(true).setAskPassword(true).getActionDefinition());
    addAction(ActionDefinitionBuilder.create(ActionType.EXECUTE, "Resume").setAskParticipantId(true).setAskPassword(true).getActionDefinition());
  }

  public String getName() {
    return "Quartz.Interrupted";
  }

  @Override
  public void stop(Action action) {
    log.info("Quartz Stage {} is canceling", super.getStage().getName());
    cancelQuestionnaireParticipant();
    if(areDependenciesCompleted() != null && areDependenciesCompleted()) {
      castEvent(TransitionEvent.CANCEL);
    } else {
      castEvent(TransitionEvent.INVALID);
    }
  }

  @Override
  protected boolean wantTransitionEvent(TransitionEvent transitionEvent) {
    if(transitionEvent.equals(TransitionEvent.NOTAPPLICABLE)) cancelQuestionnaireParticipant();
    return super.wantTransitionEvent(transitionEvent);
  }

  @Override
  public void execute(Action action) {
    log.info("Quartz Stage {} is resuming", super.getStage().getName());
    castEvent(TransitionEvent.RESUME);
  }
}
