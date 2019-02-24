package main.presentation.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ModelTest {

    private lateinit var model: RngModel

    @Before
    fun setUp() {
        model = RngModel()
    }

    @Test
    fun `test index for value 067`() {
        assertEquals(13, model.getIntervalIndex(0.674, 0.0, 1.0, 20))
        assertEquals(13, model.getIntervalIndex(0.687744140625, 0.0, 1.0, 20))
        assertEquals(13, model.getIntervalIndex(0.666259765625, 0.0, 1.0, 20))
        assertEquals(13, model.getIntervalIndex(0.671142578125, 0.0, 1.0, 20))
    }
}