#!/bin/bash

awk -v group=$1 '$4 == group && $5 < 4 { print $1, $2, $3 }' students.txt

