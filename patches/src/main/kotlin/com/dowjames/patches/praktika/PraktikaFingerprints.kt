package com.dowjames.patches.praktika

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

private const val ENTITLEMENT_INFOS = "Lcom/revenuecat/purchases/EntitlementInfos;"

/**
 * EntitlementInfosMapperKt.map(EntitlementInfos): Map
 *
 * Serializes the native entitlement infos into the {all, active, verification}
 * map handed to the Flutter layer. We inject synthetic active entitlements here.
 */
object EntitlementInfosMapperFingerprint : Fingerprint(
    definingClass = "Lcom/revenuecat/purchases/hybridcommon/mappers/EntitlementInfosMapperKt;",
    name = "map",
    returnType = "Ljava/util/Map;",
    filters = listOf(
        methodCall(
            definingClass = ENTITLEMENT_INFOS,
            name = "getActive",
        )
    )
)

/**
 * EntitlementInfos.getVerification(): VerificationResult
 *
 * Top-level signature verification result reported to Flutter.
 */
object EntitlementInfosGetVerificationFingerprint : Fingerprint(
    definingClass = ENTITLEMENT_INFOS,
    name = "getVerification",
    returnType = "Lcom/revenuecat/purchases/VerificationResult;",
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = ENTITLEMENT_INFOS,
        )
    )
)
