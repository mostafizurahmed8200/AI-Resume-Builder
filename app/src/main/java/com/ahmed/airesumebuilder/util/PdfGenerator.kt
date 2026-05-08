package com.ahmed.airesumebuilder.util

import com.google.api.Context
import javax.inject.Inject

class PdfGenerator @Inject constructor(
    private val context: Context
) {
    fun generatePdf(resume: Resume)
}