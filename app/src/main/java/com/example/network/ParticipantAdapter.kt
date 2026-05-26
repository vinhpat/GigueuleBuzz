package com.example.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

class ParticipantAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): ParticipantDto {
        return if (reader.peek() == JsonReader.Token.STRING) {
            ParticipantDto(userName = reader.nextString(), buzzTime = null)
        } else {
            reader.beginObject()
            var userName = ""
            var buzzTime: Long? = null
            while (reader.hasNext()) {
                val name = reader.nextName()
                if (name == "userName") {
                    userName = reader.nextString()
                } else if (name == "buzzTime") {
                    if (reader.peek() == JsonReader.Token.NULL) {
                        reader.nextNull<Unit>()
                    } else {
                        buzzTime = reader.nextLong()
                    }
                } else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            ParticipantDto(userName, buzzTime)
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: ParticipantDto) {
        writer.beginObject()
        writer.name("userName").value(value.userName)
        if (value.buzzTime != null) {
            writer.name("buzzTime").value(value.buzzTime)
        } else {
            writer.name("buzzTime").nullValue()
        }
        writer.endObject()
    }
}
