/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.editor.page;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Page;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Questionnaire;
import org.obiba.onyx.quartz.editor.form.AbstractQuestionnaireElementPanelForm;
import org.obiba.onyx.quartz.editor.locale.ui.LocalesPropertiesAjaxTabbedPanel;
import org.obiba.onyx.wicket.behavior.RequiredFormFieldBehavior;

@SuppressWarnings("serial")
public class PagePropertiesPanel extends AbstractQuestionnaireElementPanelForm<Page> {

  public PagePropertiesPanel(String id, IModel<Page> model, Questionnaire questionnaireParent, ModalWindow modalWindow) {
    super(id, model, questionnaireParent, modalWindow);
    createComponent();
  }

  public void createComponent() {
    TextField<String> name = new TextField<String>("name", new PropertyModel<String>(super.getDefaultModel(), "name"), String.class);
    name.add(new RequiredFormFieldBehavior());
    name.add(new StringValidator.MaximumLengthValidator(20));
    form.add(name);

    form.add(new LocalesPropertiesAjaxTabbedPanel("localesPropertiesTabs", form.getModelObject(), localePropertiesModel));
  }

}