package net.mbl.demo.statedemo.event;

/**
 * This event is sent by the localizer in case resource localization fails for
 * the requested resource.
 */
public class ResourceFailedLocalizationEvent extends ResourceEvent {

    private final String diagnosticMesage;

    public ResourceFailedLocalizationEvent(String diagnosticMesage) {
        super(ResourceEventType.LOCALIZATION_FAILED);
        this.diagnosticMesage = diagnosticMesage;
    }

    public String getDiagnosticMessage() {
        return diagnosticMesage;
    }
}
