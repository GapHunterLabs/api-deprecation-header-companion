package dev.gaphunter.apideprecationheadercompanion.detect

import com.intellij.psi.PsiFile
import dev.gaphunter.apideprecationheadercompanion.model.DeprecationHit
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/** Kotlin counterpart of [JavaDeprecationFinder]. */
object KotlinDeprecationFinder {

    fun findAll(file: PsiFile): List<DeprecationHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<DeprecationHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitNamedFunction(function: KtNamedFunction) {
                super.visitNamedFunction(function)
                if (!isDeprecated(function)) return
                if (!hasMapping(function)) return
                val body = function.bodyExpression ?: return
                if (DeprecationSignals.bodySetsDeprecationHeader(body.text)) return
                val nameIdentifier = function.nameIdentifier ?: return
                hits += DeprecationHit(nameIdentifier)
            }
        })
        return hits
    }

    private fun isDeprecated(function: KtNamedFunction): Boolean =
        function.annotationEntries.any { it.shortName?.asString() == "Deprecated" }

    private fun hasMapping(function: KtNamedFunction): Boolean =
        function.annotationEntries.any { it.shortName?.asString() in DeprecationSignals.MAPPING_ANNOTATIONS }
}
