#!/bin/bash

max_red=12
max_green=13
max_blue=14

total=0

while read -e game; do
	if [ -z "$game" ]; then
		continue
	fi
	game_id="$(echo "$game" | sed -E 's/Game ([0-9]+):.*/\1/')"
	echo "GAME $game_id"
	sets="${game#*: }"
	IFS=';'
	for set in $sets; do
		red="$(echo "$set" | perl -pe 's/.*?([0-9]+) red.*/\1/')"
		if [ "$red" = "$set" ]; then red=0; fi
		green="$(echo "$set" | perl -pe 's/.*?([0-9]+) green.*/\1/')"
		if [ "$green" = "$set" ]; then green=0; fi
		blue="$(echo "$set" | perl -pe 's/.*?([0-9]+) blue.*/\1/')"
		if [ "$blue" = "$set" ]; then blue=0; fi
#		echo -n "red=$red, green=$green, blue=$blue, set='$set'"
		if (( red > max_red )) || (( green > max_green )) || (( blue > max_blue)); then
#			echo ", rejected"
			game_id=0
			break
		fi
#		echo ""
	done
	total="$(( total + game_id ))"
done

echo "Total sum: $total"
