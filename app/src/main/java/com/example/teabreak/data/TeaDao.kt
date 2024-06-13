/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.teabreak.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TeaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tea: Tea)

    @Update
    suspend fun update(tea: Tea)

    @Delete
    suspend fun delete(tea: Tea)

    @Query("SELECT * from teas WHERE id = :id")
    fun getTea(id: Int): Flow<Tea>

    @Query("SELECT * from teas ORDER BY name ASC")
    fun getAllTeas(): Flow<List<Tea>>
}
