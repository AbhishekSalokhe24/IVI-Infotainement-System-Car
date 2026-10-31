package com.ivi.phone.data.repository;

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
public final class PhoneRepositoryImpl_Factory implements Factory<PhoneRepositoryImpl> {
  @Override
  public PhoneRepositoryImpl get() {
    return newInstance();
  }

  public static PhoneRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PhoneRepositoryImpl newInstance() {
    return new PhoneRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final PhoneRepositoryImpl_Factory INSTANCE = new PhoneRepositoryImpl_Factory();
  }
}
