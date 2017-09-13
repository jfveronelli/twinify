# twinify
[![release](https://img.shields.io/github/release/jfveronelli/twinify.svg)](https://github.com/jfveronelli/twinify/releases/latest)
[![status](https://travis-ci.org/jfveronelli/twinify.svg?branch=master)](https://travis-ci.org/jfveronelli/twinify)
[![coverage](https://codecov.io/gh/jfveronelli/twinify/branch/master/graph/badge.svg)](https://codecov.io/gh/jfveronelli/twinify)

**Twinify** is a multiplatform Java library and application to backup a directory tree to a remote offline location.

Most backup solutions allow you to backup a filesystem to a local media, or a remote online location. This app, on the other hand is useful when:

- you need to backup (mirror) a directory tree,
- and your offsite backup location (the "1" in the 3-2-1 rule) is offline,
- and the size of the whole backup is very large,
- but the changeset is usually much smaller (and easily stored in cheaper media).


## Download

Latest version is 1.0.1, released on 2017/09/11.

A working [Java 6+](http://www.oracle.com/technetwork/java/javase) runtime environment is required.

Download the latest version from [here](https://github.com/jfveronelli/twinify/releases/download/v1.0.1/twinify-1.0.1.zip), extract the application, and then run the GUI like this:

    java -jar crossknight-twinify.jar

A console version (with less features) is also provided, by running:

    java -cp crossknight-twinify.jar ar.org.crossknight.twinify.console.App

The application may be run even from a USB stick.


## Author

Julio Francisco Veronelli <julio.veronelli@crossknight.com.ar>


## MIT License

Copyright (c) 2015 CrossKnight

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
