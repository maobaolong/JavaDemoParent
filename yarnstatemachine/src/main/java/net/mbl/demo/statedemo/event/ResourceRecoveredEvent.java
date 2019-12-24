package net.mbl.demo.statedemo.event;

public class ResourceRecoveredEvent extends ResourceEvent {

    public ResourceRecoveredEvent() {
        super(ResourceEventType.RECOVERED);
    }
}
