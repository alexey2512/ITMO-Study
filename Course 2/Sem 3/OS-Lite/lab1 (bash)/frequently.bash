#!/bin/bash

man bash | tr -cs "[:alnum:]" "\n" | \
grep -E "^[[:alnum:]]{4,}$" | \
sort | uniq -c | sort -nr | head -n 3 | \
awk '{print $2}'


