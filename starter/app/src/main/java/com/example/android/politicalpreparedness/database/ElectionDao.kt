package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: Election)

    @Query("INSERT INTO election_table (id) values(:electionId)")
    suspend fun followElection(electionId: Int)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table")
    fun getElections(): List<Election>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElection(id: Int):LiveData<Election?>

    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun getElection2(electionId: Int) : Election

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :electionId")
    fun deleteElection(electionId: Int)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    suspend fun clearAll()

    //https://stackoverflow.com/questions/60585796/how-do-i-check-if-theres-a-certain-item-in-database-when-using-room-in-android
    @Query("SELECT EXISTS (SELECT 1 FROM election_table WHERE id = :id)")
    fun exists(id: Int): Boolean




}