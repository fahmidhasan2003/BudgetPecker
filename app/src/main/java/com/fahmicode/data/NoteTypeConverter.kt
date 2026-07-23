package com.fahmicode.data

import androidx.room.TypeConverter
import com.fahmicode.data.model.ChecklistItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class NoteTypeConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val listType = Types.newParameterizedType(List::class.java, ChecklistItem::class.java)
    private val adapter = moshi.adapter<List<ChecklistItem>>(listType)

    @TypeConverter
    fun fromChecklistItems(items: List<ChecklistItem>?): String {
        return adapter.toJson(items ?: emptyList())
    }

    @TypeConverter
    fun toChecklistItems(json: String?): List<ChecklistItem> {
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            adapter.fromJson(json) ?: emptyList()
        }
    }
}
