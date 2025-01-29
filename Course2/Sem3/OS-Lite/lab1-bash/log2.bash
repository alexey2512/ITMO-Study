#!/bin/bash

out="X_info_warn.log"

grep "(WW)" /var/log/Xorg.0.log | sed "s/(WW)/Warning:/" | sed '1d' > "$out"
grep "(II)" /var/log/Xorg.0.log | sed "s/(II)/Information:/" | sed '1d' >> "$out"

cat "$out"

