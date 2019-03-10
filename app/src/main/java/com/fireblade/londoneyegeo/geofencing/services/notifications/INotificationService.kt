package com.fireblade.londoneyegeo.geofencing.services.notifications

// Interface for sending notifications
interface INotificationService {
  fun sendNotification(notificationDetails: String)
}