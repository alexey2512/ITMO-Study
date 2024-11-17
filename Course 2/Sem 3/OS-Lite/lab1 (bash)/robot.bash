#!/bin/bash

h=$1
w=$2
x=$((w / 2))
y=$((h / 2))

while true; do
  echo "x=$x;y=$y"
  read -rsn 1 dir
  case "$dir" in
    W|w) ((y++)) ;;
    A|a) ((x--)) ;;
    S|s) ((y--)) ;;
    D|d) ((x++)) ;;
  esac
  if ((x < 0 || x >= w || y < 0 || y >= h)) || [[ "$dir" == "q" ]]; then
      break
    fi
done