package net.mbl.demo.statedemo.event;

import net.mbl.demo.statedemo.LocalizedResource;

/**
 * Events delivered to {@link LocalizedResource}. Each of these
 * events is a subclass of {@link ResourceEvent}.
 */
public enum ResourceEventType {
  /** See {@link ResourceRequestEvent} */
  REQUEST,
  /** See {@link ResourceLocalizedEvent} */
  LOCALIZED,
  /** See {@link ResourceReleaseEvent} */
  RELEASE,
  /** See {@link ResourceFailedLocalizationEvent} */
  LOCALIZATION_FAILED,
  /** See {@link ResourceRecoveredEvent} */
  RECOVERED
}
