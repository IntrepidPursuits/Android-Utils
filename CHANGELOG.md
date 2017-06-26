Change Log
==========

Version 0.2.2 *(2017-06-26)*
----------------------------
* New:  `AndroidStringUtils.parseUriFromString()` method for adding the scheme (http://, https://, etc.) if it was missing when parsing a Uri from a String.
* Also created `AndroidStringUtils.parseHttpUriFromString()` and `AndroidStringUtils.parseHttpsUriFromString()` convenience methods.
* Enable local publication for testing

Version 0.2.1 *(2017-05-19)*
----------------------------
* Migrate to maven central
* New: `AndroidStringUtils.fromHtml()` method for calling the version appropriate `Html.fromHtml()`.

Version 0.2.0 *(2016-11-22)*
----------------------------
* Remove "Intrepid" prefix from classes.
* New: Additional methods to `ViewUtils` for setting visibility, width, height, and margins.
* New: `StringUtils` and `AndroidStringUtils` classes for common string manipulations.


Version 0.1.0 *(2016-04-21)*
----------------------------
Initial release.
