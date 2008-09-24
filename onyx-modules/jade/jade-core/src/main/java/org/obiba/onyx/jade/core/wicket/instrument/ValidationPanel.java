package org.obiba.onyx.jade.core.wicket.instrument;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.SpringWebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.obiba.core.service.EntityQueryService;
import org.obiba.onyx.core.service.UserSessionService;
import org.obiba.onyx.jade.core.domain.instrument.Instrument;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentInputParameter;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentOutputParameter;
import org.obiba.onyx.jade.core.domain.run.InstrumentRunValue;
import org.obiba.onyx.jade.core.service.ActiveInstrumentRunService;
import org.obiba.wicket.markup.html.panel.KeyValueDataPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationPanel extends Panel {

  private static final long serialVersionUID = 3008363510160516288L;

  @SuppressWarnings("unused")
  private static final Logger log = LoggerFactory.getLogger(ValidationPanel.class);

  @SpringBean
  private EntityQueryService queryService;

  @SpringBean
  private ActiveInstrumentRunService activeInstrumentRunService;

  @SpringBean(name = "userSessionService")
  private UserSessionService userSessionService;

  public ValidationPanel(String id) {
    super(id);
    setOutputMarkupId(true);

    Instrument instrument = activeInstrumentRunService.getInstrument();
    InstrumentInputParameter templateIn = new InstrumentInputParameter();
    templateIn.setInstrument(instrument);

    if(queryService.count(templateIn) > 0) {
      KeyValueDataPanel kv = new KeyValueDataPanel("inputs", new StringResourceModel("DataInputs", this, null));
      for(final InstrumentInputParameter param : queryService.match(templateIn)) {
        // Inject the Spring application context and the user session service
        // into the instrument parameter. NOTE: These are dependencies of
        // InstrumentParameter.getDescription().
        param.setApplicationContext(((SpringWebApplication) getApplication()).getSpringContextLocator().getSpringContext());
        param.setUserSessionService(userSessionService);

        Label label = new Label(KeyValueDataPanel.getRowKeyId(), new Model() {
          public Object getObject() {
            return param.getDescription();
          }
        });
        InstrumentRunValue runValue = activeInstrumentRunService.getInputInstrumentRunValue(param.getName());
        kv.addRow(label, new Label(KeyValueDataPanel.getRowValueId(), new RunValueLabelModel(runValue)));
      }
      add(kv);
    } else {
      add(new EmptyPanel("inputs"));
    }

    InstrumentOutputParameter templateOut = new InstrumentOutputParameter();
    templateOut.setInstrument(instrument);

    if(queryService.count(templateOut) > 0) {
      KeyValueDataPanel kv = new KeyValueDataPanel("outputs", new StringResourceModel("DataOutputs", this, null));
      for(final InstrumentOutputParameter param : queryService.match(templateOut)) {
        // Inject the Spring application context and the user session service
        // into the instrument parameter. NOTE: These are dependencies of
        // InstrumentParameter.getDescription().
        param.setApplicationContext(((SpringWebApplication) getApplication()).getSpringContextLocator().getSpringContext());
        param.setUserSessionService(userSessionService);

        Label label = new Label(KeyValueDataPanel.getRowKeyId(), new Model() {
          public Object getObject() {
            return param.getDescription();
          }
        });
        InstrumentRunValue runValue = activeInstrumentRunService.getOutputInstrumentRunValue(param.getName());
        if(runValue != null) {
          kv.addRow(label, new Label(KeyValueDataPanel.getRowValueId(), new RunValueLabelModel(runValue)));
        }
      }
      add(kv);
    } else {
      add(new EmptyPanel("outputs"));
    }
  }

}
