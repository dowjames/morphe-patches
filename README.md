# 🧩 dowjames Morphe Patches

A collection of [Morphe](https://morphe.software) patches for Android apps.

## ❓ About

This repository hosts multiple patches, each targeting a different app.
New patches are added over time — see the generated patches list below for current support.

### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=dowjames/morphe-patches

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0](https://github.com/dowjames/morphe-patches/releases/tag/v1.0.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;1 patches total
<details open>
<summary>📦 PreMom&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.105.1 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Unlock premium](#unlock-premium) | Unlocks all premium membership features in the app. |  |

</details>

## 🧑‍💻 Dev usage

- Each app's patches live in their own package under `patches/src/main/kotlin/`
  (e.g. `eh.premom.patches` for the Premom app).
- Build your patches using `./gradlew :patches:build` to generate `patches/build/libs/patches-*.mpp`.
  Apply the patch locally using [Morphe Desktop](https://github.com/MorpheApp/morphe-desktop).
- Use [semantic commit](https://kapeli.com/cheat_sheets/Semantic_Commits.docset/Contents/Resources/Documents/index) messages:
  - `feat: Added a new feature`
  - `fix: Some problem now fixed`
  - `chore: Random change you do not want in the user facing changelog`
- **Always use semantic release (release.yml)**. Do not manually upload or create releases by hand.
- Do not manually edit generated files: `patches-list.json`, `patches-bundle.json`, `CHANGELOG.md`.

## 🛠️ Building locally

- Requires a GitHub PAT with `read:packages` (set as `gpr.user`/`gpr.key` in `~/.gradle/gradle.properties`).
- Run `./gradlew :patches:build`
- The built patches `.mpp` file is found in `patches/build/libs/patches-*.mpp`
- Patch the `.mpp` file using [Morphe Desktop](https://github.com/MorpheApp/morphe-desktop) or Morphe Manager.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) and the
[patcher fingerprinting docs](https://github.com/MorpheApp/morphe-patcher/blob/main/docs/2_2_1_fingerprinting.md)
for more information.

<!-- The patches end tag is intentionally placed here so the first release will clean up
     this readme of all developer instructions above. -->
<!-- PATCHES_END -->

## 📜 License

dowjames Morphe Patches are licensed under the [GNU General Public License v3.0](LICENSE).
See [NOTICE](NOTICE) for additional conditions.
