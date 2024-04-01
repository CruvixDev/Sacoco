package com.example.sacoco.data.converters;

import androidx.room.TypeConverter;

import com.example.sacoco.models.ClothTypeEnum;

import java.util.UUID;

public class Converters {
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
