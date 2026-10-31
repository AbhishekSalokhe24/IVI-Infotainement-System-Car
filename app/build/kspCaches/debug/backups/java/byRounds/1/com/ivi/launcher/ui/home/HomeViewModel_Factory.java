package com.ivi.launcher.ui.home;

import com.ivi.carservice.ClimateClient;
import com.ivi.carservice.MediaClient;
import com.ivi.carservice.VehiclePropertyClient;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<VehiclePropertyClient> vehiclePropertyClientProvider;

  private final Provider<MediaClient> mediaClientProvider;

  private final Provider<ClimateClient> climateClientProvider;

  public HomeViewModel_Factory(Provider<VehiclePropertyClient> vehiclePropertyClientProvider,
      Provider<MediaClient> mediaClientProvider, Provider<ClimateClient> climateClientProvider) {
    this.vehiclePropertyClientProvider = vehiclePropertyClientProvider;
    this.mediaClientProvider = mediaClientProvider;
    this.climateClientProvider = climateClientProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(vehiclePropertyClientProvider.get(), mediaClientProvider.get(), climateClientProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<VehiclePropertyClient> vehiclePropertyClientProvider,
      Provider<MediaClient> mediaClientProvider, Provider<ClimateClient> climateClientProvider) {
    return new HomeViewModel_Factory(vehiclePropertyClientProvider, mediaClientProvider, climateClientProvider);
  }

  public static HomeViewModel newInstance(VehiclePropertyClient vehiclePropertyClient,
      MediaClient mediaClient, ClimateClient climateClient) {
    return new HomeViewModel(vehiclePropertyClient, mediaClient, climateClient);
  }
}
