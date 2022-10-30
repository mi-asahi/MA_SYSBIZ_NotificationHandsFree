package com.miasahi.ma_sysbiz_notificationhandsfree

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log

data class AppInfo(
    val displayName: String,
    val packageName: String,
    val icon: Drawable,
    val permissions: List<String>,
)

object InstalledAppManager {
    private const val kTargetPermission = "android.permission.POST_NOTIFICATIONS"
    private val comparator: Comparator<AppInfo> = compareBy {
        !it.permissions.contains(
            kTargetPermission
        )
    }
    private val defaultFilter: (AppInfo) -> Boolean = { it.permissions.contains(kTargetPermission) }
    var appInfos: List<AppInfo> = listOf()


    @Suppress("DEPRECATION")
    fun initialize(context: Context) {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        appInfos = packages.map {
            val packageName = it.packageName
            val displayName = it.loadLabel(pm).toString()
            val icon = it.loadIcon(pm)
            val packageInfo = pm.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
            val permissions: List<String> = if (packageInfo.requestedPermissions == null) listOf()
            else packageInfo.requestedPermissions.toList()
            AppInfo(
                displayName = displayName,
                packageName = packageName,
                icon = icon,
                permissions = permissions
            )
        }
            .sortedWith(comparator)
            .filter(defaultFilter)
        Log.d(TAG, "----------")
        appInfos.forEach {
            Log.d(TAG, "$it")
        }
        Log.d(TAG, "----------")

    }

    private const val TAG = "InstalledAppManager"
}