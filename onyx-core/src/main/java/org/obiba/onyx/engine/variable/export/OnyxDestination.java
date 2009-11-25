/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.engine.variable.export;

import java.util.List;

import org.obiba.magma.engine.output.Strategies;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 */
public class OnyxDestination {

  private String name;

  private Strategies strategies;

  @XStreamImplicit
  private List<ValueSetFilter> valueSetFilters;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Strategies getStrategies() {
    return strategies;
  }

  void setStrategies(Strategies strategies) {
    this.strategies = strategies;
  }

  public List<ValueSetFilter> getValueSetFilters() {
    return valueSetFilters;
  }

  public void setValueSetFilters(List<ValueSetFilter> valueSetFilters) {
    this.valueSetFilters = valueSetFilters;
  }

}