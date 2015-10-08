#!/bin/bash

echo "lösche Datenbank..."
rm db.sqlite
echo "lösche Test Datenbank"
rm core/db.sqlite
echo "erstelle neue Datenbank für Live-Service (db.sqlite)..."
sqlite3 db.sqlite < dbscript.sql

echo "erstelle neue Datenbank für Tests (core/db.sqlite)..."
sqlite3 core/db.sqlite < dbscript.sql

