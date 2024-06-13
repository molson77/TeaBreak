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

import kotlinx.coroutines.flow.Flow

class OfflineTeasRepository(private val teaDao: TeaDao) : TeasRepository {
    override fun getAllTeasStream(): Flow<List<Tea>> = teaDao.getAllTeas()

    override fun getTeaStream(id: Int): Flow<Tea?> = teaDao.getTea(id)

    override suspend fun insertTea(tea: Tea) = teaDao.insert(tea)

    override suspend fun deleteTea(tea: Tea) = teaDao.delete(tea)

    override suspend fun updateTea(tea: Tea) = teaDao.update(tea)
}
