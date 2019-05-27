# OHDM Offline Viewer
> View OHDM maps offline
 
![HOME](screenshots/home.png)
![MAP](screenshots/map.png)

![DOWNLOAD](screenshots/download.png)
![ABOUT](screenshots/about.png)

        
## Goal of this project
The goal of this repository is to be to render [Open Street Maps](https://www.openstreetmap.de/), without the need of a network connection.
To avoid caching dozens of different zoom layers, vector tiles are being used. 

## Getting Started
*Follow these instructions to build and run the OHDM Offline Viewer*
1. Clone this repository.
2. [Install Android Studio](https://developer.android.com/sdk/index.html).
3. [Downlaod Open Historical Data maps](http://www.ohdm.net/)
4. Store them on your phones internal storage
5. Import the project. Open Android Studio, click `Open an existing Android
   Studio project` and select the project. Gradle will build the project.
6. Run the app. Click `Run > Run 'app'`. After the project builds you'll be
   prompted to build or launch an emulator. 
7. Place your maps on the device storage. There should be a ```OHDM``` directory in the internal storage.
