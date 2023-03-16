# Yes Chef
## Recipe Aggregator Application created in Android Studio with Java


### Description
Yes Chef is a recipe aggregator application that displays recipes that a user can make based on their ingredient selection. The application utilizes an SQLLite database and the Spoonacular API to enable its function. The application was created between August-December 2022 with a small team.

### End Evaluation
The development of the application was confined to the duration of a single Fall semester, which limited time to polish the application. The app functions as intended, however if it were to be redeveloped, emphasis would be put on improving performance speed, threading, exception handling, and the use of an online database instead of a file. The project was intended to introduce and explore developing applications in Android Studio with Java and XML files.


### How to Run
Download all files and open the application in Android Studio. Run the application, it will not launch but this step is necessary to access the device or emulator files. Download the YesChef_db_original file, and import it to the emulator or device files by cicking Device File Explorer and navigating to data/data/com.example.yeschef/databases. Delete the file YesChef_db_original.db in the database folder, then reimport the downloaded database file and rerun the application. The application should launch.

#### Note: The API key that is provided limits the number of calls to the API that are allowed. If the app stops running, replace the APIKey variable in the RecipesActivity file with the key listed as APIKey1. The keys reset every day and can be swapped as needed.

#### The PowerPoint presentation follows the development of the application in further detail and has a demo from the emulator. A demo of the app being run on a physical device can be found here: https://youtu.be/d8frD2oO6vE
