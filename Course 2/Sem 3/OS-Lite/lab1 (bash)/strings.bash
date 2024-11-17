#!/bin/bash

while true; do
  read -r str
  if [ "$str" == "q" ]; then
    break
  fi
  echo ${#str}
  if [[ "$str" =~ ^[a-zA-Z]+$ ]]; then
    echo "true"
  else
    echo "false"
  fi
done
