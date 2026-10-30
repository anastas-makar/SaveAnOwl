package pro.progr.saveanowl

import pro.progr.diagnostics.Diagnostics
import ru.ok.tracer.crash.report.TracerCrashReport

/** App-specific bridge from reusable modules to this app's Tracer project. */
object TracerDiagnostics : Diagnostics {
    override fun breadcrumb(event: String) {
        TracerCrashReport.log(event)
    }

    override fun reportNonFatal(error: Throwable, issueKey: String) {
        // Keep the original call stack while omitting exception messages and causes,
        // which may contain request or user data.
        val safeError = IllegalStateException(
            "Handled exception type=${error.javaClass.simpleName}"
        ).apply {
            stackTrace = error.stackTrace
        }
        TracerCrashReport.report(safeError, issueKey = issueKey)
    }
}
