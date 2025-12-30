![Build](https://github.com/igorescodro/alkaa/actions/workflows/build.yml/badge.svg)
![Tests](https://github.com/igorescodro/alkaa/actions/workflows/tests.yml/badge.svg)
![Release](https://github.com/igorescodro/alkaa/actions/workflows/release.yml/badge.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/igorescodro/alkaa/badge/main)](https://www.codefactor.io/repository/github/igorescodro/alkaa/overview/main)
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>

<img src="desktop-app/src/desktopMain/resources/ic_launcher.png" width="256" alt="Alkaa logo">

# Alkaa - Multiplatform

Alkaa (_begin_, _start_ in Finnish) is a to-do application to study the latest components,
architecture, and tools for multiplatform development. The project is
available on Google Play, App Store, and for Desktop! ❤️

The current version of Alkaa was completely migrated to **Kotlin and Compose Multiplatform**!

## Android
| <img src="https://play-lh.googleusercontent.com/W76J3O4OvtqiBvGePEL2Czt_Jl52SIcm38SK2m7Jzbq83RdzvDStd1Qn5NoAp3ffEibn=w5120-h2880-rw">  | <img src="https://play-lh.googleusercontent.com/CRRgxRGMLXykBlehra9LNOmCedbAhRjAEbU5QFx0hTXpE6m4P6YXoRv78rd5T22-LEA=w5120-h2880-rw"> | <img src="https://play-lh.googleusercontent.com/XYlsapSwCTNFPNZmFRI9-e5Joc9h5ZprpU8X_eA8Gpcwm2E2ZLOWc7kNi028UoZR0N8=w5120-h2880-rw">  | <img src="https://play-lh.googleusercontent.com/XBjXPeuFkG_lhrbH392LD_wACYpPx69aRP4W2h2oxlvmDYwXDK2ZSEQXGl6pyo-jTfaX=w5120-h2880-rw"> |
| ------------- | ------------- | ------------- | ------------- |

## iOS
| <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource126/v4/e6/6e/e4/e66ee445-cbaa-17c3-6f63-fef2b62ca0e0/87c54b1b-49d4-4555-8071-60a39f15dbb0_01.png/460x0w.webp"> | <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource126/v4/a2/82/5f/a2825f13-1da8-aedc-f708-517c2cc2913b/3733dbf1-1844-408f-a947-9250267c608b_02.png/460x0w.png">  | <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource116/v4/cd/cd/b2/cdcdb2fa-8835-0f21-310b-c59e21fbbfbe/d16b3c35-5885-4f00-b7e6-045b09bb3809_03.png/460x0w.webp"> | <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource116/v4/79/6b/3f/796b3f9b-2141-bdd8-0583-c0df856c3b26/dc6cf806-a9cc-4b5d-9643-711e2f88da65_04.png/460x0w.png"> |
| ------------- | ------------- | ------------- | ------------- |

## 📦 Download


<table style="width:100%">
  <tr>
    <td><a href='https://play.google.com/store/apps/details?id=com.escodro.alkaa'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=240/></a>
    <td><a href='https://apps.apple.com/ca/app/alkaa/id6476208968'><img alt='Download in the App Store' src='https://developer.apple.com/assets/elements/badges/download-on-the-app-store.svg' width=180/></a>
  </tr>
</table>


## 📚 Tech stack

One of the main goals of Alkaa is to use all the latest libraries and tools available.

### 🧑🏻‍💻 Multiplatform development

- Application entirely written in [Kotlin](https://kotlinlang.org)
- UI developed in [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- UI Tests with [Compose Multiplatform UI Testing](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html)
- Following the [Material You](https://m3.material.io/) guidelines and dynamic color (Android only)
- Adaptive layouts with [Compose Adaptive](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- Navigation using [Navigation3](https://developer.android.com/jetpack/compose/navigation)
- Asynchronous processing using [Coroutines](https://kotlin.github.io/kotlinx.coroutines/)
- Widgets using [Jetpack Glance](https://developer.android.com/jetpack/androidx/releases/glance)
- Dependency injection with [Koin](https://insert-koin.io)
- Database using [SQLDelight](https://github.com/cashapp/sqldelight)
- Local storage with [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- License management with [AboutLibraries](https://github.com/mikepenz/AboutLibraries)

For more dependencies used in project, please access the
[Dependency File](https://github.com/igorescodro/alkaa/blob/main/gradle/libs.versions.toml)

If you want to check the previous version of Alkaa, please take a look at
the latest [V1](https://github.com/igorescodro/alkaa/tree/v1.7.0) or [V2](https://github.com/igorescodro/alkaa/tree/v2.3.0) releases.

### 🧪 Quality

- [ktlint](https://github.com/pinterest/ktlint)
- [detekt](https://github.com/arturbosch/detekt)
- [compose-rules](https://github.com/twitter/compose-rules)
- [lint](https://developer.android.com/studio/write/lint)
- [CodeFactor](https://www.codefactor.io/)

## 🏛 Architecture

Alkaa architecture is strongly based on the [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
by Alistair Cockburn. The application also relies heavily on modularization for better separation of
concerns and encapsulation.

Let's take a look at each major module of the application:

* **app**, **ios-app**, and **desktop-app** - The platform-specific app modules. They contain the
  initialization logic for each platform.
* **shared** - The module containing the code shared between all platforms.
* **features** - The modules containing the features (visual or not) of the application.
* **domain** - The modules containing the business logic. These modules depend only on themselves,
  and all interaction is done via _dependency inversion_.
* **data** - The modules containing the data logic (local, repository, etc.).
* **libraries** - The modules with useful small libraries for the project, such as design system,
  navigation, testing, etc.

This type of architecture protects the most important modules in the app. To achieve this, all
dependencies point to the center. The modules are organized so that _the more a module is in the
center, the more important it is_.

To better represent the idea behind the modules, here is an architecture image representing the flow
of dependency:

![Alkaa Architecture](assets/alkaa-arch-3-0.png)

## 📃 License

```
Copyright 2018 Igor Escodro

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
