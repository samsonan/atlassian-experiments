package com.atlassian.plugins.tutorial.jira.workflow.jira.workflow;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the post-function class that gets executed at the end of the
 * transition. Any parameters that were saved in your factory class will be
 * available in the transientVars Map.
 */
public class LuxoftFeaturePostFunction extends AbstractJiraFunctionProvider {
    private static final Logger log = LoggerFactory.getLogger(LuxoftFeaturePostFunction.class);

    private final CustomFieldManager customFieldManager;
    private final WorkflowManager workflowManager;
    private final CommentManager commentManager;

    public LuxoftFeaturePostFunction(WorkflowManager workflowManager, CustomFieldManager customFieldManager,
            CommentManager commentManager) {
        this.customFieldManager = customFieldManager;
        this.workflowManager = workflowManager;
        this.commentManager = commentManager;
    }

    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        MutableIssue issue = getIssue(transientVars);

        log.debug("LuxoftFeaturePostFunction. Processing issue {} of type {}; ",
                new Object[] { issue.getKey(), issue.getIssueType().getName() });

        String issueTypeName = args.get(LuxoftFeaturePostFunctionFactory.ISSUE_TYPE_NAME) + "";
        String dateFieldName = args.get(LuxoftFeaturePostFunctionFactory.DATE_FIELD_NAME) + "";

        log.debug("LuxoftFeaturePostFunction. Post-function args: Issue type: {}; Date field Name: {}; ",
                new Object[] { issueTypeName, dateFieldName });

        if (issueTypeName.equals(issue.getIssueType().getName())) {

            log.debug("LuxoftFeaturePostFunction. issue {} state {}",
                    new Object[] { issue.getKey(), issue.getStatus().getName() });

            String currentStatus = issue.getStatus().getName();

            try {

                ActionDescriptor action = workflowManager.getActionDescriptor(issue,
                        Integer.parseInt(transientVars.get("actionId").toString()));

                List<CustomField> customFields = customFieldManager.getCustomFieldObjects(issue);

                for (CustomField customField : customFields) {

                    if (dateFieldName.equals(customField.getName())) {

                        log.debug("setting {} field  value to current date", customField.getName());

                        issue.setCustomFieldValue(customField, new Timestamp(new Date().getTime()));
                    }
                }

                String commentString = new StringBuilder().append("Setting field ").append(dateFieldName)
                        .append(" to current date in post-function while moving from ").append(currentStatus)
                        .append(" in transition ").append(action.getName()).toString();

                ApplicationUser user = issue.getAssignee();
                commentManager.create(issue, user, commentString, false);

            } catch (Exception ex) {
                throw new WorkflowException("Cannot set custom date fields in post function", ex);
            }

        }
    }

}