#!/bin/bash

echo "lösche Datenbank..."
rm db.sqlite
echo "erstelle neue Datenbank..."
sqlite3 db.sqlite < dbscript.sql

