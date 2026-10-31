package com.ivi.phone.ui.dialer;

import com.ivi.phone.domain.repository.PhoneRepository;
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
public final class DialerViewModel_Factory implements Factory<DialerViewModel> {
  private final Provider<PhoneRepository> repositoryProvider;

  public DialerViewModel_Factory(Provider<PhoneRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DialerViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DialerViewModel_Factory create(Provider<PhoneRepository> repositoryProvider) {
    return new DialerViewModel_Factory(repositoryProvider);
  }

  public static DialerViewModel newInstance(PhoneRepository repository) {
    return new DialerViewModel(repository);
  }
}
