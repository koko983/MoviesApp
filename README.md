# MoviesApp
The Popular Moview App project of the Udacity course "developing Android apps".
This was built using Android Studio 2.1., with SDK version 23.
Please make sure to add your own API key for the moviedb service. Do so in "build.gradle", at the following lines
    
    buildTypes.each {
        it.buildConfigField 'String', 'API_KEY', '"yourApiKeyHere"'
    }

Abdulrahman Almowafy
