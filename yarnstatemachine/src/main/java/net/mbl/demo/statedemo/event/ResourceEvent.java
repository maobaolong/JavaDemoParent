package net.mbl.demo.statedemo.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class ResourceEvent extends AbstractEvent<ResourceEventType> {

    public ResourceEvent(ResourceEventType type) {
        super(type);
    }
}
