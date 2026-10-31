package com.ivi.launcher.ui.shell;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class StatusBarViewModel_Factory implements Factory<StatusBarViewModel> {
  @Override
  public StatusBarViewModel get() {
    return newInstance();
  }

  public static StatusBarViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StatusBarViewModel newInstance() {
    return new StatusBarViewModel();
  }

  private static final class InstanceHolder {
    private static final StatusBarViewModel_Factory INSTANCE = new StatusBarViewModel_Factory();
  }
}
