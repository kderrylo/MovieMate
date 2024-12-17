# MovieMate

MovieMate is an Android application designed to help users discover and track movies. With a user-friendly interface and powerful features, MovieMate allows you to explore the world of cinema easily.

## Features

- Explore a collection of movies and TV shows
- Search for movies and TV shows.
- View detailed information about movies (synopsis, release date, ratings, etc.).
- Create a watchlist of your favorite movies.
- Get recommendations based on your preferences.
- User authentication
  
## Technologies Used

- Android SDK
- Java/Kotlin
- Gradle
- Retrofit for API calls
- SQLite for local data storage
- Firebase Authentication for user management
- Firestore for cloud-based data storage

## Installation

To run the application locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/kderrylo/MovieMate.git

2. Navigate to the project directory:
   ```bash
   cd MovieMate

3. Set Up Firebase

    1. Create a Firebase Project
        - Go to the Firebase Console.
        - Click "Add Project" and complete the setup.
        
    2. Register Your Android App
        - Click Add App → Android.
        - Enter the Package Name:
            ```bash
            com.example.moviemate
        - Follow the steps to download the google-services.json file.
        
    3. Replace the Template google-services.json
        - Copy the downloaded google-services.json file.
        - Replace the existing file located in:
            ```bash
            app/google-services.json
            
    4. Enable Firebase Authentication
        - Go to Firebase Console → Authentication → Sign-in method.
        - Enable Email/Password as the sign-in method.

    5. Set Firestore Database Rules
        - Go to Firebase Console → Firestore Database → Rules.
        - Replace the default rules with the following:
            ```bash
            service cloud.firestore {
              match /databases/{database}/documents {
                match /users/{userId} {
                  allow read, write: if request.auth != null && request.auth.uid == userId;
                }
                match /{document=**} {
                  allow read: if true;  
                  allow write: if false;
                }
              }
            }
        - Click Publish to apply the rules.

4. Open the Project in Android Studio
    - Open Android Studio Koala (latest version).
    - Select Open and navigate to the MovieMate directory.
    - Let Gradle sync the project and resolve dependencies.

5. Run the Application
    - Connect a physical device or start an Android emulator.
    - Run the app
    
## Demo

<div align="center">

[![Video Title](https://img.youtube.com/vi/hZgVqukWVYA/0.jpg)](https://www.youtube.com/watch?v=hZgVqukWVYA)

![thanks](https://media3.giphy.com/media/26gsjCZpPolPr3sBy/giphy.gif)

</div>