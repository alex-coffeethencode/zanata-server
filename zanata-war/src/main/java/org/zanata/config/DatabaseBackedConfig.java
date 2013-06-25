/*
 * Copyright 2010, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.zanata.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.zanata.dao.ApplicationConfigurationDAO;
import org.zanata.model.HApplicationConfiguration;

/**
 * Configuration store implementation that is backed by database tables.
 *
 * @author Carlos Munoz <a href="mailto:camunoz@redhat.com">camunoz@redhat.com</a>
 */
@Name("databaseBackedConfig")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class DatabaseBackedConfig implements ConfigurationStore<String, String>
{

   private static final String[] allConfigKeys = new String[]
   {
         HApplicationConfiguration.KEY_ADMIN_EMAIL,
         HApplicationConfiguration.KEY_DOMAIN,
         HApplicationConfiguration.KEY_EMAIL_FROM_ADDRESS,
         HApplicationConfiguration.KEY_EMAIL_LOG_EVENTS,
         HApplicationConfiguration.KEY_EMAIL_LOG_LEVEL,
         HApplicationConfiguration.KEY_HELP_CONTENT,
         HApplicationConfiguration.KEY_HOME_CONTENT,
         HApplicationConfiguration.KEY_HOST,
         HApplicationConfiguration.KEY_LOG_DESTINATION_EMAIL,
         HApplicationConfiguration.KEY_REGISTER
   };

   @In
   private ApplicationConfigurationDAO applicationConfigurationDAO;

   private Map<String, String> configurationValues;

   /**
    * Resets the store by clearing out all values. This means that values will need to be
    * reloaded as they are requested.
    */
   @Override
   @Create
   public void reset()
   {
      configurationValues = new HashMap<String, String>( allConfigKeys.length );
   }

   @Override
   public String getConfigValue(String key)
   {
      if( !configurationValues.containsKey(key) )
      {
         HApplicationConfiguration configRecord = applicationConfigurationDAO.findByKey(key);
         configurationValues.put(key, configRecord.getValue());
      }
      return configurationValues.get(key);
   }

   @Override
   public boolean containsKey(String key)
   {
      for( String k : allConfigKeys )
      {
         if( k.equals(key) )
         {
            return true;
         }
      }
      return false;
   }

   /**
    * ==================================================================================================================
    * Specific property accessor methods for configuration values
    * ==================================================================================================================
    */
   public String getAdminEmailAddress()
   {
      return getConfigValue(HApplicationConfiguration.KEY_ADMIN_EMAIL);
   }

   public String getDomain()
   {
      return getConfigValue(HApplicationConfiguration.KEY_DOMAIN);
   }

   public String getFromEmailAddress()
   {
      return getConfigValue(HApplicationConfiguration.KEY_EMAIL_FROM_ADDRESS);
   }

   public String getShouldLogEvents()
   {
      return getConfigValue(HApplicationConfiguration.KEY_EMAIL_LOG_EVENTS);
   }

   public String getEmailLogLevel()
   {
      return getConfigValue(HApplicationConfiguration.KEY_EMAIL_LOG_LEVEL);
   }

   public String getHelpContent()
   {
      return getConfigValue(HApplicationConfiguration.KEY_HELP_CONTENT);
   }

   public String getHomeContent()
   {
      return getConfigValue(HApplicationConfiguration.KEY_HOME_CONTENT);
   }

   public String getServerHost()
   {
      return getConfigValue(HApplicationConfiguration.KEY_HOST);
   }

   public String getLogEventsDestinationEmailAddress()
   {
      return getConfigValue(HApplicationConfiguration.KEY_LOG_DESTINATION_EMAIL);
   }

   public String getRegistrationUrl()
   {
      return getConfigValue(HApplicationConfiguration.KEY_REGISTER);
   }
}
