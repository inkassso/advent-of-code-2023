#!/bin/bash

total=0

while read -e game; do
	if [ -z "$game" ]; then
		continue
	fi

	game_id="$(echo "$game" | sed -E 's/Game ([0-9]+):.*/\1/')"
	sets="${game#*: }"

	min_red=0
	min_green=0
	min_blue=0

	IFS=';'
	for set in $sets; do
		red="$(echo "$set" | perl -pe 's/.*?([0-9]+) red.*/\1/')"
		if [ "$red" = "$set" ]; then red=0; elif (( red > min_red )); then min_red=$red; fi
		green="$(echo "$set" | perl -pe 's/.*?([0-9]+) green.*/\1/')"
		if [ "$green" = "$set" ]; then green=0; elif (( green > min_green )); then min_green=$green; fi
		blue="$(echo "$set" | perl -pe 's/.*?([0-9]+) blue.*/\1/')"
		if [ "$blue" = "$set" ]; then blue=0; elif (( blue > min_blue )); then min_blue=$blue; fi
		echo "red=$red, green=$green, blue=$blue, set='$set'"
	done
	power="$(( min_red * min_green * min_blue ))"
	echo "Game $game_id: power=$power"
	total="$(( total + power ))"
done

echo "Total sum: $total"
