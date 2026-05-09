package com.ahmed.airesumebuilder.data.remote


import com.ahmed.airesumebuilder.BuildConfig
import com.ahmed.airesumebuilder.domain.model.Experience
import com.ahmed.airesumebuilder.domain.model.Resume
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GeminiService @Inject constructor() {
    private val model = GenerativeModel(
        modelName = "gemini-pro", apiKey = BuildConfig.GEMINI_API_KEY
    )

    //Enhance Summery
    suspend fun enhanceSummery(currentSummary: String, jobTitle: String): String {
        val prompt = """
            You are an expert resume writer. Enhance the following professional summary for a $jobTitle.
            Make it compelling, concise, and ATS-friendly. Keep it under 4 sentences.
            Current Summary: $currentSummary
            Return only the enhanced summary, no explanations or additional text.
   """.trimIndent()
        return try {
            val response = model.generateContent(prompt)
            response.text?.trim() ?: currentSummary
        } catch (_: Exception) {
            currentSummary
        }
    }

    //Enhance Experience
    suspend fun enhanceExperienceDescription(experience: Experience): String {

        val prompt = """
            You are an expert resume writer. Enhance the following job description using action verbs
            and quantifiable achievements. Make it ATS-friendly and impactful.

            Job Title: ${experience.jobTitle}
            Company: ${experience.company}
            Current Description: ${experience.description}

            Return bullet points (3-5) starting with action verbs. Format each point on a new line
            starting with "•". Return only the bullet points, no explanations.
        """.trimIndent()


        return try {
            val response = model.generateContent(prompt)
            response.text?.trim() ?: experience.description
        } catch (_: Exception) {
            experience.description
        }

    }

    //Generate Skills Suggestion
    suspend fun generateSkillSuggestion(
        jobTitle: String,
        currentSkills: List<String>
    ): List<String> {

        val prompt = """
            You are an expert career advisor. Based on the job title "$jobTitle" and the current skills
            [${currentSkills.joinToString(", ")}], suggest 10 additional relevant skills that would
            strengthen this resume.
            
            Return only the skill names, one per line, no numbering or bullet points.
        """.trimIndent()




        return try {
            val response = model.generateContent(prompt)
            response.text?.lines()?.filter { it.isNotBlank() }?.take(10) ?: emptyList()

        } catch (_: Exception) {
            emptyList()
        }
    }

    //Generate Cover Later
    suspend fun generateCoverLater(
        resume: Resume,
        companyName: String,
        jobDescription: String,
    ): String {
        val prompt = """
            You are an expert cover letter writer. Create a compelling cover letter based on the following:

            Candidate Name: ${resume.personalInfo.fullName}
            Current/Target Role: ${resume.experiences.firstOrNull()?.jobTitle ?: "Professional"}
            Company: $companyName
            Professional Summary: ${resume.personalInfo.summary}
            Key Skills: ${resume.skills.take(5).joinToString(", ") { it.name }}
            Recent Experience: ${
            resume.experiences.firstOrNull()?.let {
                "${it.jobTitle} at ${it.company}: ${it.description}"
            } ?: "N/A"
        }

            Job Description: $jobDescription

            Create a professional cover letter (3-4 paragraphs) that:
            1. Shows enthusiasm for the role and company
            2. Highlights relevant experience and skills
            3. Demonstrates value the candidate can bring
            4. Includes a strong call to action

            Return only the cover letter text with proper formatting.
        """.trimIndent()


        return try {
            val response = model.generateContent(prompt)
            response.text?.trim() ?: "Unable to generate cover later"
        } catch (e: Exception) {
            "Error generating cover later ${e.message}"
        }

    }

    //Analysis ATS Resumes
    suspend fun analysisResumeForATS(
        resume: Resume, jobDescription: String
    ): ATSAnalysisResult {
        val prompt = """
    You are an ATS (Applicant Tracking System) expert. Analyze the following resume against
            the job description and provide a detailed analysis.
            
     Resume Details:
          - Name: ${resume.personalInfo.fullName}
            - Summary: ${resume.personalInfo.summary}
            - Skills: ${resume.skills.joinToString(", ") { it.name }}
            - Experience: ${resume.experiences.joinToString("\n") { "${it.jobTitle} at ${it.company}" }}
            - Education: ${resume.education.joinToString(", ") { "${it.degree} from ${it.institution}" }}
 
 
     Job Description: $jobDescription
            Provide:
            1. ATS Score (0-100)
            2. Matching Keywords (comma-separated)
            3. Missing Keywords (comma-separated)
            4. 3-5 Improvement Suggestions (one per line, starting with "•")

            Format your response exactly like this:
            SCORE: [number]
            MATCHING: [keywords]
            MISSING: [keywords]
            SUGGESTIONS:
            • suggestion 1
            • suggestion 2
""".trimIndent()

        return try {
            val response = model.generateContent(prompt)
            parseATSResponse(response.text ?: "")

        } catch (e: Exception) {
            ATSAnalysisResult(
                score = 0,
                matchingResult = emptyList(),
                missingKeywords = emptyList(),
                suggestions = listOf("Unable to analyze :${e.message}")

            )
        }
    }


    private fun parseATSResponse(response: String): ATSAnalysisResult {

        val lines = response.lines()
        var score = 0
        var matching = emptyList<String>()
        var missing = emptyList<String>()
        val suggestions = mutableListOf<String>()
        var isSuggestions = false

        for (line in lines) {
            when {
                line.startsWith("SCORE:") -> {
                    score = line.substringAfter("SCORE:").trim().toIntOrNull() ?: 0
                }

                line.startsWith("MATCHING:") -> {
                    matching = line.substringAfter("MATCHING:").split(",").map {
                        it.trim()
                    }.filter { it.isNotEmpty() }
                }

                line.startsWith("MISSING:") -> {
                    missing = line.substringAfter("MISSING:").split(",").map { it.trim() }
                        .filter { it.isNotEmpty() }
                }

                line.startsWith("SUGGESTIONS:") -> {
                    isSuggestions = true
                }

                isSuggestions && line.startsWith("•") -> {
                    suggestions.add(line.removePrefix("•").trim())
                }

            }
        }
        return ATSAnalysisResult(score, matching, missing, suggestions)
    }


    suspend fun suggestImprovements(resume: Resume): List<String> {
        val prompt = """
            You are a professional resume reviewer. Review the following resume and provide 5-7
            specific, actionable improvement suggestions.

            Resume:
            Name: ${resume.personalInfo.fullName}
            Email: ${resume.personalInfo.email}
            Summary: ${resume.personalInfo.summary}

            Experience:
            ${
            resume.experiences.joinToString("\n") {
                "${it.jobTitle} at ${it.company} (${it.startDate} - ${it.endDate})\n${it.description}"
            }
        }

            Education:
            ${resume.education.joinToString("\n") { "${it.degree} - ${it.institution} (${it.graduationYear})" }}

            Skills: ${resume.skills.joinToString(", ") { it.name }}

            Provide 5-7 specific improvement suggestions. Format each suggestion on a new line
            starting with "•". Focus on content, formatting, and impact.
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            response.text?.lines()?.filter { it.startsWith("•") }
                ?.map { it.removePrefix("•").trim() } ?: emptyList()

        } catch (e: Exception) {
            listOf("Unable to generate suggestions : ${e.message}")
        }

    }


    data class ATSAnalysisResult(

        val score: Int,
        val matchingResult: List<String>,
        val missingKeywords: List<String>,
        val suggestions: List<String>
    )


}