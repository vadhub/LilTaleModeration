package com.abg.liltalemoderation.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abg.liltalemoderation.model.pojo.Audio

@Dao
interface AudioDao {

    @Query("SELECT * FROM audio_table WHERE id =:id")
    suspend fun getById(id: Long): Audio?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audio: Audio)

    @Query("DELETE FROM audio_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM audio_table")
    suspend fun deleteAll()
}