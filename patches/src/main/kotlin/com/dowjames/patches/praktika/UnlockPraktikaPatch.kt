package com.dowjames.patches.praktika

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.dowjames.patches.praktika.PraktikaConstants.COMPATIBILITY_PRAKTIKA

// Entitlement identifiers the Dart layer may look up as "premium".
private val PREMIUM_ENTITLEMENT_IDS = listOf(
    "PREMIUM",
    "LIFETIME",
    "premium",
    "lifetime",
    "premiumPlus",
    "Premium Plus",
)

private const val PUT =
    "    invoke-interface {v1, v2, v4}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;\n"

// Builds a raw serialized EntitlementInfo map (v1) for [id] and stores it in the
// active map (v3) under key [id]. Uses only scratch registers v1, v2, v4.
private fun entitlementEntry(id: String) = """
    new-instance v1, Ljava/util/LinkedHashMap;
    invoke-direct {v1}, Ljava/util/LinkedHashMap;-><init>()V
    const-string v2, "identifier"
    const-string v4, "$id"
$PUT    const-string v2, "isActive"
    const/4 v4, 0x1
    invoke-static {v4}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
    move-result-object v4
$PUT    const-string v2, "willRenew"
    const/4 v4, 0x0
    invoke-static {v4}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
    move-result-object v4
$PUT    const-string v2, "latestPurchaseDate"
    const-string v4, "2024-01-01T00:00:00Z"
$PUT    const-string v2, "originalPurchaseDate"
    const-string v4, "2024-01-01T00:00:00Z"
$PUT    const-string v2, "productIdentifier"
    const-string v4, "premium"
$PUT    const-string v2, "isSandbox"
    const/4 v4, 0x0
    invoke-static {v4}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
    move-result-object v4
$PUT    const-string v2, "store"
    const-string v4, "PLAY_STORE"
$PUT    const-string v2, "periodType"
    const-string v4, "NORMAL"
$PUT    const-string v2, "ownershipType"
    const-string v4, "PURCHASED"
$PUT    const-string v2, "verification"
    const-string v4, "VERIFIED"
$PUT    const-string v2, "$id"
    invoke-interface {v3, v2, v1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
"""

@Suppress("unused")
val unlockPraktikaPatch = bytecodePatch(
    name = "Unlock premium",
    description = "Unlocks full premium access in Praktika by injecting active premium entitlements.",
    default = true
) {
    compatibleWith(COMPATIBILITY_PRAKTIKA)

    execute {
        // Inject synthetic active entitlements into the mapper's "active" map (v3),
        // just before it is paired under the "active" key.
        val mapper = EntitlementInfosMapperFingerprint.method
        val instructions = mapper.implementation!!.instructions
        val activeIndex = instructions.indexOfFirst {
            it.opcode == Opcode.CONST_STRING &&
                ((it as ReferenceInstruction).reference as StringReference).string == "active"
        }
        check(activeIndex >= 0) { "Could not locate the \"active\" key in the entitlement mapper" }

        val block = PREMIUM_ENTITLEMENT_IDS.joinToString("") { entitlementEntry(it) }
        mapper.addInstructions(activeIndex, block)

        // Report the top-level entitlement verification as VERIFIED.
        EntitlementInfosGetVerificationFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lcom/revenuecat/purchases/VerificationResult;->VERIFIED:Lcom/revenuecat/purchases/VerificationResult;
                return-object v0
            """
        )
    }
}
