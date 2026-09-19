package com.dowjames.patches.snapchat

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.dowjames.patches.snapchat.SnapchatConstants.COMPATIBILITY_SNAPCHAT

@Suppress("unused")
val removeSnapchatAdsPatch = bytecodePatch(
    name = "Remove ads",
    description = "Disables Snapchat ad requests and ad insertion.",
    default = true
) {
    compatibleWith(COMPATIBILITY_SNAPCHAT)

    execute {
        SnapAdsHttpClientFingerprint.method.apply {
            removeInstructions(0, implementation!!.instructions.size)
            addInstructions(
                0,
                """
                    new-instance v0, Ljava/lang/Exception;
                    const-string v1, "Ads disabled by Morphe"
                    invoke-direct {v0, v1}, Ljava/lang/Exception;-><init>(Ljava/lang/String;)V

                    invoke-static {v0}, Lio/reactivex/rxjava3/core/Single;->j(Ljava/lang/Throwable;)Lio/reactivex/rxjava3/internal/operators/single/SingleError;
                    move-result-object v1

                    sget-object v2, Lio/reactivex/rxjava3/schedulers/Schedulers;->b:Lio/reactivex/rxjava3/core/Scheduler;

                    new-instance v3, Lio/reactivex/rxjava3/internal/operators/single/SingleSubscribeOn;
                    invoke-direct {v3, v1, v2}, Lio/reactivex/rxjava3/internal/operators/single/SingleSubscribeOn;-><init>(Lio/reactivex/rxjava3/core/SingleSource;Lio/reactivex/rxjava3/core/Scheduler;)V
                    return-object v3
                """
            )
        }

        SnapAdInsertionPlanFingerprint.method.apply {
            removeInstructions(0, implementation!!.instructions.size)
            addInstructions(
                0,
                """
                    const/4 v0, 0x0
                    new-array v1, v0, [LgCe;
                    new-instance v0, LLxe;
                    invoke-direct {v0, v1}, LLxe;-><init>([LgCe;)V
                    return-object v0
                """
            )
        }

        SnapAdCacheQueryFingerprint.method.apply {
            removeInstructions(0, implementation!!.instructions.size)
            addInstructions(
                0,
                """
                    new-instance v1, Ljava/util/ArrayList;
                    invoke-direct {v1}, Ljava/util/ArrayList;-><init>()V

                    const/4 v2, 0x0
                    const/4 v3, 0x0
                    const/4 v4, 0x0
                    const/4 v5, 0x0

                    new-instance v0, LBi;
                    invoke-direct/range {v0 .. v5}, LBi;-><init>(Ljava/util/List;IIILjava/lang/Long;)V
                    return-object v0
                """
            )
        }
    }
}
