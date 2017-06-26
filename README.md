# Common Utils
A collection of common utility methods used by Intrepid.

# Table of Contents
1. [Setup](#setup)
2. [Release](#release)
3. [License](#license)

# Setup
Add the following lines to your build.gradle file:
```
dependencies {
    compile "io.intrepid.commonutils:commonutils:0.2.2"
}
```

# Release
To release a new version of this library, follow these steps:
1. Create a new branch containing the version # in its name
2. Increment the `versionName` in `build.gradle` (changing the `versionCode` is not required)
3. Update the current version number in `README.md`
4. Add a new entry for the current version in `CHANGELOG.md`
5. Submit a PR against the `develop` branch with your changes
6. Once approved, squash and merge your changes in GitHub.
7. Then check out the 'develop' branch in Android Studio.
8. Run `./gradlew clean testDebugUnitTest` to verify that everything is working correctly.
9. Obtain the appropriate release credentials (you can ask the `#android` Slack channel) and add them to your local.properties file.
You will need the following information:
```
bintray_user=####
bintray_key=####
bintray_gpg_passphrase=####
sonatype_user=####
sonatype_password=####
```
10. Then run `./gradlew bintrayUpload` to publish the library to Bintray.
11. Once that is complete, verify that the new version of the library is available by updating another project which uses this library to use the new version # and rebuilding it.

# License
```
Copyright 2016 Intrepid Pursuits LLC.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
