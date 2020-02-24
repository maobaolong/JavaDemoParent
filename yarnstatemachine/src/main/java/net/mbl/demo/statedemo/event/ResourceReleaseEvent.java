package net.mbl.demo.statedemo.event;

public class ResourceReleaseEvent extends ResourceEvent {

    public ResourceReleaseEvent() {
        super(ResourceEventType.RELEASE);
    }
}
