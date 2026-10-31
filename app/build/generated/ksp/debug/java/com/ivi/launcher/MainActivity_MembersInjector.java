package com.ivi.launcher;

import com.ivi.launcher.ui.notifications.IVINotificationManager;
import com.ivi.launcher.ui.voice.VoiceAssistantManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<IVINotificationManager> notificationManagerProvider;

  private final Provider<VoiceAssistantManager> voiceAssistantManagerProvider;

  public MainActivity_MembersInjector(Provider<IVINotificationManager> notificationManagerProvider,
      Provider<VoiceAssistantManager> voiceAssistantManagerProvider) {
    this.notificationManagerProvider = notificationManagerProvider;
    this.voiceAssistantManagerProvider = voiceAssistantManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<IVINotificationManager> notificationManagerProvider,
      Provider<VoiceAssistantManager> voiceAssistantManagerProvider) {
    return new MainActivity_MembersInjector(notificationManagerProvider, voiceAssistantManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectNotificationManager(instance, notificationManagerProvider.get());
    injectVoiceAssistantManager(instance, voiceAssistantManagerProvider.get());
  }

  @InjectedFieldSignature("com.ivi.launcher.MainActivity.notificationManager")
  public static void injectNotificationManager(MainActivity instance,
      IVINotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }

  @InjectedFieldSignature("com.ivi.launcher.MainActivity.voiceAssistantManager")
  public static void injectVoiceAssistantManager(MainActivity instance,
      VoiceAssistantManager voiceAssistantManager) {
    instance.voiceAssistantManager = voiceAssistantManager;
  }
}
