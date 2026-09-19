package com.dowjames.patches.snapchat

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object SnapchatConstants {
    val COMPATIBILITY_SNAPCHAT = Compatibility(
        name = "Snapchat",
        packageName = "com.snapchat.android",
        apkFileType = ApkFileType.APKM,
        appIconColor = 0xCDCA00,
        targets = listOf(
            AppTarget(
                version = "14.24.0.46"
            )
        )
    )
}
