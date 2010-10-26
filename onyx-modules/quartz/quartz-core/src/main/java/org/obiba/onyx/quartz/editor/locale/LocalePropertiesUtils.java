/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.editor.locale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.obiba.onyx.quartz.core.engine.questionnaire.IQuestionnaireElement;
import org.obiba.onyx.quartz.core.engine.questionnaire.bundle.QuestionnaireBundle;
import org.obiba.onyx.quartz.core.engine.questionnaire.bundle.QuestionnaireBundleManager;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Questionnaire;
import org.obiba.onyx.quartz.core.engine.questionnaire.util.localization.impl.DefaultPropertyKeyProviderImpl;
import org.obiba.onyx.quartz.core.wicket.model.QuestionnaireStringResourceModelHelper;
import org.obiba.onyx.quartz.editor.locale.LocaleProperties.KeyValue;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ListMultimap;

public class LocalePropertiesUtils {

  // private transient Logger logger = LoggerFactory.getLogger(getClass());

  private QuestionnaireBundleManager questionnaireBundleManager;

  public LocaleProperties load(Questionnaire questionnaire, IQuestionnaireElement... elements) {
    LocaleProperties localeProperties = new LocaleProperties();
    load(localeProperties, questionnaire, elements);
    return localeProperties;
  }

  public void load(LocaleProperties localeProperties, Questionnaire questionnaire, IQuestionnaireElement... elements) {
    localeProperties.setLocales(new ArrayList<Locale>(questionnaire.getLocales()));
    for(IQuestionnaireElement element : elements) {
      QuestionnaireBundle bundle = questionnaireBundleManager.getClearedMessageSourceCacheBundle(questionnaire.getName());
      List<String> listKeys = new DefaultPropertyKeyProviderImpl().getProperties(element);
      for(Locale locale : localeProperties.getLocales()) {
        for(String key : listKeys) {
          if(bundle != null) {
            String value = null;
            try {
              value = QuestionnaireStringResourceModelHelper.getNonRecursiveResolutionMessage(bundle, element, key, new Object[0], locale);
            } catch(Exception e) {
              // label not found
            }
            localeProperties.addElementLabels(element, locale, key, value);
          }
        }
      }
    }
  }

  private Map<Locale, Properties> toLocalePropertiesMap(LocaleProperties localeProperties) {
    DefaultPropertyKeyProviderImpl defaultPropertyKeyProviderImpl = new DefaultPropertyKeyProviderImpl();
    Map<Locale, Properties> mapLocaleProperties = new HashMap<Locale, Properties>();
    for(Locale locale : localeProperties.getLocales()) {
      Properties properties = new Properties();
      for(Entry<IQuestionnaireElement, ListMultimap<Locale, KeyValue>> entry : localeProperties.getElementLabels().entrySet()) {
        IQuestionnaireElement element = entry.getKey();
        List<KeyValue> keyValueList = entry.getValue().get(locale);
        for(KeyValue keyValue : keyValueList) {
          String fullKey = defaultPropertyKeyProviderImpl.getPropertyKey(element, keyValue.getKey());
          String value = keyValue.getValue();
          properties.setProperty(fullKey, value != null ? value : "");
        }
      }
      mapLocaleProperties.put(locale, properties);
    }
    return mapLocaleProperties;
  }

  public void persist(final QuestionnaireBundle bundle, LocaleProperties localeProperties) {

    List<Locale> questionnaireLocales = bundle.getQuestionnaire().getLocales();
    @SuppressWarnings("unchecked")
    Collection<Locale> deletedLocales = CollectionUtils.subtract(bundle.getAvailableLanguages(), questionnaireLocales);
    for(Locale localeToDelete : deletedLocales) {
      bundle.deleteLanguage(localeToDelete);
    }
    Map<Locale, Properties> localePropertiesMap = toLocalePropertiesMap(localeProperties);
    if(localePropertiesMap.entrySet().isEmpty()) {
      for(Locale locale : questionnaireLocales) {
        bundle.updateLanguage(locale, new Properties());
      }
    } else {
      for(Entry<Locale, Properties> entry : localePropertiesMap.entrySet()) {
        bundle.updateLanguage(entry.getKey(), entry.getValue());
      }
    }
  }

  @Required
  public void setQuestionnaireBundleManager(QuestionnaireBundleManager questionnaireBundleManager) {
    this.questionnaireBundleManager = questionnaireBundleManager;
  }
}