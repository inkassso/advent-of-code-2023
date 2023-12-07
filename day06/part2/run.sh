#!/bin/bash

if [ -z "$1" ]; then
	echo "Parameter missing: path to input file" >&2
	exit 1
fi

mvn_proj="${0%/*}/../boat-races"

pushd "$mvn_proj"
mvn compile assembly:single
popd

java -Dlog.level.root=INFO -jar "$mvn_proj/target/day06.boat-races-1.0-SNAPSHOT-jar-with-dependencies.jar" single "$1"
