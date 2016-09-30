package com.atlassian.plugins.tutorial.jira;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;


/**
 * example http://localhost:2990/jira/rest/tutorial-plugin/1.0/
 * @author asamsonov
 *
 */
@Path("/")
public class ConfigResource {

    private final PluginSettingsFactory pluginSettingsFactory;
    private final TransactionTemplate transactionTemplate;

    public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory, TransactionTemplate transactionTemplate) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest request) {

        return Response.ok(transactionTemplate.execute(new TransactionCallback<PluginConfigBody>() {
         
            public PluginConfigBody doInTransaction() {

                PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();                
                
                PluginConfigBody config = new PluginConfigBody();
                config.setName(pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".name-text")+"");
                config.setSelectOption(pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".select-option")+"");
                config.setCheckboxOne(pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".checkbox-1")!=null);
                config.setCheckboxTwo(pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".checkbox-2")!=null);
                
                return config;
            }
            
        })).build();
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class PluginConfigBody {
        
        @XmlElement
        private String name;
        
        @XmlElement
        private String selectOption;
        
        @XmlElement
        private boolean checkboxOne;
        
        @XmlElement
        private boolean checkboxTwo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSelectOption() {
            return selectOption;
        }

        public void setSelectOption(String selectOption) {
            this.selectOption = selectOption;
        }

        public boolean isCheckboxOne() {
            return checkboxOne;
        }

        public void setCheckboxOne(boolean checkboxOne) {
            this.checkboxOne = checkboxOne;
        }

        public boolean isCheckboxTwo() {
            return checkboxTwo;
        }

        public void setCheckboxTwo(boolean checkboxTwo) {
            this.checkboxTwo = checkboxTwo;
        }


        
    }

}
