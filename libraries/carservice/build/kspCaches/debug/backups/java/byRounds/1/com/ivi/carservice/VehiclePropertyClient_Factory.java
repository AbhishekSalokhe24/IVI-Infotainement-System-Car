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
public final class VehiclePropertyClient_Factory implements Factory<VehiclePropertyClient> {
  @Override
  public VehiclePropertyClient get() {
    return newInstance();
  }

  public static VehiclePropertyClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static VehiclePropertyClient newInstance() {
    return new VehiclePropertyClient();
  }

  private static final class InstanceHolder {
    private static final VehiclePropertyClient_Factory INSTANCE = new VehiclePropertyClient_Factory();
  }
}
