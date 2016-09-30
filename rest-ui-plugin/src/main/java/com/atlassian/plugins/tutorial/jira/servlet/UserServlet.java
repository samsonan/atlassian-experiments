package com.atlassian.plugins.tutorial.jira.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.templaterenderer.TemplateRenderer;

/**
 * Servlet class to support Common User Configuration View screen
 * 
 * @author asamsonov@luxoft.com
 *
 */
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = -5382786051192011908L;
    
    static final String PLUGIN_STORAGE_KEY = "com.atlassian.plugins.tutorial.jira.rest-ui-plugin";    
    
    private final TemplateRenderer templateRenderer;
    private final PluginSettingsFactory pluginSettingsFactory;

    public UserServlet(TemplateRenderer templateRenderer, PluginSettingsFactory pluginSettingsFactory) {
        this.templateRenderer = templateRenderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        Map<String, Object> context = new HashMap<>();
        context.put("name-text", pluginSettings.get(PLUGIN_STORAGE_KEY + ".name-text"));
        context.put("select-option", pluginSettings.get(PLUGIN_STORAGE_KEY + ".select-option"));        
        context.put("checkbox-1", pluginSettings.get(PLUGIN_STORAGE_KEY + ".checkbox-1"));        
        context.put("checkbox-2", pluginSettings.get(PLUGIN_STORAGE_KEY + ".checkbox-2"));        
        
        response.setContentType("text/html;charset=utf-8");
        templateRenderer.render("user-ro.vm", context, response.getWriter());        
        
    }
    
}
