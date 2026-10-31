package com.ivi.carservice;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class CarServiceManager_Factory implements Factory<CarServiceManager> {
  private final Provider<Context> contextProvider;

  public CarServiceManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CarServiceManager get() {
    return newInstance(contextProvider.get());
  }

  public static CarServiceManager_Factory create(Provider<Context> contextProvider) {
    return new CarServiceManager_Factory(contextProvider);
  }

  public static CarServiceManager newInstance(Context context) {
    return new CarServiceManager(context);
  }
}
