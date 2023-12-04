#!/bin/bash

if [ -z "$1" ]; then
	echo "Parameter missing: path to input file" >&2
	exit 1
fi

java -jar "${0%/*}/../gear-ratios/target/day03.gear-ratios-1.0-SNAPSHOT-jar-with-dependencies.jar" detect-gears "$1"
