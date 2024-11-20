package com.vyvyvi.caltrack.utils

object Utils {
    fun convertEmailToKey(email: String): String {
        return email.replace(".", "_").replace("@", "_at_").replace("$", "_d_")
            .replace("[", "_bo_").replace("]", "_ob_")
            .replace("#", "_h_")
    }
}