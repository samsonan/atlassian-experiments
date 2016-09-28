package com.atlassian.plugins.tutorial.jira;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.templaterenderer.TemplateRenderer;

/**
 * https://developer.atlassian.com/docs/common-coding-tasks/creating-an-admin-configuration-form
 * 
 * @author asamsonov
 *
 */
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);
    
    private static final long serialVersionUID = 4361736855786716158L;
    private static final String PLUGIN_STORAGE_KEY = "com.atlassian.plugins.tutorial.jira.rest-ui-plugin";    
    
    private final TemplateRenderer templateRenderer;
    private final PluginSettingsFactory pluginSettingsFactory;

    public AdminServlet(TemplateRenderer templateRenderer, PluginSettingsFactory pluginSettingsFactory) {
        this.templateRenderer = templateRenderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        //We don't need to check permission here really because we use web item 'condition' to control access rights

        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        Map<String, Object> context = new HashMap<>();
        
        context.put("name-text", pluginSettings.get(PLUGIN_STORAGE_KEY + ".name-text"));
        context.put("select-example", pluginSettings.get(PLUGIN_STORAGE_KEY + ".select-example"));        
        context.put("checkbox-1", pluginSettings.get(PLUGIN_STORAGE_KEY + ".checkbox-1"));        
        context.put("checkbox-2", pluginSettings.get(PLUGIN_STORAGE_KEY + ".checkbox-2"));        
        
        log.debug("context[checkbox-2]:"+context.get("checkbox-2")+"; context[checkbox-1]:"+context.get("checkbox-1"));
        
        response.setContentType("text/html;charset=utf-8");
        
        templateRenderer.render("admin.vm", context, response.getWriter());        
        
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost: chb1:" + req.getParameter("checkBoxOne")+"; chb2:"+req.getParameter("checkBoxTwo"));
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".name-text", req.getParameter("name-text"));
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".select-example", req.getParameter("select-example"));
        
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".checkbox-1", req.getParameter("checkbox-1"));
        pluginSettings.put(PLUGIN_STORAGE_KEY + ".checkbox-2", req.getParameter("checkbox-2"));
        
        response.sendRedirect("admin"); //back to self
    }
    
}
