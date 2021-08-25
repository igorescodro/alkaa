![build](https://github.com/igorescodro/alkaa/actions/workflows/build.yml/badge.svg)
![instrumented tests](https://github.com/igorescodro/alkaa/actions/workflows/android_tests.yml/badge.svg)
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>
[![codebeat badge](https://codebeat.co/badges/5dce2515-5fa7-4885-bfab-2a2905d84ee5)](https://codebeat.co/projects/github-com-igorescodro-alkaa-main)
[![CodeFactor](https://www.codefactor.io/repository/github/igorescodro/alkaa/badge/main)](https://www.codefactor.io/repository/github/igorescodro/alkaa/overview/main)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d88ab7250f1a4e9fb0a96dec11a0c2cd)](https://www.codacy.com/manual/igorescodro/alkaa?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=igorescodro/alkaa&amp;utm_campaign=Badge_Grade)

<img src="app/src/main/ic_launcher-web.png" width="200">

# Alkaa 2.0

Alkaa (_begin_, _start_ in Finnish) is a to-do application project to study the latest components,
architecture and tools for Android development. The project evolved a lot since the beginning is
available on Google Play! :heart:

The current version of Alkaa was also completely migrate to **Jetpack Compose**!


<img src="https://play-lh.googleusercontent.com/tzN8wqrM0YpcCbsFP9ttzln48nf2bz8EUcUtrAjf2v7YheAz3SNg5Xz36zJVuVjghBY=w1920-h995" width="186">  <img src="https://play-lh.googleusercontent.com/_W5kBODa9LQVTp0T0eviIWs-ajD3yiX-VdJKwogMGQpBI2eUrjThwvI1TNzyggQSlLA=w1920-h995" width="186">  <img src="https://play-lh.googleusercontent.com/mkhrcsfHAK8pPEKkUIMaq0FWYD8MYR6oYzhDB8l2jtX6XQXcUneed1TRWXAhtt_YMcA=w1920-h995" width="186">  <img src="https://play-lh.googleusercontent.com/041NuKD-ODmBsEMW9sgzIL6cWf4RRDPV_uVOkCo031YY8vTImw-uEdv6bU_buxBTr9MR=w1920-h995" width="186"> 

## 📦 Download

<a href='https://play.google.com/store/apps/details?id=com.escodro.alkaa'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=240/></a>

## 📚 Android tech stack

One of the main goals of Alkaa is too use all the latest libraries and tools available.

### 🧑🏻‍💻 Android development

- Application entirely written in [Kotlin](https://kotlinlang.org)
- Complete migrated to [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Asynchronous processing using [Coroutines](https://kotlin.github.io/kotlinx.coroutines/)
- [Dynamic delivery](https://developer.android.com/guide/playcore/feature-delivery) for the _Task
  Tracker_ feature
- Dependency injection with [Koin](https://insert-koin.io)
- Database using [Room](https://developer.android.com/topic/libraries/architecture/room)

For more dependencies used in project, please access the
[Dependency File](https://github.com/igorescodro/alkaa/blob/main/buildSrc/src/main/java/Dependencies.kt)

If you want to check the previous version of Alkaa, please take a look at
the [last V1 release](https://github.com/igorescodro/alkaa/tree/v1.7.0)

### 🧪 Quality

- [klint](https://github.com/shyiko/ktlint)
- [detekt](https://github.com/arturbosch/detekt)
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

* **app** - The Application module. It contains all the initialization logic for the Android
  environment and starts the _Jetpack Navigation Compose Graph_.
* **features** - The module/folder containing all the features (visual or not) from the application
* **domain** - The modules containing the most important part of the application: the business
  logic. This module depends only on itself and all interaction it does is via _dependency
  inversion_.
* **data** - The module containing the data (local, remote, light etc) from the app

This type of architecture protects the most important modules in the app. To achieve this, all the
dependency points to the center, and the modules are organized in a way that
_the more the module is in the center, more important it is_.

To better represents the idea behind the modules, here is a architecture image representing the flow
of dependency:

![Alkaa Architecture](assets/alkaa-2-0-architecture.png)

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
