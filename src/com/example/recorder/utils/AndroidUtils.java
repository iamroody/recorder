package com.example.recorder.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * User: gongming
 * Date: 2/22/14
 * Time: 9:51 上午
 * Email:gongmingqm10@foxmail.com
 */
public class AndroidUtils {

    public final static boolean isAppInstalled(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(packageName);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfoList.size() > 0;
    }

}
