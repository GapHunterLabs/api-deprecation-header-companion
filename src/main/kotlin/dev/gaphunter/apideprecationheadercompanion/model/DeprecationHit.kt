package dev.gaphunter.apideprecationheadercompanion.model

import com.intellij.psi.PsiElement

/** One Spring endpoint method marked `@Deprecated` with no deprecation-related HTTP header set anywhere in its own body. */
data class DeprecationHit(val methodNameElement: PsiElement)
