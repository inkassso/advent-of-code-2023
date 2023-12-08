#!/bin/bash

if [ -z "$1" ]; then
	echo "Parameter missing: path to input file" >&2
	exit 1
fi

mvn_proj="${0%/*}/../camel-cards"

pushd "$mvn_proj"
mvn compile assembly:single
popd

java -Dlog.level.root=INFO -jar "$mvn_proj/target/day07.camel-cards-1.0-SNAPSHOT-jar-with-dependencies.jar" with-joker "$1"
