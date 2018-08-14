# Count-An-Offline-first-app
An Offline first app completely written in kotlin which combines the power of  MVVM + RxJava2+ Room + Dagger2

## What is this project about?
This project is a simple illustration of how an offline first app should be, how to create an app based on SOLID principles.
It's functionality is fairly simple. user enters an url into the edit text and hits fetch button to get all the words from that website along with their frequency. 

**There's also a filter button to apply various filters like**   
- remove common words (like *the,of,to* etc)
- set a limit on how words should be shown
- set a limit on word length

## Why this project was created?
It was created out of personal interest, but i put it on github because there was lack of resources on offline apps.

## Features
- Completely written in kotlin
- Illustrates the power of various RxJava2 operators like [just](http://reactivex.io/documentation/operators/just.html),[map](http://reactivex.io/documentation/operators/map.html),[filter](http://reactivex.io/documentation/operators/filter.html),[concat](http://reactivex.io/documentation/operators/concat.html),[first](http://reactivex.io/documentation/operators/first.html) *etc*
- Shows how each class should have it's own functionality
- Shows the usages of viewmodel
- Shows usages of dagger2 
- Shows usage of Android Room persistence library

## Libraries used
- [Rxjava2](https://github.com/ReactiveX/RxJava)
- [Jsoup](https://github.com/jhy/jsoup)
- [Dagger2](https://github.com/google/dagger)
- [Room](https://developer.android.com/topic/libraries/architecture/room)

There's a lot of room for improvements in this project, i'll be updating it whenever i get time, feel free to fork it and make the necessary changes.
