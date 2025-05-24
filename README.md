# ShopFlow - Android Shopping App

![ShopFlow Home Screen](https://github.com/user-attachments/assets/e0f41bda-fcfe-4ef2-9d8c-1291e8be2761)

**ShopFlow** is a modern Android shopping app built with Jetpack Compose, featuring a sleek and intuitive user interface for browsing beauty products. The app includes a vibrant home screen with category filtering, product cards, favorites, and cart functionality, designed to provide a seamless shopping experience.

## Features

- **Modern UI**: Vibrant teal-to-indigo gradients, compact product cards, and pill-shaped category chips for a polished look.
- **Top Bar Actions**: Interactive heart (favorites), search, and cart icons with badge indicators.
- **Category Filtering**: Filter products by categories like Skin, Hair, Body, Makeup, or All.
- **Favorites System**: Toggle favorite products with a pink heart icon, with snackbar feedback.
- **Cart System**: Add products to the cart with snackbar confirmation.
- **Product Cards**: Display product title, price, and image with a sand-textured background (`R.drawable.background`).
- **Responsive Design**: Smooth animations for card elevation and chip selection.
- **Hero Banner**: Promotional banner with a call-to-action ("Explore Beauty Essentials").

## Screenshots

| Home Screen |
|-------------|
| ![Home Screen](https://github.com/user-attachments/assets/e0f41bda-fcfe-4ef2-9d8c-1291e8be2761) |

## Prerequisites

- **Android Studio**: Latest stable version (e.g., Koala | 2024.1.1 or later).
- **Kotlin**: Version 1.9.0 or higher.
- **Jetpack Compose**: Compatible with Compose 1.6.0+.
- **Min SDK**: 21 (Android 5.0 Lollipop).
- **Target SDK**: 34 (Android 14).

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/shopflow.git
2. **Open in Android Studio**:
Open Android Studio and select File > Open.
Navigate to the cloned shopflow directory and click OK.
Add Drawable Resources:
Place the following drawables in app/src/main/res/drawable:
background.png: Sand-textured background for product images.
productimage.png: Sample product image.
categorysample.png: Alternate product image.
Ensure images are PNGs with transparency for the background to be visible.
Update build.gradle: Ensure the app/build.gradle file includes the following dependencies:

dependencies {
    implementation "androidx.core:core-ktx:1.13.1"
    implementation "androidx.activity:activity-compose:1.9.2"
    implementation "androidx.compose.material3:material3:1.2.1"
    implementation "androidx.compose.animation:animation:1.6.0"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}

## Sync the project with Gradle.
## Run the App:
Connect an Android device or start an emulator.
Click Run > Run 'app' in Android Studio.

## Contributing
Contributions are welcome! To contribute:
Fork the repository.
Create a new branch (git checkout -b feature/your-feature).
Make changes and commit (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For questions or feedback, open an issue or contact your-Abhi95081.
