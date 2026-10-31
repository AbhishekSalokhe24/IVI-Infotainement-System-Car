package com.ivi.launcher;

import com.ivi.carservice.CarServiceManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
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
public final class IVIApplication_MembersInjector implements MembersInjector<IVIApplication> {
  private final Provider<CarServiceManager> carServiceManagerProvider;

  public IVIApplication_MembersInjector(Provider<CarServiceManager> carServiceManagerProvider) {
    this.carServiceManagerProvider = carServiceManagerProvider;
  }

  public static MembersInjector<IVIApplication> create(
      Provider<CarServiceManager> carServiceManagerProvider) {
    return new IVIApplication_MembersInjector(carServiceManagerProvider);
  }

  @Override
  public void injectMembers(IVIApplication instance) {
    injectCarServiceManager(instance, carServiceManagerProvider.get());
  }

  @InjectedFieldSignature("com.ivi.launcher.IVIApplication.carServiceManager")
  public static void injectCarServiceManager(IVIApplication instance,
      CarServiceManager carServiceManager) {
    instance.carServiceManager = carServiceManager;
  }
}
