//
//  AppDelegate.swift
//  alkaa
//
//  Created by Igor Escodro on 2023-11-02.
//

import Foundation
import UIKit
import shared

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        UNUserNotificationCenter.current().delegate = self
        return true
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void)
    {
        completionHandler([.banner, .list, .badge, .sound])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler:
                                @escaping () -> Void) {
        
        let delegate = InjectionHelper().notificationActionDelegate
        delegate.userNotificationCenter(response: response) {
            // Call the completion handler after the action handling is finished
            // Otherwise the Coroutine will be cancelled if the app is not running in background
            completionHandler()
        }
    }
}
