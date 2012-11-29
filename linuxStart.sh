#!/bin/bash

version=$(uname -a)

case "$version" in
	*x86_64*) $(rm version) $(echo nodejs/linuxx64/bin/node http.js);;
	*i386*) $(rm version) $(echo nodejs/linuxx86/bin/node http.js);;
esac
