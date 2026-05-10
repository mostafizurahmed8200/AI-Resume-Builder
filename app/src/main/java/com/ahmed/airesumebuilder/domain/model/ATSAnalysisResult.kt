package com.ahmed.airesumebuilder.domain.model

data class ATSAnalysisResult(
    val score: Int,
    val matchingResult: List<String>,
    val missingKeywords: List<String>,
    val suggestions: List<String>
)
