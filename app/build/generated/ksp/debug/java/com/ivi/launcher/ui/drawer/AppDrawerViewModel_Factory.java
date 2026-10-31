package com.ivi.launcher.ui.drawer;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppDrawerViewModel_Factory implements Factory<AppDrawerViewModel> {
  private final Provider<Context> contextProvider;

  public AppDrawerViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AppDrawerViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static AppDrawerViewModel_Factory create(Provider<Context> contextProvider) {
    return new AppDrawerViewModel_Factory(contextProvider);
  }

  public static AppDrawerViewModel newInstance(Context context) {
    return new AppDrawerViewModel(context);
  }
}
