package com.dowjames.patches.paprika

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.dowjames.patches.paprika.PaprikaConstants.PACKAGE_NAME
import com.dowjames.patches.paprika.PaprikaConstants.SUBSCRIPTION_PRODUCT_ID
import com.dowjames.patches.paprika.PaprikaConstants.COMPATIBILITY_PAPRIKA

private const val SYNTHETIC_PURCHASE_JSON =
    "{\\\"orderId\\\":\\\"GPA::patch-premium-0001\\\"," +
    "\\\"packageName\\\":\\\"$PACKAGE_NAME\\\"," +
    "\\\"productId\\\":\\\"$SUBSCRIPTION_PRODUCT_ID\\\"," +
    "\\\"purchaseTime\\\":1794357167666," +
    "\\\"purchaseState\\\":0," +
    "\\\"purchaseToken\\\":\\\"patch.premium.token.v4\\\"," +
    "\\\"quantity\\\":1," +
    "\\\"acknowledged\\\":true," +
    "\\\"autoRenewing\\\":true}"

/**
 * Appends a synthetic owned subscription purchase to the billing callback
 * unless the real response already contains the subscription product.
 * Runs before the results cross into the managed (AOT) PurchaseManager.
 */
private fun purchaseInjectionSmali() = """
    if-eqz p2, :cond_native

    invoke-interface {p2}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v0

    :cond_loop
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v1

    if-eqz v1, :cond_inject

    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/android/billingclient/api/Purchase;

    invoke-virtual {v1}, Lcom/android/billingclient/api/Purchase;->getProducts()Ljava/util/List;

    move-result-object v1

    const-string v2, "$SUBSCRIPTION_PRODUCT_ID"

    invoke-interface {v1, v2}, Ljava/util/List;->contains(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_loop

    goto :cond_native

    :cond_inject
    const-string v0, "$SYNTHETIC_PURCHASE_JSON"

    new-instance v1, Lcom/android/billingclient/api/Purchase;

    const-string v2, "patchsignature"

    invoke-direct {v1, v0, v2}, Lcom/android/billingclient/api/Purchase;-><init>(Ljava/lang/String;Ljava/lang/String;)V

    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    invoke-virtual {v0, p2}, Ljava/util/ArrayList;->addAll(Ljava/util/Collection;)Z

    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    move-object p2, v0

    :cond_native
"""

@Suppress("unused")
val unlockPaprikaPremiumPatch = bytecodePatch(
    name = "Unlock premium",
    description = "Unlocks Paprika Premium by injecting a synthetic owned subscription into Google Play billing callbacks.",
    default = true
) {
    compatibleWith(COMPATIBILITY_PAPRIKA)

    execute {
        // Purchase updates (initial sync, new purchases, restored purchases).
        PurchasesUpdatedListenerFingerprint.method.addInstructions(
            0,
            purchaseInjectionSmali()
        )

        // queryPurchasesAsync() responses (subscription status checks).
        PurchasesResponseListenerFingerprint.method.addInstructions(
            0,
            purchaseInjectionSmali()
        )
    }
}
