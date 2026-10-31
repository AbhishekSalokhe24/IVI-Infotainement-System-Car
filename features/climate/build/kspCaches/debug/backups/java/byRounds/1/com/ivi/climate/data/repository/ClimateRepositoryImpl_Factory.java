package com.ivi.climate.data.repository;

import com.ivi.carservice.ClimateClient;
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
public final class ClimateRepositoryImpl_Factory implements Factory<ClimateRepositoryImpl> {
  private final Provider<ClimateClient> climateClientProvider;

  public ClimateRepositoryImpl_Factory(Provider<ClimateClient> climateClientProvider) {
    this.climateClientProvider = climateClientProvider;
  }

  @Override
  public ClimateRepositoryImpl get() {
    return newInstance(climateClientProvider.get());
  }

  public static ClimateRepositoryImpl_Factory create(
      Provider<ClimateClient> climateClientProvider) {
    return new ClimateRepositoryImpl_Factory(climateClientProvider);
  }

  public static ClimateRepositoryImpl newInstance(ClimateClient climateClient) {
    return new ClimateRepositoryImpl(climateClient);
  }
}
