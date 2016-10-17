package com.example.weakbatteryxposed;

import android.content.Intent;
import android.os.BatteryManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by zhushi on 2016/8/19.
 */
public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod(
                "android.content.Intent",//想要操作的类名
                lpparam.classLoader,
                "getIntExtra",//方法名 后面为方法参数类型
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param)
                            throws Throwable {
                        Intent intent = (Intent) param.thisObject;
                        final String action = intent.getAction();
                        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                            if (BatteryManager.EXTRA_LEVEL.equals(param.args[0] + "")) {
                                param.setResult(1);
                            } else if ("status".equals(param.args[0] + "")) {
                                XposedBridge.log("status");
                                param.setResult(BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                            }
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                    }
                }
        );
    }
}
