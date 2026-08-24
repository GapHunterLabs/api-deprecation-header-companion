package dev.gaphunter.apideprecationheadercompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import dev.gaphunter.apideprecationheadercompanion.model.DeprecationHit

/**
 * Finds Java Spring MVC endpoint methods annotated `@Deprecated` whose
 * body never sets a deprecation-related HTTP response header
 * ([DeprecationSignals]) -- the handler tells the IDE/compiler it's
 * deprecated but tells real API consumers nothing, since nothing in the
 * actual HTTP response signals it.
 */
object JavaDeprecationFinder {

    fun findAll(file: PsiFile): List<DeprecationHit> {
        val hits = mutableListOf<DeprecationHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitMethod(method: PsiMethod) {
                super.visitMethod(method)
                if (!isDeprecated(method)) return
                if (!hasMapping(method)) return
                val body = method.body ?: return
                if (DeprecationSignals.bodySetsDeprecationHeader(body.text)) return
                val nameIdentifier = method.nameIdentifier ?: return
                hits += DeprecationHit(nameIdentifier)
            }
        })
        return hits
    }

    private fun isDeprecated(method: PsiMethod): Boolean =
        method.modifierList?.annotations.orEmpty().any { it.nameReferenceElement?.referenceName == "Deprecated" }

    private fun hasMapping(method: PsiMethod): Boolean =
        method.modifierList?.annotations.orEmpty().any {
            it.nameReferenceElement?.referenceName in DeprecationSignals.MAPPING_ANNOTATIONS
        }
}
