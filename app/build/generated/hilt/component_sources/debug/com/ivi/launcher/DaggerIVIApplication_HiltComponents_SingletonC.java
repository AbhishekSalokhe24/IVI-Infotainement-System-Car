package com.ivi.launcher;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.ivi.carservice.CarServiceManager;
import com.ivi.carservice.ClimateClient;
import com.ivi.carservice.MediaClient;
import com.ivi.carservice.VehiclePropertyClient;
import com.ivi.climate.data.repository.ClimateRepositoryImpl;
import com.ivi.climate.domain.usecase.GetClimateStateUseCase;
import com.ivi.climate.domain.usecase.SetTemperatureUseCase;
import com.ivi.climate.ui.ClimateViewModel;
import com.ivi.climate.ui.ClimateViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.launcher.ui.drawer.AppDrawerViewModel;
import com.ivi.launcher.ui.drawer.AppDrawerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.launcher.ui.home.HomeViewModel;
import com.ivi.launcher.ui.home.HomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.launcher.ui.notifications.IVINotificationManager;
import com.ivi.launcher.ui.shell.StatusBarViewModel;
import com.ivi.launcher.ui.shell.StatusBarViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.launcher.ui.voice.VoiceAssistantManager;
import com.ivi.media.data.repository.MediaRepositoryImpl;
import com.ivi.media.ui.MediaViewModel;
import com.ivi.media.ui.MediaViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.navigation.data.repository.NavigationRepositoryImpl;
import com.ivi.navigation.ui.map.MapViewModel;
import com.ivi.navigation.ui.map.MapViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.phone.data.repository.PhoneRepositoryImpl;
import com.ivi.phone.ui.dialer.DialerViewModel;
import com.ivi.phone.ui.dialer.DialerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.settings.data.repository.SettingsRepositoryImpl;
import com.ivi.settings.ui.SettingsViewModel;
import com.ivi.settings.ui.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.ivi.vehicle.data.repository.VehicleRepositoryImpl;
import com.ivi.vehicle.ui.VehicleViewModel;
import com.ivi.vehicle.ui.VehicleViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DaggerIVIApplication_HiltComponents_SingletonC {
  private DaggerIVIApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public IVIApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements IVIApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements IVIApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements IVIApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements IVIApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements IVIApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements IVIApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements IVIApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public IVIApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends IVIApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends IVIApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends IVIApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends IVIApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(9).add(AppDrawerViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(ClimateViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(DialerViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(HomeViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MapViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MediaViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(StatusBarViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(VehicleViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectNotificationManager(instance, singletonCImpl.iVINotificationManagerProvider.get());
      MainActivity_MembersInjector.injectVoiceAssistantManager(instance, singletonCImpl.voiceAssistantManagerProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends IVIApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AppDrawerViewModel> appDrawerViewModelProvider;

    private Provider<ClimateViewModel> climateViewModelProvider;

    private Provider<DialerViewModel> dialerViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MapViewModel> mapViewModelProvider;

    private Provider<MediaViewModel> mediaViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StatusBarViewModel> statusBarViewModelProvider;

    private Provider<VehicleViewModel> vehicleViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetClimateStateUseCase getClimateStateUseCase() {
      return new GetClimateStateUseCase(singletonCImpl.climateRepositoryImplProvider.get());
    }

    private SetTemperatureUseCase setTemperatureUseCase() {
      return new SetTemperatureUseCase(singletonCImpl.climateRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.appDrawerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.climateViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.dialerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.mapViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.mediaViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.statusBarViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.vehicleViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(9).put("com.ivi.launcher.ui.drawer.AppDrawerViewModel", ((Provider) appDrawerViewModelProvider)).put("com.ivi.climate.ui.ClimateViewModel", ((Provider) climateViewModelProvider)).put("com.ivi.phone.ui.dialer.DialerViewModel", ((Provider) dialerViewModelProvider)).put("com.ivi.launcher.ui.home.HomeViewModel", ((Provider) homeViewModelProvider)).put("com.ivi.navigation.ui.map.MapViewModel", ((Provider) mapViewModelProvider)).put("com.ivi.media.ui.MediaViewModel", ((Provider) mediaViewModelProvider)).put("com.ivi.settings.ui.SettingsViewModel", ((Provider) settingsViewModelProvider)).put("com.ivi.launcher.ui.shell.StatusBarViewModel", ((Provider) statusBarViewModelProvider)).put("com.ivi.vehicle.ui.VehicleViewModel", ((Provider) vehicleViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ivi.launcher.ui.drawer.AppDrawerViewModel 
          return (T) new AppDrawerViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.ivi.climate.ui.ClimateViewModel 
          return (T) new ClimateViewModel(viewModelCImpl.getClimateStateUseCase(), viewModelCImpl.setTemperatureUseCase(), singletonCImpl.climateRepositoryImplProvider.get());

          case 2: // com.ivi.phone.ui.dialer.DialerViewModel 
          return (T) new DialerViewModel(singletonCImpl.phoneRepositoryImplProvider.get());

          case 3: // com.ivi.launcher.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.vehiclePropertyClientProvider.get(), singletonCImpl.mediaClientProvider.get(), singletonCImpl.climateClientProvider.get());

          case 4: // com.ivi.navigation.ui.map.MapViewModel 
          return (T) new MapViewModel(singletonCImpl.navigationRepositoryImplProvider.get());

          case 5: // com.ivi.media.ui.MediaViewModel 
          return (T) new MediaViewModel(singletonCImpl.mediaRepositoryImplProvider.get());

          case 6: // com.ivi.settings.ui.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.settingsRepositoryImplProvider.get());

          case 7: // com.ivi.launcher.ui.shell.StatusBarViewModel 
          return (T) new StatusBarViewModel();

          case 8: // com.ivi.vehicle.ui.VehicleViewModel 
          return (T) new VehicleViewModel(singletonCImpl.vehicleRepositoryImplProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends IVIApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends IVIApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends IVIApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<CarServiceManager> carServiceManagerProvider;

    private Provider<IVINotificationManager> iVINotificationManagerProvider;

    private Provider<ClimateClient> climateClientProvider;

    private Provider<MediaClient> mediaClientProvider;

    private Provider<VoiceAssistantManager> voiceAssistantManagerProvider;

    private Provider<ClimateRepositoryImpl> climateRepositoryImplProvider;

    private Provider<PhoneRepositoryImpl> phoneRepositoryImplProvider;

    private Provider<VehiclePropertyClient> vehiclePropertyClientProvider;

    private Provider<NavigationRepositoryImpl> navigationRepositoryImplProvider;

    private Provider<MediaRepositoryImpl> mediaRepositoryImplProvider;

    private Provider<SettingsRepositoryImpl> settingsRepositoryImplProvider;

    private Provider<VehicleRepositoryImpl> vehicleRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.carServiceManagerProvider = DoubleCheck.provider(new SwitchingProvider<CarServiceManager>(singletonCImpl, 0));
      this.iVINotificationManagerProvider = DoubleCheck.provider(new SwitchingProvider<IVINotificationManager>(singletonCImpl, 1));
      this.climateClientProvider = DoubleCheck.provider(new SwitchingProvider<ClimateClient>(singletonCImpl, 3));
      this.mediaClientProvider = DoubleCheck.provider(new SwitchingProvider<MediaClient>(singletonCImpl, 4));
      this.voiceAssistantManagerProvider = DoubleCheck.provider(new SwitchingProvider<VoiceAssistantManager>(singletonCImpl, 2));
      this.climateRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ClimateRepositoryImpl>(singletonCImpl, 5));
      this.phoneRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<PhoneRepositoryImpl>(singletonCImpl, 6));
      this.vehiclePropertyClientProvider = DoubleCheck.provider(new SwitchingProvider<VehiclePropertyClient>(singletonCImpl, 7));
      this.navigationRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<NavigationRepositoryImpl>(singletonCImpl, 8));
      this.mediaRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<MediaRepositoryImpl>(singletonCImpl, 9));
      this.settingsRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepositoryImpl>(singletonCImpl, 10));
      this.vehicleRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<VehicleRepositoryImpl>(singletonCImpl, 11));
    }

    @Override
    public void injectIVIApplication(IVIApplication arg0) {
      injectIVIApplication2(arg0);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private IVIApplication injectIVIApplication2(IVIApplication instance) {
      IVIApplication_MembersInjector.injectCarServiceManager(instance, carServiceManagerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.ivi.carservice.CarServiceManager 
          return (T) new CarServiceManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.ivi.launcher.ui.notifications.IVINotificationManager 
          return (T) new IVINotificationManager();

          case 2: // com.ivi.launcher.ui.voice.VoiceAssistantManager 
          return (T) new VoiceAssistantManager(singletonCImpl.climateClientProvider.get(), singletonCImpl.mediaClientProvider.get());

          case 3: // com.ivi.carservice.ClimateClient 
          return (T) new ClimateClient();

          case 4: // com.ivi.carservice.MediaClient 
          return (T) new MediaClient();

          case 5: // com.ivi.climate.data.repository.ClimateRepositoryImpl 
          return (T) new ClimateRepositoryImpl(singletonCImpl.climateClientProvider.get());

          case 6: // com.ivi.phone.data.repository.PhoneRepositoryImpl 
          return (T) new PhoneRepositoryImpl();

          case 7: // com.ivi.carservice.VehiclePropertyClient 
          return (T) new VehiclePropertyClient();

          case 8: // com.ivi.navigation.data.repository.NavigationRepositoryImpl 
          return (T) new NavigationRepositoryImpl();

          case 9: // com.ivi.media.data.repository.MediaRepositoryImpl 
          return (T) new MediaRepositoryImpl(singletonCImpl.mediaClientProvider.get());

          case 10: // com.ivi.settings.data.repository.SettingsRepositoryImpl 
          return (T) new SettingsRepositoryImpl();

          case 11: // com.ivi.vehicle.data.repository.VehicleRepositoryImpl 
          return (T) new VehicleRepositoryImpl(singletonCImpl.vehiclePropertyClientProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
