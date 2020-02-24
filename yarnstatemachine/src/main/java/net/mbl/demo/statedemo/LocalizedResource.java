package net.mbl.demo.statedemo;

import net.mbl.demo.statedemo.event.ResourceEvent;
import net.mbl.demo.statedemo.event.ResourceEventType;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.InvalidStateTransitionException;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocalizedResource implements EventHandler<ResourceEvent> {
    private static final Logger LOG =
            LoggerFactory.getLogger(LocalizedResource.class);
    private static final StateMachineFactory<LocalizedResource, ResourceState,
                ResourceEventType, ResourceEvent> stateMachineFactory =
            new StateMachineFactory<LocalizedResource, ResourceState,
                    ResourceEventType, ResourceEvent>(ResourceState.INIT)

                    // From INIT (ref == 0, awaiting req)
                    .addTransition(ResourceState.INIT, ResourceState.DOWNLOADING,
                            ResourceEventType.REQUEST, new FetchResourceTransition())
                    .addTransition(ResourceState.INIT, ResourceState.LOCALIZED,
                            ResourceEventType.RECOVERED, new RecoveredTransition())

                    // From DOWNLOADING (ref > 0, may be localizing)
                    .addTransition(ResourceState.DOWNLOADING, ResourceState.DOWNLOADING,
                            ResourceEventType.REQUEST, new FetchResourceTransition())
                    .addTransition(ResourceState.DOWNLOADING, ResourceState.LOCALIZED,
                            ResourceEventType.LOCALIZED, new FetchSuccessTransition())
                    .addTransition(ResourceState.DOWNLOADING, ResourceState.DOWNLOADING,
                            ResourceEventType.RELEASE, new ReleaseTransition())
                    .addTransition(ResourceState.DOWNLOADING, ResourceState.FAILED,
                            ResourceEventType.LOCALIZATION_FAILED, new FetchFailedTransition())

                    // From LOCALIZED (ref >= 0, on disk)
                    .addTransition(ResourceState.LOCALIZED, ResourceState.LOCALIZED,
                            ResourceEventType.REQUEST, new LocalizedResourceTransition())
                    .addTransition(ResourceState.LOCALIZED, ResourceState.LOCALIZED,
                            ResourceEventType.RELEASE, new ReleaseTransition())
                    .installTopology();
    final StateMachine<ResourceState, ResourceEventType, ResourceEvent>
            stateMachine;
    final Semaphore sem = new Semaphore(1);
    // resource
    private final Lock readLock;
    private final Lock writeLock;

    public LocalizedResource() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();

        this.stateMachine = stateMachineFactory.make(this);
    }

    public ResourceState getState() {
        this.readLock.lock();
        try {
            return stateMachine.getCurrentState();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void handle(ResourceEvent event) {
        this.writeLock.lock();
        try {
            LOG.debug("Processing of type {}", event.getType());
            ResourceState oldState = this.stateMachine.getCurrentState();
            ResourceState newState = null;
            try {
                newState = this.stateMachine.doTransition(event.getType(), event);
            } catch (InvalidStateTransitionException e) {
                LOG.error("Can't handle this event at current state", e);
            }
            if (newState != null && oldState != newState) {
                LOG.debug("Resource transitioned from {} to {}", oldState, newState);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    private static class FetchResourceTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }

    private static class RecoveredTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }

    private static class FetchSuccessTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }

    private static class ReleaseTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }

    private static class FetchFailedTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }

    private static class LocalizedResourceTransition implements SingleArcTransition<LocalizedResource, ResourceEvent> {
        @Override
        public void transition(LocalizedResource localizedResource, ResourceEvent resourceEvent) {
        }
    }
}
