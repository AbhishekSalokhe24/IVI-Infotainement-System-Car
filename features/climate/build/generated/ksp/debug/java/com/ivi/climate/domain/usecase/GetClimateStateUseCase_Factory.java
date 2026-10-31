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
public final class GetClimateStateUseCase_Factory implements Factory<GetClimateStateUseCase> {
  private final Provider<ClimateRepository> repositoryProvider;

  public GetClimateStateUseCase_Factory(Provider<ClimateRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetClimateStateUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetClimateStateUseCase_Factory create(
      Provider<ClimateRepository> repositoryProvider) {
    return new GetClimateStateUseCase_Factory(repositoryProvider);
  }

  public static GetClimateStateUseCase newInstance(ClimateRepository repository) {
    return new GetClimateStateUseCase(repository);
  }
}
