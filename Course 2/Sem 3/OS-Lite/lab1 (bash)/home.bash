#!/bin/bash

[ "$PWD" = "$HOME" ] && \
echo "$HOME" && exit 0 || \
echo "error: script run not in home directory" && exit 1
