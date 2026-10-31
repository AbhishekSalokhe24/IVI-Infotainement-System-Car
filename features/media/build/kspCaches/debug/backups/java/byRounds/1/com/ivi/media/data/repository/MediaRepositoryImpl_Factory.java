package com.ivi.media.data.repository;

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
public final class MediaRepositoryImpl_Factory implements Factory<MediaRepositoryImpl> {
  private final Provider<MediaClient> clientProvider;

  public MediaRepositoryImpl_Factory(Provider<MediaClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Override
  public MediaRepositoryImpl get() {
    return newInstance(clientProvider.get());
  }

  public static MediaRepositoryImpl_Factory create(Provider<MediaClient> clientProvider) {
    return new MediaRepositoryImpl_Factory(clientProvider);
  }

  public static MediaRepositoryImpl newInstance(MediaClient client) {
    return new MediaRepositoryImpl(client);
  }
}
