#!/bin/bash

declare -A numbers
numbers[zero]=0
numbers[one]=1
numbers[two]=2
numbers[three]=3
numbers[four]=4
numbers[five]=5
numbers[six]=6
numbers[seven]=7
numbers[eight]=8
numbers[nine]=9
numbers[0]=0
numbers[1]=1
numbers[2]=2
numbers[3]=3
numbers[4]=4
numbers[5]=5
numbers[6]=6
numbers[7]=7
numbers[8]=8
numbers[9]=9

total=0

while read -e line; do
	if [ -z "$line" ]; then
		continue
	fi
	pair="$(echo "$line$line" | perl -pe 's/^.*?(zero|one|two|three|four|five|six|seven|eight|nine|[0-9]).*(zero|one|two|three|four|five|six|seven|eight|nine|[0-9]).*?$/\1|\2/')"
	first="${pair%|*}"
	second="${pair#*|}"
	actual="${numbers["$first"]}${numbers["$second"]}"
	total="$((total+actual))"
#	echo "parsed=$pair actual=$actual total=$total line=$line"
done

echo "Total sum: $total"
