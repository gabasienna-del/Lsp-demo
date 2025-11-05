package com.example.lspdemo

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.maco.lasthope") return
        XposedBridge.log("✅ [LSPosed] Hooked into ${lpparam.packageName}")

        try {
            XposedHelpers.findAndHookMethod(
                "com.maco.lasthope.MainActivity",
                lpparam.classLoader,
                "onCreate",
                android.os.Bundle::class.java,
                object : de.robv.android.xposed.XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("🔥 onCreate() hook worked")
                    }
                }
            )
        } catch (e: Throwable) {
            XposedBridge.log("❌ Hook error: ${e.message}")
        }
    }
}
