import com.google.api.Logging;
import com.threewks.thundr.gae.GaeModule;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.route.Router;
import com.threewks.thundr.route.controller.FilterRegistry;
import controllers.GreetingController;
import controllers.RequestAuditController;
import filters.LoggingFilter;
import service.LogService;
import service.bigquery.BigQueryService;

public class ApplicationModule extends BaseModule {

    @Override
    public void requires(DependencyRegistry dependencyRegistry) {
        super.requires(dependencyRegistry);
        dependencyRegistry.addDependency(GaeModule.class);
    }

    @Override
    public void initialise(UpdatableInjectionContext injectionContext) {
        super.initialise(injectionContext);
        injectionContext.inject(injectionContext).as(UpdatableInjectionContext.class);
    }

    @Override
    public void configure(UpdatableInjectionContext injectionContext) {
        super.configure(injectionContext);
        injectionContext.inject(BigQueryService.class).as(BigQueryService.class);
        injectionContext.inject(Logging.class).as(Logging.class);
        injectionContext.inject(LogService.class).as(LogService.class);
    }

    @Override
    public void start(UpdatableInjectionContext injectionContext) {
        super.start(injectionContext);

        // Routes
        Router router = injectionContext.get(Router.class);
        router.get("/thundr-demo/", GreetingController.class, "greeting");
        router.post("/thundr-demo/greeting", GreetingController.class, "namedGreeting");
        router.get("/thundr-demo/audit/tailRequests", RequestAuditController.class, "tailRequests");
        router.get("/thundr-demo/audit/tailResponses", RequestAuditController.class, "tailResponses");

        // Filters
        FilterRegistry filters = injectionContext.get(FilterRegistry.class);
        filters.add(new LoggingFilter(injectionContext.get(LogService.class)), "/**");
    }

}
