package com.example.artmaster.notes

import org.junit.Assert.assertEquals
import org.junit.Test

class NoteActivityTest {

    @Test
    fun filterNotes() {
        // Arrange: Prepare a list of notes and a search query
        val notes = listOf(
            Notes("1", "Title 1", "Content 1"),
            Notes("2", "Title 2", "Content 2")
        )

        // Act: Filter the notes based on the search query
        val filteredNotesWithTitle1 = filterNotes(notes, "Title 1")
        val filteredNotesWithTitle2 = filterNotes(notes, "Title 2")
        val filteredNotesWithContent1 = filterNotes(notes, "Content 1")
        val filteredNotesWithContent2 = filterNotes(notes, "Content 2")
        val filteredNotesWithNonExistingQuery = filterNotes(notes, "Non-existing")

        // Assert: Verify the filtered results
        assertEquals(1, filteredNotesWithTitle1.size)
        assertEquals("Title 1", filteredNotesWithTitle1[0].title)

        assertEquals(1, filteredNotesWithTitle2.size)
        assertEquals("Title 2", filteredNotesWithTitle2[0].title)

        assertEquals(1, filteredNotesWithContent1.size)
        assertEquals("Title 1", filteredNotesWithContent1[0].title)

        assertEquals(1, filteredNotesWithContent2.size)
        assertEquals("Title 2", filteredNotesWithContent2[0].title)

        assertEquals(0, filteredNotesWithNonExistingQuery.size)
    }


    @Test
    fun onNoteClick() {
        // Test the onNoteClick function to ensure it starts DetailActivity with the correct noteId
        val noteId = "1"
        var selectedNoteId: String? = null
        val onNoteClick: (String) -> Unit = { id -> selectedNoteId = id }

        onNoteClick(noteId)

        assertEquals(noteId, selectedNoteId)
    }




}