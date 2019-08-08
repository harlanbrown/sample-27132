package org.nuxeo.sample;

import java.io.Serializable;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.InvalidChainException;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;

@Name("myFancyBox")
@Scope(ScopeType.EVENT)
public class MyFancyBoxBean implements Serializable {

    private static final Log log = LogFactory.getLog(MyFancyBoxBean.class);

    private static final long serialVersionUID = 1L;

    private static final String OPERATION_NAME = "MyChain";

    protected List<String> assignees;

    @In(create = true, required = false)
    CoreSession documentManager;

    public void assign() {
        try {
            AutomationService as = Framework.getLocalService(AutomationService.class);
            OperationContext ctx = new OperationContext(documentManager);
            Map<String, Object> params = new HashMap<String, Object>();

            // the selection from the fancy box can be placed in a context variable
            ctx.put("assignees", assignees);
            
            // or the parameter passed to the chain which does the assignment
            params.put("assignees", assignees);

            Object result = as.run(ctx, OPERATION_NAME, params);
        } catch (InvalidChainException e) {
            log.error("Unable to resolve reference", e);
        } catch (OperationException e) {
            log.error("Unable to resolve reference", e);
        }
    } 

    public List<String> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<String> assignees) {
        this.assignees = assignees;
    }

}    