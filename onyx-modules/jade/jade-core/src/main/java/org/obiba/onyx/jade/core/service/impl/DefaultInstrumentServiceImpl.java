package org.obiba.onyx.jade.core.service.impl;

import java.util.List;

import org.obiba.core.service.impl.PersistenceManagerAwareService;
import org.obiba.onyx.jade.core.domain.instrument.Instrument;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentType;
import org.obiba.onyx.jade.core.service.InstrumentService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultInstrumentServiceImpl extends PersistenceManagerAwareService implements InstrumentService {

  public InstrumentType createInstrumentType(String name, String description) {
    InstrumentType type = new InstrumentType(name, description);
    return getPersistenceManager().save(type);
  }
  
  public List<Instrument> getInstruments(InstrumentType instrumentType) {
    Instrument template = new Instrument();
    template.setInstrumentType(instrumentType);
    
    return getPersistenceManager().match(template);
  }

  public void addInstrument(InstrumentType instrumentType, Instrument instrument) {
    if (instrumentType != null && instrument != null) {
      instrumentType.addInstrument(instrument);
      getPersistenceManager().save(instrumentType);
    }
  }


}
