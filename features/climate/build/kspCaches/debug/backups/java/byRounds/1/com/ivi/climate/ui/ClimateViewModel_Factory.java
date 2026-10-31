package com.ivi.climate.ui;

import com.ivi.climate.domain.repository.ClimateRepository;
import com.ivi.climate.domain.usecase.GetClimateStateUseCase;
import com.ivi.climate.domain.usecase.SetTemperatureUseCase;
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
public final class ClimateViewModel_Factory implements Factory<ClimateViewModel> {
  private final Provider<GetClimateStateUseCase> getClimateStateUseCaseProvider;

  private final Provider<SetTemperatureUseCase> setTemperatureUseCaseProvider;

  private final Provider<ClimateRepository> climateRepositoryProvider;

  public ClimateViewModel_Factory(Provider<GetClimateStateUseCase> getClimateStateUseCaseProvider,
      Provider<SetTemperatureUseCase> setTemperatureUseCaseProvider,
      Provider<ClimateRepository> climateRepositoryProvider) {
    this.getClimateStateUseCaseProvider = getClimateStateUseCaseProvider;
    this.setTemperatureUseCaseProvider = setTemperatureUseCaseProvider;
    this.climateRepositoryProvider = climateRepositoryProvider;
  }

  @Override
  public ClimateViewModel get() {
    return newInstance(getClimateStateUseCaseProvider.get(), setTemperatureUseCaseProvider.get(), climateRepositoryProvider.get());
  }

  public static ClimateViewModel_Factory create(
      Provider<GetClimateStateUseCase> getClimateStateUseCaseProvider,
      Provider<SetTemperatureUseCase> setTemperatureUseCaseProvider,
      Provider<ClimateRepository> climateRepositoryProvider) {
    return new ClimateViewModel_Factory(getClimateStateUseCaseProvider, setTemperatureUseCaseProvider, climateRepositoryProvider);
  }

  public static ClimateViewModel newInstance(GetClimateStateUseCase getClimateStateUseCase,
      SetTemperatureUseCase setTemperatureUseCase, ClimateRepository climateRepository) {
    return new ClimateViewModel(getClimateStateUseCase, setTemperatureUseCase, climateRepository);
  }
}
