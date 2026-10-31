package com.ivi.vehicle.data.repository;

import com.ivi.carservice.VehiclePropertyClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class VehicleRepositoryImpl_Factory implements Factory<VehicleRepositoryImpl> {
  private final Provider<VehiclePropertyClient> clientProvider;

  public VehicleRepositoryImpl_Factory(Provider<VehiclePropertyClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public VehicleRepositoryImpl get() {
    return newInstance(clientProvider.get());
  }

  public static VehicleRepositoryImpl_Factory create(
      Provider<VehiclePropertyClient> clientProvider) {
    return new VehicleRepositoryImpl_Factory(clientProvider);
  }

  public static VehicleRepositoryImpl newInstance(VehiclePropertyClient client) {
    return new VehicleRepositoryImpl(client);
  }
}
