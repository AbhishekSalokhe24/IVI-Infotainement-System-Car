package com.ivi.launcher.ui.notifications;

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
public final class IVINotificationManager_Factory implements Factory<IVINotificationManager> {
  @Override
  public IVINotificationManager get() {
    return newInstance();
  }

  public static IVINotificationManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static IVINotificationManager newInstance() {
    return new IVINotificationManager();
  }

  private static final class InstanceHolder {
    private static final IVINotificationManager_Factory INSTANCE = new IVINotificationManager_Factory();
  }
}
