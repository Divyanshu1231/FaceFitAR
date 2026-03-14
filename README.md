# FaceFit AR

FaceFit AR is a real-time Android face filter application built using **CameraX** and **Google ML Kit**.
The app detects facial landmarks and dynamically applies AR filters like glasses, crowns, masks, and animal ears.

---

## 📱 Features

* Real-time face detection using **Google ML Kit**
* Face landmark tracking
* Multiple AR filters

  * Glasses
  * Crown
  * Cat Ears
  * Dog Ears
  * Mask
* Capture photos with filters applied
* Video recording
* Firebase authentication (Login / Register)
* Gallery preview of last captured media
* MVVM architecture

---

## 🛠 Tech Stack

* **Language:** Kotlin
* **Camera:** CameraX API
* **Machine Learning:** Google ML Kit
* **Authentication:** Firebase Authentication
* **Architecture:** MVVM
* **UI Components:** RecyclerView, ConstraintLayout
* **Version Control:** Git & GitHub

---

## 🧠 Architecture

The application follows the **MVVM architecture pattern**.

View
↓
ViewModel
↓
Repository
↓
Model

Modules:

* **UI Layer**

  * SplashActivity
  * LoginActivity
  * RegisterActivity
  * CameraActivity

* **ML Layer**

  * FaceAnalyzer (ML Kit face detection)

* **Filter Engine**

  * FilterOverlayView

---

## 📸 Screenshots

Add screenshots in the **screenshots folder**.

| Login Screen                    | Camera Screen                     |
| ------------------------------- | --------------------------------- |
| ![Login](screenshots/login.png) | ![Camera](screenshots/camera.png) |

| Filters                             | Photo Capture                       |
| ----------------------------------- | ----------------------------------- |
| ![Filters](screenshots/filters.png) | ![Capture](screenshots/capture.png) |

---

## 📦 APK

APK file available in:

```
apk/app-debug.apk
```

Install the APK to test the application.

---

## 🎬 Demo Video

Demo video included with assignment submission.

Features demonstrated:

* Login / Register
* Real-time face detection
* Filter selection
* Photo capture
* Video recording
* Gallery preview

---

## 📂 Project Structure

```
FaceFitAR
 ├ app
 ├ apk
 │   └ app-debug.apk
 ├ docs
 │   └ FaceFitAR_Documentation.pdf
 ├ screenshots
 ├ README.md
 └ gradle files
```

---

## 👨‍💻 Author

**Divyanshu Srivastava**

Android Developer | Kotlin | ML Kit | CameraX
