package com.ahmed.airesumebuilder.presentation.screens.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.airesumebuilder.data.repository.ResumeRepository
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.PdfGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class PreviewUiState(
    val resume: Resume? = null,
    val isLoading: Boolean = false,
    val pdfFile: File? = null,
    val errorMessage: String? = null
)


@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository,
    private val pdfGenerator: PdfGenerator
) : ViewModel() {
    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState = _uiState.asStateFlow()

    fun loadResume(resumeId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            resumeRepository.getResumeById(resumeId)
                .filterNotNull()
                .collect { resume ->
                    _uiState.update {
                        it.copy(resume = resume, isLoading = false)
                    }
                }


        }
    }

    fun exportToPdf() {
        val resume = _uiState.value.resume ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val file = pdfGenerator.generatePdf(resume)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    pdfFile = file,
                    errorMessage = if (file == null) "Failed to generate PDF" else null
                )
            }

        }
    }

    fun clearPdfFile() = _uiState.update { it.copy(pdfFile = null) }

}