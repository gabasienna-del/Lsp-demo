package com.example.lspdemo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage {
    private static final String TARGET_PKG = "com.maco.lasthope";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(TARGET_PKG)) return;

        XposedBridge.log("[LSP-DEMO] Loaded target: " + lpparam.packageName);

        XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader,
                "onCreate", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Application app = (Application) param.thisObject;
                        Context ctx = app.getApplicationContext();
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(ctx, "LSPosed Demo: hooked " + TARGET_PKG, Toast.LENGTH_SHORT).show()
                        );
                        XposedBridge.log("[LSP-DEMO] onCreate -> Toast shown");
                    }
                });
    }
}
