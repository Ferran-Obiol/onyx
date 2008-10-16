/***********************************************************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 **********************************************************************************************************************/
package org.obiba.onyx.quartz.core.wicket.layout.impl;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Question;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.QuestionCategory;
import org.obiba.onyx.quartz.core.wicket.layout.QuestionPanel;
import org.obiba.onyx.quartz.core.wicket.model.QuestionnaireStringResourceModel;

public class SingleChoiceQuestionPanel extends QuestionPanel {

  private static final long serialVersionUID = 2951128797454847260L;

  @SuppressWarnings("serial")
  public SingleChoiceQuestionPanel(String id, IModel model) {
    super(id, model);

    Question question = (Question) model.getObject();
    add(new Label("label", new QuestionnaireStringResourceModel(question, "label", null)));
    add(new Label("instructions", new QuestionnaireStringResourceModel(question, "instructions", null)));
    add(new Label("caption", new QuestionnaireStringResourceModel(question, "caption", null)));

    final RadioGroup radioGroup = new RadioGroup("categories", new Model());
    add(radioGroup);
    ListView radioList = new ListView("category", question.getQuestionCategories()) {

      @Override
      protected void populateItem(ListItem item) {
        QuestionCategory questionCategory = (QuestionCategory) item.getModelObject();
        Radio radio = new Radio("radio", item.getModel());
        radio.setLabel(new QuestionnaireStringResourceModel(questionCategory, "label", null));

        FormComponentLabel radioLabel = new FormComponentLabel("categoryLabel", radio);
        item.add(radioLabel);
        radioLabel.add(radio);
        radioLabel.add(new Label("label", radio.getLabel()).setRenderBodyOnly(true));

        final OpenAnswerDefinitionPanel openField;
        if(questionCategory.getCategory().getOpenAnswerDefinition() != null) {
          openField = new OpenAnswerDefinitionPanel("open", new Model(questionCategory));
          radioLabel.add(openField);
          openField.setFieldEnabled(questionCategory.isSelected());
        } else {
          openField = null;
          radioLabel.add(new EmptyPanel("open"));
        }

        radio.add(new AjaxEventBehavior("onchange") {

          @Override
          protected void onEvent(AjaxRequestTarget target) {
            if(openField != null) {
              openField.setFieldEnabled(!openField.isFieldEnabled());
              target.addComponent(openField);
            }
          }

        });

        if(questionCategory.isSelected()) {
          radioGroup.setModel(item.getModel());
        }
      }

    }.setReuseItems(true);
    radioGroup.add(radioList);
    radioGroup.setRequired(question.isRequired());

  }

  public void onNext(AjaxRequestTarget target) {
    // TODO Auto-generated method stub

  }

  public void onPrevious(AjaxRequestTarget target) {
    // TODO Auto-generated method stub

  }

}
