 <h1 align="center">v2plus</h1></br>

 <p align="center">
 🥪  A third party android application for v2ex.
 </p>
 </br>

<p align="center">
   <a href="https://github.com/AiryMiku/v2plus_android/actions"><img alt="Android CI" src="https://github.com/AiryMiku/v2plus_android/workflows/Android%20CI/badge.svg"/></a>
</p>

## Tech stack & Open-source libraries
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct a database using the abstract layer.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.

## Architecture
v2plus is based on MVVM architecture and a repository pattern.