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
6. Once approved, squash and merge your changes and then ask the `#android` Slack channel about how to publish the new version to Bintray.

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
