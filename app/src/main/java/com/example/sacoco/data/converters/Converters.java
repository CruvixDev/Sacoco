package com.example.sacoco.data.converters;

import androidx.room.TypeConverter;

import com.example.sacoco.models.ClothTypeEnum;

import java.net.URI;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static URI clothImagePathStringToURI(String clothImagePathString) {
        return clothImagePathString == null ? null : URI.create(clothImagePathString);
    }

    @TypeConverter
    public static String clothImagePathURIToString(URI clothImagePathURI) {
        return clothImagePathURI == null ? null : clothImagePathURI.toString();
    }

    @TypeConverter
    public static UUID clothIdentifierStringToUUID(String clothIdentifierString) {
        return clothIdentifierString == null ? null : UUID.fromString(clothIdentifierString);
    }

    @TypeConverter
    public static String clothIdentifierUUIDToString(UUID clothIdentifierUUID) {
        return clothIdentifierUUID == null ? null : clothIdentifierUUID.toString();
    }

    @TypeConverter
    public static ClothTypeEnum clothTypeIntToEnum(int clothTypeInt) {
        return ClothTypeEnum.values()[clothTypeInt];
    }

    @TypeConverter
    public static int clothTypeEnumToInt(ClothTypeEnum clothTypeEnum) {
        return clothTypeEnum.ordinal();
    }
}
