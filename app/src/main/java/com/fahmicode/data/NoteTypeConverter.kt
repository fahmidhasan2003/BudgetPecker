package com.fahmicode.data

import androidx.room.TypeConverter
import com.fahmicode.data.model.ChecklistItem
import com.fahmicode.data.model.RichTextContent
import com.fahmicode.data.model.CalculationTable
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class NoteTypeConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    
    private val checklistListType = Types.newParameterizedType(List::class.java, ChecklistItem::class.java)
    private val checklistAdapter = moshi.adapter<List<ChecklistItem>>(checklistListType)

    private val richTextAdapter = moshi.adapter(RichTextContent::class.java)
    private val calculationTableAdapter = moshi.adapter(CalculationTable::class.java)

    @TypeConverter
    fun fromChecklistItems(items: List<ChecklistItem>?): String {
        return checklistAdapter.toJson(items ?: emptyList())
    }

    @TypeConverter
    fun toChecklistItems(json: String?): List<ChecklistItem> {
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            checklistAdapter.fromJson(json) ?: emptyList()
        }
    }

    @TypeConverter
    fun fromRichText(content: RichTextContent?): String {
        return richTextAdapter.toJson(content ?: RichTextContent())
    }

    @TypeConverter
    fun toRichText(json: String?): RichTextContent {
        return if (json.isNullOrEmpty()) {
            RichTextContent()
        } else {
            richTextAdapter.fromJson(json) ?: RichTextContent()
        }
    }

    @TypeConverter
    fun fromCalculationTable(table: CalculationTable?): String {
        return calculationTableAdapter.toJson(table ?: CalculationTable())
    }

    @TypeConverter
    fun toCalculationTable(json: String?): CalculationTable {
        return if (json.isNullOrEmpty()) {
            CalculationTable()
        } else {
            calculationTableAdapter.fromJson(json) ?: CalculationTable()
        }
    }
}
