<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/d/d7/Android_robot.svg" alt="Android Icon" width="100"/>
</p>

<h1 align="center">NewsHub - Android News App</h1>

<p align="center">
  <strong>An Android application delivering the latest news from multiple categories.</strong><br/>
  <em>Powered by <a href="https://newsapi.org/">NewsAPI</a></em>
</p>

---

## ✨ Features

1. **🏠 Home News Feed**
    - Displays the latest top headlines.
    - Automatically updates content based on user preferences.

2. **🗂️ Category-Specific News**
    - Browse news by categories: Health, Science, Sports, and Entertainment.

3. **🔎 Search Functionality**
    - Search for articles using keywords.

4. **📌 Bookmarks**
    - Save articles for future reading using Room Database.

5. **⚙️ Settings**
    - Change app language preferences.
    - Clear all saved bookmarks.

6. **📖 Detailed Reading**
    - Open full articles using an in-app WebView.

7. **💫 Animations and Smooth UI**
    - RecyclerView with custom animations for better user experience.

8. **📝 Article Summarizer**
    - Uses the Hugging Face API to generate concise summaries.
    - Summarizes a news article on a button click and displays a popup summary (between 100-200 words).

---

## 🚀 Setup Instructions

### Prerequisites

- <img src="https://img.shields.io/badge/Android%20Studio-4CAF50?style=flat&logo=android-studio&logoColor=white" alt="Android Studio Badge"/> Android Studio installed on your machine.
- A valid API key from [NewsAPI](https://newsapi.org/).

### Steps to Setup

1. **Clone this Repository**
   ```bash
   git clone <repository-url>
   cd NewsHub




## Add Your API Key
Open the `Constants.java` file located in the `com.cosc3p97.newshub` package and replace the placeholder with your actual NewsAPI key:

```java
package com.cosc3p97.newshub;

public class Constants {
    public static final String API_KEY = "PUT_YOUR_KEY_HERE";
}
```

## Build and Run

1. **Open the project in Android Studio.**  
   Make sure you have the latest version of Android Studio installed.

2. **Sync the Gradle files.**  
   This can be done automatically when prompted, or by selecting
   **File → Sync Project with Gradle Files**.

3. **Run the app on an emulator or physical device.**  
   Choose your preferred device configuration in the toolbar and then click
   **Run** (the green play button).


## 🏗 Code Structure

---

### 🌐 API Integration

#### **ApiInterface**
- Defines API endpoints for fetching articles using Retrofit.
- **Supported operations**:
  - Fetch top headlines
  - Get articles by category
  - Search articles by keyword

#### **ApiUtilities**
- Configures Retrofit with `OkHttpClient` for handling API requests and responses.

---

### 🗃 Database

#### **AppDatabase**
- Implements a Room Database to manage bookmarks locally.
- Stores bookmarked articles as `Model` objects.

#### **BookmarkDao**
- Provides methods for adding, removing, and retrieving bookmarks.
- Allows clearing all bookmarks from the database.

---

### 🎨 UI Components

#### **Fragments**
- **HomeFragment:** Displays top news headlines.  
- **Category Fragments:** Include `HealthFragment`, `ScienceFragment`, `SportsFragment`, and `EntertainmentFragment`. These fetch news articles filtered by category.  
- **SearchFragment:** Handles keyword-based search and displays results.  
- **BookmarkFragment:** Lists all saved bookmarks retrieved from the database.  
- **SettingsFragment:** Allows users to select a preferred language and provides the option to clear bookmarks.

#### **Activities**
- **MainActivity:**  
  - Hosts the app’s navigation and manages fragment transitions.  
  - Includes bottom navigation for switching between categories.
- **ReadNewsActivity:**  
  - Displays a selected article using a `WebView` for detailed reading.

#### **Adapter**
- **Custom RecyclerView Adapter:**  
  - Renders articles in a list.  
  - Handles bookmarking actions.  
  - Implements smooth animations for list items.


## 📌 Key Files and Highlights

- **Constants.java**  
  Stores the API key for NewsAPI. Make sure to replace `PUT_YOUR_KEY_HERE` with your actual API key.

- **AndroidManifest.xml**  
  Includes necessary permissions and configures activities and app metadata.

- **Adapter.java**  
  Manages article rendering in `RecyclerView`. Implements click listeners for bookmarking articles and opening full articles in **ReadNewsActivity**.

- **MainActivity.java**  
  Acts as the app's entry point. Manages bottom navigation and handles fragment transitions.

- **ReadNewsActivity.java**  
  Uses a `WebView` to display the full content of articles. Ensures smooth error handling for unsupported pages.

---

## 💻 Technologies Used

| Technology                                                                                             | Description                                       |
|--------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| <img src="https://img.shields.io/badge/Android_SDK-3DDC84?style=flat&logo=android&logoColor=white"/>   | Core framework for Android development            |
| <img src="https://img.shields.io/badge/Retrofit-FFCA28?style=flat&logo=android&logoColor=black"/>      | For network requests to NewsAPI                   |
| <img src="https://img.shields.io/badge/Room_Database-4285F4?style=flat&logo=google&logoColor=white"/>  | Local data storage for bookmarks                  |
| <img src="https://img.shields.io/badge/Glide-8BC34A?style=flat&logo=android&logoColor=white"/>         | Efficient image loading in RecyclerView           |
| <img src="https://img.shields.io/badge/RecyclerView-2196F3?style=flat&logo=android&logoColor=white"/>  | Dynamic and scrollable UI list                    |
| <img src="https://img.shields.io/badge/WebView-9C27B0?style=flat&logo=android&logoColor=white"/>       | Display full articles inside the app              |
  

---

## 🔮 Future Enhancements

1. **Dark Mode**  
   Provide an option for light and dark themes.

2. **Offline Reading**  
   Allow users to save articles locally for offline access.

3. **Push Notifications**  
   Notify users of breaking news or trending topics.

4. **Personalized News Feed**  
   Implement user preferences to tailor the news feed.
