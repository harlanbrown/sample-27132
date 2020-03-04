package org.nuxeo.sample;

import static org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager.CURRENT_DOCUMENT_SELECTION;

import java.io.Serializable;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

@Name("myFancyBox")
@Scope(ScopeType.CONVERSATION)
public class MyFancyBoxBean implements Serializable {

    private static final Log log = LogFactory.getLog(MyFancyBoxBean.class);

    private static final long serialVersionUID = 1L;

    private static final String OPERATION_NAME = "MyChain";
    private static final String MAIL_OPERATION_NAME = "MyChain2";

    protected String assignee;

    protected String addressee = "devnull@nuxeo.com";
    protected String note;
    protected String description;
    protected String source;

    protected List<DocumentModel> selectedDocuments;
    protected List<String> cclist;

    @In(create = true, required = false)
    CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;

    public void assign() {
        try {
            AutomationService as = Framework.getLocalService(AutomationService.class);
            OperationContext ctx = new OperationContext(documentManager);
            Map<String, Object> params = new HashMap<String, Object>();

            // the selection from the fancy box can be placed in a context variable
            ctx.put("assignee", assignee);
            
            // or the parameter passed to the chain which does the assignment
            params.put("assignee", assignee);

            Object result = as.run(ctx, OPERATION_NAME, params);
        } catch (InvalidChainException e) {
            log.error("Unable to resolve reference", e);
        } catch (OperationException e) {
            log.error("Unable to resolve reference", e);
        }
    } 

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void send() {
        try {
            AutomationService as = Framework.getLocalService(AutomationService.class);
            OperationContext ctx = new OperationContext(documentManager);
            Map<String, Object> params = new HashMap<String, Object>();

            ctx.setInput(navigationContext.getCurrentDocument());

            // the selection from the fancy box can be placed in a context variable
            ctx.put("addressee", addressee);
            ctx.put("selectedDocuments",selectedDocuments);
            ctx.put("note",note);

            Object result = as.run(ctx, MAIL_OPERATION_NAME, params);
        } catch (InvalidChainException e) {
            log.error("Unable to resolve reference", e);
        } catch (OperationException e) {
            log.error("Unable to resolve reference", e);
        }
    } 

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        description = currentDocument.getPropertyValue("dc:description").toString();
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        source = currentDocument.getPropertyValue("dc:source").toString();
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DocumentModel> getSelectedDocuments() {
        selectedDocuments = documentsListsManager.getWorkingList(CURRENT_DOCUMENT_SELECTION);
        return selectedDocuments;
    }

    public void setSelectedDocuments(List<DocumentModel> selectedDocuments) {
        this.selectedDocuments = selectedDocuments;
    }

    public List<String> getcclist() {
        return cclist;
    }

    public void setcclist(List<String> cclist) {
        this.cclist = cclist;
    }
}    
