![build](https://github.com/igorescodro/alkaa/actions/workflows/build.yml/badge.svg)
[![UI Tests](https://github.com/igorescodro/alkaa/actions/workflows/instrumented_tests.yml/badge.svg)](https://github.com/igorescodro/alkaa/actions/workflows/instrumented_tests.yml)
[![codebeat badge](https://codebeat.co/badges/5dce2515-5fa7-4885-bfab-2a2905d84ee5)](https://codebeat.co/projects/github-com-igorescodro-alkaa-main)
[![CodeFactor](https://www.codefactor.io/repository/github/igorescodro/alkaa/badge/main)](https://www.codefactor.io/repository/github/igorescodro/alkaa/overview/main)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d88ab7250f1a4e9fb0a96dec11a0c2cd)](https://www.codacy.com/manual/igorescodro/alkaa?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=igorescodro/alkaa&amp;utm_campaign=Badge_Grade)
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>

<img src="app/src/main/ic_launcher-web.png" width="200">

# Alkaa 3.0 - Multiplatform

Alkaa (_begin_, _start_ in Finnish) is a to-do application project to study the latest components,
architecture and tools for Android development. The project evolved a lot since the beginning is
available on Google Play and App Store! ❤️

The current version of Alkaa was completely migrate to **Kotlin and Compose Multiplatform**!

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

One of the main goals of Alkaa is too use all the latest libraries and tools available.

### 🧑🏻‍💻 Multiplatform development

- Application entirely written in [Kotlin](https://kotlinlang.org)
- UI developed in [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- UI Tests with [Compose Multiplatform UI Testing](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html)
- Following the [Material You](https://m3.material.io/) guidelines and dynamic color (Android only)
- Asynchronous processing using [Coroutines](https://kotlin.github.io/kotlinx.coroutines/)
- Widgets using [Jetpack Glance](https://developer.android.com/jetpack/androidx/releases/glance)
- Dependency injection with [Koin](https://insert-koin.io)
- Database using [SQLDelight](https://github.com/cashapp/sqldelight)

For more dependencies used in project, please access the
[Dependency File](https://github.com/igorescodro/alkaa/blob/main/gradle/libs.versions.toml)

If you want to check the previous version of Alkaa, please take a look at
the latest [V1](https://github.com/igorescodro/alkaa/tree/v1.7.0) or [V2](https://github.com/igorescodro/alkaa/tree/v2.3.0) releases.

### 🧪 Quality

- [klint](https://github.com/shyiko/ktlint)
- [detekt](https://github.com/arturbosch/detekt)
- [compose-rules](https://github.com/twitter/compose-rules)
- [lint](https://developer.android.com/studio/write/lint)
- [codebeat](https://codebeat.co)
- [CodeFactor](https://www.codefactor.io/)
- [Codacy](http://codacy.com)

## 🏛 Architecture

Alkaa architecture is strongly based on
the [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) by Alistair
Cockburn. The application also relies heavily in modularization for better separation of concerns
and encapsulation.

Let's take a look in each major module of the application:

* **app** and **ios-app** - The platform-specific app module. It contains all the initialization logic for each platform.
* **features** - The module/folder containing all the features (visual or not) from the application
* **domain** - The modules containing the most important part of the application: the business
  logic. This module depends only on itself and all interaction it does is via _dependency
  inversion_.
* **data** - The module containing the data (local, remote, light etc) from the app.
* **libraries** - The module with useful small libraries for the project, such as design system,
* navigation, test etc.

This type of architecture protects the most important modules in the app. To achieve this, all the
dependency points to the center, and the modules are organized in a way that
_the more the module is in the center, more important it is_.

To better represents the idea behind the modules, here is a architecture image representing the flow
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
