package me.hollow.trollgod.client.managers;

import me.hollow.trollgod.client.notification.Notification;

import java.util.ArrayList;

public class NotificationManager {
    private final ArrayList<Notification> notifications = new ArrayList <> ();

    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).drawNotification(posY);
            posY -= 22;
        }
    }

    public void addNotification(String text) {
        this.getNotifications().add(new Notification(text));
    }

    public ArrayList<Notification> getNotifications() {
        return this.notifications;
    }
}

