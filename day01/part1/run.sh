#!/bin/bash

total=0

while read -e line; do
  nr="$(echo "$line$line" | sed -E 's/[^0-9]*([0-9]).*([0-9])[^0-9]*/\1\2/g')"
  total="$((total+nr))"
#  echo "curent=$nr total=$total"
done

echo "Total sum: $total"
