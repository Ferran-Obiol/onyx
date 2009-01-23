/***********************************************************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 **********************************************************************************************************************/
package org.obiba.onyx.quartz.engine.state;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.obiba.onyx.core.domain.participant.Participant;
import org.obiba.onyx.core.service.ActiveInterviewService;
import org.obiba.onyx.engine.Action;
import org.obiba.onyx.engine.Stage;
import org.obiba.onyx.engine.state.ITransitionEventSink;
import org.obiba.onyx.engine.state.TransitionEvent;
import org.obiba.onyx.quartz.core.domain.answer.QuestionnaireParticipant;
import org.obiba.onyx.quartz.core.service.ActiveQuestionnaireAdministrationService;
import org.obiba.onyx.quartz.core.service.QuestionnaireParticipantService;

public class QuartzCompletedStateTest {

  private ITransitionEventSink eventSinkMock;

  private QuestionnaireParticipantService questionnaireParticipantServiceMock;

  private ActiveInterviewService activeInterviewServiceMock;

  private ActiveQuestionnaireAdministrationService activeQuestionnaireAdministrationServiceMock;

  private QuartzCompletedState completedState;

  /**
   * Tests that the questionnaireParticipant is deleted if stage canceled or not applicable
   */

  @Before
  public void setUp() {
    eventSinkMock = createMock(ITransitionEventSink.class);
    questionnaireParticipantServiceMock = createMock(QuestionnaireParticipantService.class);
    activeInterviewServiceMock = createMock(ActiveInterviewService.class);
    activeQuestionnaireAdministrationServiceMock = createMock(ActiveQuestionnaireAdministrationService.class);
  }

  @Test
  public void testQuestionnaireParticipantDeleteWhenCancel() {

    completedState = new QuartzCompletedState() {
      @Override
      protected Boolean areDependenciesCompleted() {
        return true;
      }
    };

    setCompletedState();

    expect(activeInterviewServiceMock.getParticipant()).andReturn(new Participant());
    expect(questionnaireParticipantServiceMock.getQuestionnaireParticipant((Participant) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(new QuestionnaireParticipant());
    questionnaireParticipantServiceMock.deleteQuestionnaireParticipant((QuestionnaireParticipant) EasyMock.anyObject());
    eventSinkMock.castEvent(TransitionEvent.CANCEL);

    Action completeAction = new Action();

    replay(eventSinkMock);
    replay(activeInterviewServiceMock);
    replay(questionnaireParticipantServiceMock);
    replay(activeQuestionnaireAdministrationServiceMock);

    completedState.stop(completeAction);

    verify(eventSinkMock);
    verify(activeInterviewServiceMock);
    verify(questionnaireParticipantServiceMock);
    verify(activeQuestionnaireAdministrationServiceMock);
  }

  @Test
  public void testQuestionnaireParticipantDeleteWhenInvalid() {

    completedState = new QuartzCompletedState() {
      @Override
      protected Boolean areDependenciesCompleted() {
        return null;
      }
    };

    setCompletedState();

    expect(activeInterviewServiceMock.getParticipant()).andReturn(new Participant());
    expect(questionnaireParticipantServiceMock.getQuestionnaireParticipant((Participant) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(new QuestionnaireParticipant());
    questionnaireParticipantServiceMock.deleteQuestionnaireParticipant((QuestionnaireParticipant) EasyMock.anyObject());
    eventSinkMock.castEvent(TransitionEvent.INVALID);

    Action completeAction = new Action();

    replay(eventSinkMock);
    replay(activeInterviewServiceMock);
    replay(questionnaireParticipantServiceMock);
    replay(activeQuestionnaireAdministrationServiceMock);

    completedState.stop(completeAction);

    verify(eventSinkMock);
    verify(activeInterviewServiceMock);
    verify(questionnaireParticipantServiceMock);
    verify(activeQuestionnaireAdministrationServiceMock);
  }

  @Test
  public void testQuestionnaireParticipantDeleteWhenNotApplicable() {

    completedState = new QuartzCompletedState();
    setCompletedState();

    expect(activeInterviewServiceMock.getParticipant()).andReturn(new Participant());
    expect(questionnaireParticipantServiceMock.getQuestionnaireParticipant((Participant) EasyMock.anyObject(), (String) EasyMock.anyObject())).andReturn(new QuestionnaireParticipant());
    questionnaireParticipantServiceMock.deleteQuestionnaireParticipant((QuestionnaireParticipant) EasyMock.anyObject());

    replay(activeInterviewServiceMock);
    replay(questionnaireParticipantServiceMock);
    replay(activeQuestionnaireAdministrationServiceMock);

    // testing when notApplicable Transition event is passed: deletion should happen
    completedState.wantTransitionEvent(TransitionEvent.NOTAPPLICABLE);

    // testing when another Transition event is passed: NO deletion should happen
    completedState.wantTransitionEvent(TransitionEvent.INVALID);

    verify(activeInterviewServiceMock);
    verify(questionnaireParticipantServiceMock);
    verify(activeQuestionnaireAdministrationServiceMock);
  }

  private void setCompletedState() {
    completedState.setStage(newTestStage());
    completedState.setEventSink(eventSinkMock);
    completedState.setQuestionnaireParticipantService(questionnaireParticipantServiceMock);
    completedState.setActiveInterviewService(activeInterviewServiceMock);
  }

  private Stage newTestStage() {
    Stage s = new Stage();
    s.setName("QUE1");
    return (s);
  }
}
