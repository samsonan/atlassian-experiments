package com.atlassian.plugins.tutorial.jira.servlet;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.plugins.tutorial.jira.Constants;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.templaterenderer.TemplateRenderer;

/**
 * Servlet class to support Admin Configuration screen for Admin
 * 
 * @author asamsonov@luxoft.com
 *
 */
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

    private static final long serialVersionUID = 4361736855786716158L;

    private final TemplateRenderer templateRenderer;
    private final PluginSettingsFactory pluginSettingsFactory;

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;    
    
    public AdminServlet(TemplateRenderer templateRenderer, PluginSettingsFactory pluginSettingsFactory, UserManager userManager, LoginUriProvider loginUriProvider) {
        this.templateRenderer = templateRenderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        UserProfile userProfile = userManager.getRemoteUser();
        if (userProfile == null || !userManager.isSystemAdmin(userProfile.getUserKey()))
        {
            redirectToLogin(request, response);
            return;
        }
        
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();

        Map<String, Object> context = new HashMap<>();

        context.put("name-text", pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".name-text"));
        context.put("select-option", pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".select-option"));
        context.put("checkbox-1", pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".checkbox-1"));
        context.put("checkbox-2", pluginSettings.get(Constants.PLUGIN_STORAGE_KEY + ".checkbox-2"));

        log.debug("context[checkbox-2]:" + context.get("checkbox-2") + "; context[checkbox-1]:" + context.get("checkbox-1"));

        response.setContentType("text/html;charset=utf-8");

        templateRenderer.render("admin.vm", context, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doPost: chb1:" + req.getParameter("checkBoxOne") + "; chb2:" + req.getParameter("checkBoxTwo"));
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        pluginSettings.put(Constants.PLUGIN_STORAGE_KEY + ".name-text", req.getParameter("name-text"));
        pluginSettings.put(Constants.PLUGIN_STORAGE_KEY + ".select-option", req.getParameter("select-option"));

        pluginSettings.put(Constants.PLUGIN_STORAGE_KEY + ".checkbox-1", req.getParameter("checkbox-1"));
        pluginSettings.put(Constants.PLUGIN_STORAGE_KEY + ".checkbox-2", req.getParameter("checkbox-2"));

        response.sendRedirect("admin"); // back to self
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("login URI: {}", loginUriProvider.getLoginUri(getUri(request)).getPath());
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).getPath());
    }

    private URI getUri(HttpServletRequest request) {
        return URI.create(request.getRequestURL().toString());
    }

}
