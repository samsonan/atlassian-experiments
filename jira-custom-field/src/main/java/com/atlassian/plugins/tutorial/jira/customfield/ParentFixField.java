package com.atlassian.plugins.tutorial.jira.customfield;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.*;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.TextFieldCharacterLengthValidator;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.google.common.base.Joiner;

/**
 * i. Show value of Fix version field of parent issue ii. Applicable to
 * sub-tasks only
 * 
 * @author asamsonov@luxoft.com
 *
 */
public class ParentFixField extends GenericTextCFType {

    private static final Logger log = LoggerFactory.getLogger(ParentFixField.class);

    protected ParentFixField(CustomFieldValuePersister customFieldValuePersister, GenericConfigManager genericConfigManager,
            TextFieldCharacterLengthValidator textFieldCharacterLengthValidator, JiraAuthenticationContext jiraAuthenticationContext,
            CustomFieldManager customFieldManager) {
        super(customFieldValuePersister, genericConfigManager, textFieldCharacterLengthValidator, jiraAuthenticationContext);
    }

    @Override
    public Map<String, Object> getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {

        Map<String, Object> params = super.getVelocityParameters(issue, field, fieldLayoutItem);

        params.put("value", getParentFixVersionAsString(issue));
        return params;
    }

    @Override
    public String getValueFromIssue(CustomField field, Issue issue) {
        return getParentFixVersionAsString(issue);
    }

    private String getParentFixVersionAsString(Issue issue) {
        Issue parentIssue = issue.getParentObject();

        String result = null;

        if (parentIssue != null) {
            Collection<Version> versions = parentIssue.getFixVersions();
            Joiner joiner = Joiner.on(", ").skipNulls();
            result = joiner.join(versions);
            if (result.isEmpty()) result = "None";
        }
        
        return result;
    }

}
