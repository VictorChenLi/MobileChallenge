package com.victorchen.mycurrency.network.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Providing all functionality relative to EventBus, used as composite pattern
 * Has to implement {@link #getSubscriber()} to specify the subscriber before use it
 */
public abstract class EventBusController {
    private boolean mSubscribeEventBus = false;
    private EventBus mEventBus = EventBus.getDefault();

    private Integer mEventReceiverId;

    /**
     * The subclass must implement this method to return a instance for subscribe
     *
     * @return the object used in {@link #subscribeEventBus(boolean)} to subscribe on EventBus
     */
    public abstract Object getSubscriber();

    /**
     * For every new subscription, new Id is generated and stored in the object
     * This Id can be used to distinguish sender and receiver for EventBus
     * when only the sender need to get the event
     *
     * @param subscribe true means register this instance, false means unregister
     */
    public void subscribeEventBus(boolean subscribe) {
        // avoid register twice or unregister twice
        if (!mSubscribeEventBus ^ subscribe)
            return;

        mSubscribeEventBus = subscribe;
        if (subscribe) {
            mEventBus.register(getSubscriber());
            mEventReceiverId = IdGenerator.getInstance().nextId();
        } else {
            if (mEventBus.isRegistered(getSubscriber()))
                mEventBus.unregister(getSubscriber());
        }
    }

    /**
     * Get the Event Receiver Id generated by subscribing, this id is specifically for subscriber
     *
     * @return This Id can be used to distinguish sender and receiver for EventBus
     * when only the sender need to get the event
     */
    public Integer getEventReceiverId() {
        return mEventReceiverId;
    }

    /**
     * Check if subscriber is register or not
     *
     * @return true means subscriber already register on EventBus
     */
    public boolean isSubscringEventBus() {
        return mSubscribeEventBus;
    }

    /**
     * Post event to EventBus
     *
     * @param event the event that going to be post to EventBus,
     *              onEventMainThread or onEvent method is required in order to response the Event broadcast
     */
    public void postEvent(Object event) {
        mEventBus.post(event);
    }
}
