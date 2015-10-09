#!/bin/bash

which sqlite3 &> /dev/zero

if [ $? -eq 1 ]
    then
    echo "sqlite Befehl nicht gefunden. Breche ab."
    exit 1
fi

echo "lösche Datenbank..."
rm db.sqlite
echo "lösche Test Datenbank"
rm core/db.sqlite
echo "erstelle neue Datenbank für Live-Service (db.sqlite)..."
sqlite3 db.sqlite < dbscript.sql

echo "erstelle neue Datenbank für Tests (core/db.sqlite)..."
sqlite3 core/db.sqlite < dbscript.sql

