// Copyright Benoit Blanchon 2014-2015
// MIT License
//
// Arduino JSON library
// https://github.com/bblanchon/ArduinoJson

#pragma once

#include "JsonVariant.hpp"

namespace ArduinoJson {

// A key value pair for JsonObject.
struct JsonPair {
  const char* key;
  JsonVariant value;
};
}
