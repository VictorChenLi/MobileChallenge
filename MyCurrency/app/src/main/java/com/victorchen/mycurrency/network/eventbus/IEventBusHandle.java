package com.victorchen.mycurrency.network.eventbus;

/**
 * Provide a common interface to initialize and get {@link EventBusController} for the class which implemented it
 */
public interface IEventBusHandle {
    /**
     * implement this method to initialize EventBusController
     *
     * @return {@link EventBusController}
     */
    EventBusController getEventBusController();
}
