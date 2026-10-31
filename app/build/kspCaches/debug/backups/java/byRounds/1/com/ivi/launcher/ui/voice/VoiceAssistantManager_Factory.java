package com.ivi.launcher.ui.voice;

import com.ivi.carservice.ClimateClient;
import com.ivi.carservice.MediaClient;
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
public final class VoiceAssistantManager_Factory implements Factory<VoiceAssistantManager> {
  private final Provider<ClimateClient> climateClientProvider;

  private final Provider<MediaClient> mediaClientProvider;

  public VoiceAssistantManager_Factory(Provider<ClimateClient> climateClientProvider,
      Provider<MediaClient> mediaClientProvider) {
    this.climateClientProvider = climateClientProvider;
    this.mediaClientProvider = mediaClientProvider;
  }

  @Override
  public VoiceAssistantManager get() {
    return newInstance(climateClientProvider.get(), mediaClientProvider.get());
  }

  public static VoiceAssistantManager_Factory create(Provider<ClimateClient> climateClientProvider,
      Provider<MediaClient> mediaClientProvider) {
    return new VoiceAssistantManager_Factory(climateClientProvider, mediaClientProvider);
  }

  public static VoiceAssistantManager newInstance(ClimateClient climateClient,
      MediaClient mediaClient) {
    return new VoiceAssistantManager(climateClient, mediaClient);
  }
}
