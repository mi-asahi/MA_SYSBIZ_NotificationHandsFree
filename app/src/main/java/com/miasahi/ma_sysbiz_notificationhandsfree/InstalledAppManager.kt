package com.miasahi.ma_sysbiz_notificationhandsfree

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Telephony
import android.util.Log

data class AppInfo(
    val displayName: String,
    val packageName: String,
    val icon: Drawable,
    val permissions: List<String>,
    var packageInfo: PackageInfo,
)

object InstalledAppManager {
    private const val kTargetPermission = "android.permission.POST_NOTIFICATIONS"
    private val comparator: Comparator<AppInfo> = compareBy {
        !it.permissions.contains(
            kTargetPermission
        )
    }

    //private val defaultFilter: (AppInfo) -> Boolean = { it.permissions.contains(kTargetPermission) }

    var appInfos: List<AppInfo> = listOf()

    fun initialize(context: Context) {
        val pm = context.packageManager
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        }

        val smsPackageName = Telephony.Sms.getDefaultSmsPackage(context)
        appInfos = packages.map {
            val packageName = it.packageName
            val displayName = it.loadLabel(pm).toString()
            val icon = it.loadIcon(pm)
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(
                    it.packageName,
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
                )
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
            }
            val permissions: List<String> = if (packageInfo.requestedPermissions == null) listOf()
            else packageInfo.requestedPermissions.toList()

            AppInfo(
                displayName = displayName,
                packageName = packageName,
                icon = icon,
                permissions = permissions,
                packageInfo = packageInfo,
            )
        }
            .filter {
                val ret = it.packageName != context.packageName && (
                        it.packageName == smsPackageName ||
                                (it.packageInfo.applicationInfo.flags and (ApplicationInfo.FLAG_SYSTEM)) == 0)
                if(!ret){
                    Log.d(TAG,"filtered: ${it.displayName}\t ${it.packageName}")
                }
                ret
            }
            .sortedBy { it.displayName }
            .sortedWith(comparator)

        //.filter(defaultFilter)
        Log.d(TAG, "----------")
        appInfos.forEach {
            Log.d(TAG, "### $it")
        }
        Log.d(TAG, "----------")

    }

    private const val TAG = "InstalledAppManager"
}