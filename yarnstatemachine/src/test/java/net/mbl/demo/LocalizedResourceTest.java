package net.mbl.demo;

import net.mbl.demo.statedemo.LocalizedResource;
import net.mbl.demo.statedemo.ResourceState;
import net.mbl.demo.statedemo.event.ResourceFailedLocalizationEvent;
import net.mbl.demo.statedemo.event.ResourceRecoveredEvent;
import net.mbl.demo.statedemo.event.ResourceReleaseEvent;
import net.mbl.demo.statedemo.event.ResourceRequestEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalizedResourceTest {
    private LocalizedResource localizedResource;

    @Before
    public void setUp() {
        localizedResource = new LocalizedResource();
    }

    @Test
    public void testTransition() {
        Assert.assertEquals(localizedResource.getState(), ResourceState.INIT);

        localizedResource.handle(new ResourceRequestEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.DOWNLOADING);

        localizedResource.handle(new ResourceRequestEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.DOWNLOADING);

        localizedResource.handle(new ResourceReleaseEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.DOWNLOADING);

        localizedResource.handle(new ResourceFailedLocalizationEvent("test"));
        Assert.assertEquals(localizedResource.getState(), ResourceState.FAILED);
    }

    @Test
    public void testTransition2() {
        Assert.assertEquals(localizedResource.getState(), ResourceState.INIT);

        localizedResource.handle(new ResourceRecoveredEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.LOCALIZED);

        localizedResource.handle(new ResourceReleaseEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.LOCALIZED);

        localizedResource.handle(new ResourceRequestEvent());
        Assert.assertEquals(localizedResource.getState(), ResourceState.LOCALIZED);
    }
}
