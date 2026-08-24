package dev.gaphunter.apideprecationheadercompanion.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import dev.gaphunter.apideprecationheadercompanion.detect.JavaDeprecationFinder
import dev.gaphunter.apideprecationheadercompanion.detect.KotlinDeprecationFinder
import dev.gaphunter.apideprecationheadercompanion.model.DeprecationHit

class MissingDeprecationHeaderLineMarkerProvider : LineMarkerProviderDescriptor(), DumbAware {

    override fun getName(): String = "Missing deprecation HTTP header"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val file = elements.firstOrNull()?.containingFile ?: return
        val hits = when (file.language.id) {
            "JAVA" -> JavaDeprecationFinder.findAll(file)
            "kotlin" -> KotlinDeprecationFinder.findAll(file)
            else -> emptyList()
        }
        if (hits.isEmpty()) return

        val hitsByElement = hits.associateBy { it.methodNameElement }
        for (element in elements) {
            val hit = hitsByElement[element] ?: continue
            result.add(buildMarker(hit))
        }
    }

    private fun buildMarker(hit: DeprecationHit): LineMarkerInfo<PsiElement> {
        val tooltip = "This endpoint is @Deprecated but its body never sets a Deprecation/Sunset HTTP header -- " +
            "real API consumers (not just IDE users) have no way to know this endpoint is going away"
        return LineMarkerInfo(
            hit.methodNameElement,
            hit.methodNameElement.textRange,
            DeprecationIcons.RISK,
            { _: PsiElement -> tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip },
        )
    }
}
