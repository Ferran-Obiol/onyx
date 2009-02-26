/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.core.engine.questionnaire.question.validation;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.obiba.onyx.core.data.IDataSource;
import org.obiba.onyx.core.service.ActiveInterviewService;
import org.obiba.onyx.util.data.ComparisonOperator;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.util.data.DataType;
import org.obiba.onyx.wicket.data.IDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates a {@link Data} value by comparing it to the one provided by the {@link IDataSource} in the context of the
 * currently administered questionnaire.
 */
public class DataSourceValidator implements IDataValidator {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(DataSourceValidator.class);

  private IDataSource dataSource;

  private ComparisonOperator comparisonOperator;

  public DataSourceValidator(ComparisonOperator comparisonOperator, IDataSource dataSource) {
    this.dataSource = dataSource;
    this.comparisonOperator = comparisonOperator;
  }

  public DataType getDataType() {
    return null;
  }

  public void validate(IValidatable validatable) {
    InnerDataSourceValidator validator = new InnerDataSourceValidator();
    validator.validate(validatable);
  }

  @SuppressWarnings("serial")
  private class InnerDataSourceValidator implements IValidator {

    @SpringBean(name = "activeInterviewService")
    private ActiveInterviewService activeInterviewService;

    public InnerDataSourceValidator() {
      InjectorHolder.getInjector().inject(this);
    }

    public void validate(IValidatable validatable) {
      Data dataToCompare = (Data) validatable.getValue();
      Data data = dataSource.getData(activeInterviewService.getParticipant());
      // TODO deal with units
      // String sourceUnit = dataSource.getUnit();
      // String validatableUnit = getUnit();
      log.debug("comparing: {} {} {}", new Object[] { dataToCompare, comparisonOperator, data });
      if(data != null && !comparisonOperator.compare(dataToCompare, data)) {
        ValidationError error = null;

        switch(comparisonOperator) {
        case eq:
          error = newValidationError("ExpectedToBeEqual", data, dataToCompare);
          break;
        case ne:
          error = newValidationError("ExpectedToBeDifferent", data, dataToCompare);
          break;
        case lt:
          error = newValidationError("ExpectedToBeLower", data, dataToCompare);
          break;
        case le:
          error = newValidationError("ExpectedToBeLowerEqual", data, dataToCompare);
          break;
        case gt:
          error = newValidationError("ExpectedToBeGreater", data, dataToCompare);
          break;
        case ge:
          error = newValidationError("ExpectedToBeGreaterEqual", data, dataToCompare);
          break;
        default:
          break;
        }

        if(error != null) {
          validatable.error(error);
        }
      }
    }

    private ValidationError newValidationError(String message, Data data, Data dataToCompare) {
      ValidationError error = new ValidationError();
      error.addMessageKey("DataSourceValidator." + message);
      if(data != null) error.setVariable("expected", data.getValue());
      if(dataToCompare != null) error.setVariable("found", dataToCompare.getValue());
      return error;
    }
  }

  public String getUnit() {
    return dataSource != null ? dataSource.getUnit() : null;
  }

}
