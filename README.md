# Simple Note App

Note App is a simple Android application to store daily notes in the internal database of the application and can store an image or a link
## Screen Recourding


https://user-images.githubusercontent.com/72816466/142661386-26771c73-9ee8-4b96-9024-e1398630d1e2.mp4




## Dependencies

```kotlin

# room database
  def room_version = "2.3.0"
  implementation "androidx.room:room-ktx:$room_version"
  kapt "androidx.room:room-compiler:$room_version"

# coroutines
  implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0'
  
# enable data binding
  buildFeatures {
        dataBinding true
    }
    
  ```
## Implementation Language
- Kotlin
- xml
