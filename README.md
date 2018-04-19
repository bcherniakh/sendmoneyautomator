## How to run
1. download the selenium chromedriver: [link](https://sites.google.com/a/chromium.org/chromedriver/downloads)
2. assemble the app by ```./gradlew exportApp```
Application files will be exported to *application* directory in the project. 
3. specify the absolute path to the chromedriver in *selenium.webdriver.path* property
When application.properties are filled, the app is ready to run.

Is was a playground for Google Guice and should not be taken seriously.