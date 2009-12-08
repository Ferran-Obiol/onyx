/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.core.service.impl;

import org.obiba.onyx.core.service.PurgeParticipantDataService;

public class PurgeParticipantDataServiceImpl implements PurgeParticipantDataService {

  private String purgeDataOlderThanInDays;

  public void setPurgeDataOlderThanInDays(String purgeDataOlderThanInDays) {
    this.purgeDataOlderThanInDays = purgeDataOlderThanInDays;
  }

  public String getPurgeDataOlderThanInDays() {
    return purgeDataOlderThanInDays;
  }

}
