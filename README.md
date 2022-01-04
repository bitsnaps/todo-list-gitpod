[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/from-referrer/)

Original example by [Hallvard Trætteberg](https://gitlab.stud.idi.ntnu.no/it1901/todo-list)

# JavaFX on Android using GraalVM and Gluon's client-maven-plugin

This branch allows you to build a native Android application using GraalVM and Gluon's client-maven-plugin.
However, if you code inside gitpod you cannot debug through the USB connection, so installing and running must be done locally:

- Configure gitpod to use a docker image with the last graalvm and set it as default
- Code your application and provide build instructions according to [gluons documentation](https://docs.gluonhq.com/client/0.1.31)
- Run **mvn -Pandroid package client:build client:package** to build the Android application
- Download the **aarch64-android** folder in *target/client* (you get a tar file). I think you could do with a bit less, but I haven’t experimented with minimising the download.
- Clone your project onto your mac and untar the **aarch64-android.tar** file into **target/client** so you get the same/necessary build state as in gitpod
- Connect your phone to the USB port and ensure it enters debug mode and run **mvn -Pandroid client:install client:run**
- The app should open on your phone and you’ll see lots of debug output from it in the console

