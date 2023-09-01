//
//  alkaaApp.swift
//  alkaa
//
//  Created by Igor Escodro on 2023-08-18.
//

import SwiftUI
import shared

@main
struct alkaaApp: App {
    
    init() {
        KoinHelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
