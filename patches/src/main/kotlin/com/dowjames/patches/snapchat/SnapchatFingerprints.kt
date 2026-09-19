package com.dowjames.patches.snapchat

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

/**
 * Ads internal HTTP client request method:
 * LOBi;->b(LWBi;I)Lio/reactivex/rxjava3/internal/operators/single/SingleSubscribeOn;
 */
object SnapAdsHttpClientFingerprint : Fingerprint(
    definingClass = "LOBi;",
    name = "b",
    returnType = "Lio/reactivex/rxjava3/internal/operators/single/SingleSubscribeOn;",
    filters = listOf(
        string("AdsInternalHttpClient")
    )
)

/**
 * Ad insertion plan builder:
 * LY16;->a(LOo;Lio/reactivex/rxjava3/core/Single;Lio/reactivex/rxjava3/core/Observable;LOk;)LLxe;
 */
object SnapAdInsertionPlanFingerprint : Fingerprint(
    definingClass = "LY16;",
    name = "a",
    returnType = "LLxe;",
    filters = listOf(
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "LJBi;",
        )
    )
)

/**
 * Ad cache pool query:
 * Lxi;->a(ILjava/util/ArrayList;)LBi;
 */
object SnapAdCacheQueryFingerprint : Fingerprint(
    definingClass = "Lxi;",
    name = "a",
    returnType = "LBi;",
    filters = listOf(
        string("AdCacheEntriesPool")
    )
)
