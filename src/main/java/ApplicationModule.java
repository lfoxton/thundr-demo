import com.threewks.thundr.bind.BinderRegistry;
import com.threewks.thundr.bind.json.GsonBinder;
import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.platform.DefaultPlatformModule;
import com.threewks.thundr.route.Router;
import com.threewks.thundr.route.controller.FilterRegistry;
import controllers.GreetingController;
import filters.LoggingFilter;

public class ApplicationModule extends BaseModule {
    @Override
    public void configure(UpdatableInjectionContext injectionContext) {

        super.configure(injectionContext);

        BinderRegistry registry = injectionContext.get(BinderRegistry.class);
        registry.registerBinder(new GsonBinder());

        Router router = injectionContext.get(Router.class);
        router.get("/thundr-demo/", GreetingController.class, "greeting");
        router.post("/thundr-demo/greeting/", GreetingController.class, "namedGreeting");

        FilterRegistry filters = injectionContext.get(FilterRegistry.class);
        filters.add("/**", new LoggingFilter());
    }

    @Override
    public void requires(DependencyRegistry dependencyRegistry) {
        super.requires(dependencyRegistry);
        dependencyRegistry.addDependency(DefaultPlatformModule.class);
    }
}
