#!/bin/bash

function icon(){
convert $1 -resize 36x ../res/drawable-ldpi/ic_launcher.png
convert $1 -resize 96x ../res/drawable-xhdpi/ic_launcher.png
convert $1 -resize 48x ../res/drawable-mdpi/ic_launcher.png
convert $1 -resize 72x ../res/drawable-hdpi/ic_launcher.png
convert $1 -resize 144x ../res/drawable-xxhdpi/ic_launcher.png
}


icon $1

