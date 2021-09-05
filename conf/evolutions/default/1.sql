# Users schema

# --- !Ups

CREATE TABLE "race" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "name" VARCHAR NOT NULL,
                        "description" TEXT NOT NULL
);

CREATE TABLE "race_feature" (
                        "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        "name" VARCHAR NOT NULL,
                        "description" TEXT NOT NULL,
                        "race_id" INTEGER NOT NULL
);

CREATE TABLE "origin" (
                     "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                     "name" VARCHAR NOT NULL,
                     "description" TEXT NOT NULL
);

CREATE TABLE "proficiency" (
                       "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                       "name" VARCHAR NOT NULL,
                       "type" INTEGER NOT NULL
);

CREATE TABLE "origin_proficiency" (
                          "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                          "origin_id" INTEGER NOT NULL,
                          "proficiency_id" INTEGER NOT NULL
);

# --- !Downs

DROP TABLE "race";
DROP TABLE "origin";
DROP TABLE "proficiency";
DROP TABLE "origin_proficiency";
DROP TABLE "race";
DROP TABLE "race_feature";
