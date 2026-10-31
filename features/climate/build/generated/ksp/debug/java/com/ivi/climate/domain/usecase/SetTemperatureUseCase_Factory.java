package com.ivi.climate.domain.usecase;

import com.ivi.climate.domain.repository.ClimateRepository;
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
public final class SetTemperatureUseCase_Factory implements Factory<SetTemperatureUseCase> {
  private final Provider<ClimateRepository> repositoryProvider;

  public SetTemperatureUseCase_Factory(Provider<ClimateRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SetTemperatureUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SetTemperatureUseCase_Factory create(
      Provider<ClimateRepository> repositoryProvider) {
    return new SetTemperatureUseCase_Factory(repositoryProvider);
  }

  public static SetTemperatureUseCase newInstance(ClimateRepository repository) {
    return new SetTemperatureUseCase(repository);
  }
}
