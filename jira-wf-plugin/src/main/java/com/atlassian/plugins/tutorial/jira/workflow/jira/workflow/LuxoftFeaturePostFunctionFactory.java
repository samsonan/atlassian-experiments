package com.atlassian.plugins.tutorial.jira.workflow.jira.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the factory class responsible for dealing with the UI for the
 * post-function. This is typically where you put default values into the
 * velocity context and where you store user input.
 */

public class LuxoftFeaturePostFunctionFactory extends AbstractWorkflowPluginFactory
        implements WorkflowPluginFunctionFactory {

    /**
     * User input params
     */
    static final String ISSUE_TYPE_NAME = "issueTypeName";
    static final String DATE_FIELD_NAME = "dateFieldName";

    public LuxoftFeaturePostFunctionFactory() {
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put(ISSUE_TYPE_NAME, "Feature");
        velocityParams.put(DATE_FIELD_NAME, "Development Date");
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        }

        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;

        String issueType = (String) functionDescriptor.getArgs().get(ISSUE_TYPE_NAME);
        velocityParams.put(ISSUE_TYPE_NAME, issueType);

        String dateField = (String) functionDescriptor.getArgs().get(DATE_FIELD_NAME);
        velocityParams.put(DATE_FIELD_NAME, dateField);

    }

    @Override
    public Map<String, ?> getDescriptorParams(Map<String, Object> formParams) {
        Map params = new HashMap();

        params.put(ISSUE_TYPE_NAME, extractSingleParam(formParams, ISSUE_TYPE_NAME));
        params.put(DATE_FIELD_NAME, extractSingleParam(formParams, DATE_FIELD_NAME));

        return params;
    }

}