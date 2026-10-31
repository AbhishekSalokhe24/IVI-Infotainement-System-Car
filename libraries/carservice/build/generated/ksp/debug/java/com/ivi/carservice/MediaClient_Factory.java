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
public final class MediaClient_Factory implements Factory<MediaClient> {
  @Override
  public MediaClient get() {
    return newInstance();
  }

  public static MediaClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MediaClient newInstance() {
    return new MediaClient();
  }

  private static final class InstanceHolder {
    private static final MediaClient_Factory INSTANCE = new MediaClient_Factory();
  }
}
