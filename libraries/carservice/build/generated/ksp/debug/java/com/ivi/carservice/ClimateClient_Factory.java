package com.ivi.carservice;

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
public final class ClimateClient_Factory implements Factory<ClimateClient> {
  @Override
  public ClimateClient get() {
    return newInstance();
  }

  public static ClimateClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ClimateClient newInstance() {
    return new ClimateClient();
  }

  private static final class InstanceHolder {
    private static final ClimateClient_Factory INSTANCE = new ClimateClient_Factory();
  }
}
