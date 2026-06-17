package com.scrns.notification;

@FunctionalInterface
public interface Notifiable {

    void notifyUser(String message);
}