package com.ahmed.airesumebuilder.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.ahmed.airesumebuilder.domain.model.Resume

import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PdfGenerator @Inject constructor(
    private val context: Context
) {
    fun generatePdf(resume: Resume): File? {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint().apply {
                textSize = 12f
                color = android.graphics.Color.BLACK
            }
            var yPosition = 50f

            //Name
            printText(
                24f, true, resume.personalInfo.fullName, 50f, yPosition
            )

            yPosition += 30f

            //Contact Info
            val contactInfo =
                "${resume.personalInfo.email} | ${resume.personalInfo.phone} |  ${resume.personalInfo.location} "
            printText(
                10f, false, contactInfo, 50f, yPosition
            )

            yPosition += 30f

            //Summery
            if (resume.personalInfo.summary.isNotEmpty()) {

                printText(
                    14f, true, "PROFESSIONAL SUMMERY", 50f, yPosition
                )

                yPosition += 20f

                paint.textSize = 10f
                paint.isFakeBoldText = false
                val summeryLines = wrapText(resume.personalInfo.summary)
                summeryLines.forEach { line ->
                    canvas.drawText(line, 50f, yPosition, paint)
                    yPosition += 15f
                }


            }

            //Experience
            if (resume.experiences.isNotEmpty()) {
                printText(
                    14f, true, "EXPERIENCE", 50f, yPosition
                )
                yPosition += 20f

                resume.experiences.forEach { experience ->
                    //Job Title
                    printText(
                        12f, true, experience.jobTitle, 50f, yPosition
                    )
                    yPosition += 15f


                    val expDetails =
                        "${experience.company} |${experience.startDate} | ${experience.endDate}"
                    printText(
                        10f, true, expDetails, 50f, yPosition
                    )
                    yPosition += 15f

                    val desLines = wrapText(experience.description)
                    desLines.forEach { line ->
                        canvas.drawText("• $line", 60f, yPosition, paint)
                        yPosition += 15f
                    }
                    yPosition += 10f


                }

            }

            //Education
            if (resume.education.isNotEmpty()) {
                printText(
                    14f, true, "Education", 50f, yPosition
                )
                yPosition += 15f

                resume.education.forEach { education ->
                    printText(12f, true, education.degree, 50f, yPosition)
                    yPosition += 15f

                    val eduDetails = "${education.institution} | ${education.graduationYear}"
                    printText(
                        10f, false, eduDetails, 50f, yPosition
                    )
                    yPosition += 20f

                }
            }

            //Skills
            if (resume.skills.isNotEmpty()) {
                printText(14f, true, "SKILLS", 50f, yPosition)
                yPosition += 20f

                paint.textSize = 10f
                paint.isFakeBoldText = false
                val skillsText = resume.skills.joinToString(" • ") { it.name }
                val skillLines = wrapText(skillsText)
                skillLines.forEach { line ->
                    canvas.drawText(line, 50f, yPosition, paint)
                    yPosition += 15f
                }
            }
            document.finishPage(page)

            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val file =
                File(downloadDir, "Resume_${resume.personalInfo.fullName.replace(" ", "_")}.pdf")

            FileOutputStream(file).use { outputStream ->
                document.writeTo(outputStream)
            }

            document.close()
            file

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    }


    private fun wrapText(text: String): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        words.forEach { words ->
            if (currentLine.length + words.length + 1 <= 80) {
                if (currentLine.isNotEmpty()) currentLine.append(" ")
                currentLine.append(words)
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                }
                currentLine = StringBuilder(words)
            }
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        return lines
    }

    fun printText(
        textSize: Float,
        isFakeBoldText: Boolean,
        drawText: String,
        drawTextSize: Float,
        drawTextPosition: Float,
    ) {
        val paint = Paint()
        val canvas = Canvas()
        paint.textSize = textSize
        paint.isFakeBoldText = isFakeBoldText
        canvas.drawText(drawText, drawTextSize, drawTextPosition, paint)
    }


}