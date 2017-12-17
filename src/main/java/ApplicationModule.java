import com.threewks.thundr.injection.BaseModule;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.DependencyRegistry;
import com.threewks.thundr.platform.DefaultPlatformModule;
import com.threewks.thundr.route.Router;
import controllers.MyController;

public class ApplicationModule extends BaseModule {
    @Override
    public void configure(UpdatableInjectionContext injectionContext) {

        super.configure(injectionContext);

        Router router = injectionContext.get(Router.class);
        router.get("/thundr-demo/", MyController.class, "home");
    }

    @Override
    public void requires(DependencyRegistry dependencyRegistry) {
        super.requires(dependencyRegistry);
        dependencyRegistry.addDependency(DefaultPlatformModule.class);
    }
}
