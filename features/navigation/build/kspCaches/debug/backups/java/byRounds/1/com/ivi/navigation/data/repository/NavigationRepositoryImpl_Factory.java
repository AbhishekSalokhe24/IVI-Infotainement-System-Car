package com.ivi.navigation.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NavigationRepositoryImpl_Factory implements Factory<NavigationRepositoryImpl> {
  @Override
  public NavigationRepositoryImpl get() {
    return newInstance();
  }

  public static NavigationRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NavigationRepositoryImpl newInstance() {
    return new NavigationRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final NavigationRepositoryImpl_Factory INSTANCE = new NavigationRepositoryImpl_Factory();
  }
}
