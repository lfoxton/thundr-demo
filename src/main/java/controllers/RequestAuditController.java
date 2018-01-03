package controllers;

import com.threewks.thundr.view.json.JsonView;
import service.LogService;

public class RequestAuditController {

    private final LogService logService;

    public RequestAuditController(LogService logService) {
        this.logService = logService;
    }

    public JsonView tailRequests() throws InterruptedException {
        return new JsonView(logService.getRequests());
    }

    public JsonView tailResponses() throws InterruptedException {
        return new JsonView(logService.getResponses());
    }
}
