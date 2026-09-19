package com.dowjames.patches.praktika

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object PraktikaConstants {
    val COMPATIBILITY_PRAKTIKA = Compatibility(
        name = "Praktika",
        packageName = "ai.praktika.android",
        apkFileType = ApkFileType.APKM,
        appIconColor = 0x7C4DFF,
        targets = listOf(
            AppTarget(
                version = "4.36.0"
            )
        )
    )
}
