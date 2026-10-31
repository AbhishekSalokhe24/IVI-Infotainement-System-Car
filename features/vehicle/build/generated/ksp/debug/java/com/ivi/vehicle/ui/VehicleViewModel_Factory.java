package com.ivi.vehicle.ui;

import com.ivi.vehicle.domain.repository.VehicleRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class VehicleViewModel_Factory implements Factory<VehicleViewModel> {
  private final Provider<VehicleRepository> repositoryProvider;

  public VehicleViewModel_Factory(Provider<VehicleRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public VehicleViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static VehicleViewModel_Factory create(Provider<VehicleRepository> repositoryProvider) {
    return new VehicleViewModel_Factory(repositoryProvider);
  }

  public static VehicleViewModel newInstance(VehicleRepository repository) {
    return new VehicleViewModel(repository);
  }
}
