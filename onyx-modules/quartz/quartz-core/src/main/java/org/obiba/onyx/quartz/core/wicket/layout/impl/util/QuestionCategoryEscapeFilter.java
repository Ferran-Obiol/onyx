/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.core.wicket.layout.impl.util;

import org.apache.wicket.model.IModel;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.QuestionCategory;

/**
 * Question category filter upon escape category flag.
 */
public class QuestionCategoryEscapeFilter implements IDataListFilter<IModel> {

  private static final long serialVersionUID = 1L;

  private boolean acceptEscape;

  public QuestionCategoryEscapeFilter(boolean acceptEscape) {
    super();
    this.acceptEscape = acceptEscape;
  }

  public boolean accept(IModel item) {
    QuestionCategory questionCategory = (QuestionCategory) item.getObject();
    return acceptEscape ? questionCategory.getCategory().isEscape() : !questionCategory.getCategory().isEscape();
  }

}
