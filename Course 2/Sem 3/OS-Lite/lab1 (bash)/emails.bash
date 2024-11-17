#!/bin/bash

grep -ERoah "[a-zA-Z.+-]+@[a-zA-Z-]+\.[a-zA-Z.-]+" /etc | \
sort -u | tr '\n' ',' | sed 's/,$//' > etc_emails.lst
