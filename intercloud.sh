#!/usr/bin/env bash

_base="$(pwd)"

function deployGateway {
    cd "${_base}/gateway" && mvn clean install wildfly:deploy
}

function deployRoot {
    cd "${_base}/root" && mvn clean install wildfly:deploy
}

[ "${#}" -eq 1 ] || { echo "Usage: $(basename $0) deployGateway | deployRoot"; exit 1; }

for arg in "$@"; do
    [ "${arg}" = "deployGateway" ] && deployGateway
    [ "${arg}" = "deployRoot" ] && deployRoot
done
