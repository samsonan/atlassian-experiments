package com.atlassian.plugins.tutorial.jira.customfield;

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
import com.atlassian.jira.security.JiraAuthenticationContext;

/**
 * i.  The field gets its value from assignee field on view
 * ii. The field is not editable
 * 
 * @author asamsonov
 *
 */
public class ReadOnlyAssigneeField extends GenericTextCFType {

    private static final Logger log = LoggerFactory.getLogger(ReadOnlyAssigneeField.class);

    protected ReadOnlyAssigneeField(CustomFieldValuePersister customFieldValuePersister, GenericConfigManager genericConfigManager,
            TextFieldCharacterLengthValidator textFieldCharacterLengthValidator, JiraAuthenticationContext jiraAuthenticationContext,
            CustomFieldManager customFieldManager) {
        super(customFieldValuePersister, genericConfigManager, textFieldCharacterLengthValidator, jiraAuthenticationContext);
    }

    @Override
    public Map<String, Object> getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {

        Map<String, Object> params = super.getVelocityParameters(issue, field, fieldLayoutItem);
        params.put("value", getAssigneeUserDisplayName(issue));
        return params;
    }

    @Override
    public String getValueFromIssue(CustomField field, Issue issue) {
        return getAssigneeUserDisplayName(issue);
    }
    
    private String getAssigneeUserDisplayName(Issue issue){
        String result = null;
        try{
            result = issue.getAssigneeUser().getDisplayName();
        }catch(Exception e){ /* that's OK, e.g. when issue is created*/ }
        
        if (result == null) result = "Undefined";
        
        return result;
    }

}
